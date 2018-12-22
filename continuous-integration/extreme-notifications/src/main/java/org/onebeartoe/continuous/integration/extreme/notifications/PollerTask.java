
package org.onebeartoe.continuous.integration.extreme.notifications;

import com.sun.syndication.io.FeedException;
import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import org.onebeartoe.system.Sleeper;

/**
 *
 * @author Roberto Marquez
 */
    /**
     *  The task class is to 
     *  poll the RSS feed for all jobs status entries.
     */
public class PollerTask extends TimerTask
{
    private Map<Integer, LedStatusIndicatorStrip> knownIndicatorStrips;
    
    private long POLL_DELAY;
    
    private Logger logger;
    
    private PrintWriter output;
    
    private String rssUrl;
    
    private JenkinsRssFeedService rssService;

    public PollerTask(PollerProfile pp)
    {
        logger = Logger.getLogger( getClass().getName() );
        
        knownIndicatorStrips = pp.getKnownIndicatorStrips();
        
        POLL_DELAY = pp.getPollDelay();
        
        output = pp.getOutput();
        
        rssUrl = pp.getRssUrl();
        
        rssService = pp.getRssService();
    }
    
    /**
     * Builds an Arduino messages in this format 'stip-index:led-index:rrr:ggg:bbb:pulse'
     * @param strip
     * @param job
     * @return example value: '000:000:000:255:000:1', which means strip 0, led 0, rgb(0,255,0), pulsing = true
     * 
     *         other examples:
     * 
     * 
                    green
                    000:000:000:255:000:1

                    blue
                    000:000:000:000:130:1

                    medium
                    000:000:130:130:130:1
     * 
     */
    private String buildArduinoMessage(int strip, JenkinsJob job)
    {        
        final String threeDigitFormat = "%03d";

        String stripIndex = String.format(threeDigitFormat, strip);
        
        String neopixelIndex = String.format(threeDigitFormat, job.getNeopixelIndex());
        
        Color c = job.getJobStatus().getColor();
        
        int red = c.getRed();
        int green = c.getGreen();
        int blue = c.getBlue();
        
        String r = String.format(threeDigitFormat, red);
        String g = String.format(threeDigitFormat, green);
        String b = String.format(threeDigitFormat, blue);
        
        int pulseCode = 0;
        if(job.getJobStatus() == JenkinsJobStatus.IN_PROGRESS)
        {
            pulseCode = 1;
        }
        String pulsing = String.valueOf(pulseCode);
     
        StringBuilder message = new StringBuilder();
        message.append(stripIndex);
        message.append(":");
        
        message.append(neopixelIndex);
        message.append(":");
        
        message.append(r);
        message.append(":");
        
        message.append(g);
        message.append(":");
        
        message.append(b);
        message.append(":");
        
        // specify whether the LED is pulsing or not
        message.append(pulsing);
        
        return message.toString();
    }    
    
    private List<JenkinsJob> filter(int indicatorIndex, List<JenkinsJob> allRssJobs)
    {
        List<String> unknownKeys = new ArrayList();

        List<JenkinsJob> configuredJobs = new ArrayList();

        LedStatusIndicatorStrip configuredStrip = knownIndicatorStrips.get(indicatorIndex);

        allRssJobs.forEach(rssJob -> 
        {
            String targetName = rssJob.getJobName();

            boolean found = false;
            for(JenkinsJob configuredJob : configuredStrip.jobs)
            {
                if( targetName.equals(configuredJob.getJobName() ) )
                {
                    rssJob.setNeopixelIndex( configuredJob.getNeopixelIndex() );
                    configuredJobs.add(rssJob);

                    found = true;

                    break;
                }
            };

            if(!found)
            {
                unknownKeys.add(rssJob.getJobName());
            }                
        });

        printUnknownRssJobs(unknownKeys);

        return configuredJobs;
    }
        
    private void printUnknownRssJobs(List<String> unknownRssJobs)
    {
        System.out.println();
        System.out.print("There is no pixel id for: ");
        unknownRssJobs.forEach( (s) ->
        {
            System.out.print(s);
            System.out.print(", ");        
        });
        System.out.println();
    }
        
    @Override
    public void run() 
    {
        System.out.println();
        System.out.println("The Jenkins RSS Poller goes off every " + POLL_DELAY + " millieseconds.");

        List<JenkinsJob> allJobs;
        try 
        {
            allJobs = obtainAllRssJobs();

            // TODO: remove the hard-coded nature of assuming there will always be 3 strips
            IntStream.rangeClosed(0, 2).forEach( i ->
            {
                List<JenkinsJob> configuredJobs = filter(i, allJobs);

                updateNeopixelStrip(i, configuredJobs);
            });
        } 
        catch (FeedException | IOException ex) 
        {
            String message = "An error occured while obtaining the Jenkins jobs.";
            logger.log(Level.SEVERE, message, ex);
        }
    }

    private void sendArduinoMessage(int strip, JenkinsJob job)
    {
        if(output == null)
        {
            System.err.println("The variable, output, is null;");
            System.err.println("No message sent to the serial port connection.");
        }
        else
        {
            String message = buildArduinoMessage(strip, job);
            output.println(message);
            output.flush();
        }
    }    
    
    private List<JenkinsJob> obtainAllRssJobs() throws MalformedURLException, FeedException, IOException
    {
        URL url = new URL(rssUrl);
        
        List<JenkinsJob> jobs = rssService.getJobs(url);
        
        return jobs;
    }

    /**
     * This method
     * sends the Arduino/serial communication port the corresponding
     * LED information.
     * 
     * @param strip
     * @param configuredJobs 
     */
    private void updateNeopixelStrip(int strip, List<JenkinsJob> configuredJobs)
    {   
        LedStatusIndicatorStrip lsi = knownIndicatorStrips.get(strip);
        
        for(JenkinsJob jj : configuredJobs)
        {
            String key = jj.getJobName();
            
            String consoleMessage = "\t\t\t\t\t" + "Sending data for " + key + 
                                    " (neopixel " + jj.getNeopixelIndex() + ")";
            System.out.println(consoleMessage);

            sendArduinoMessage(strip, jj);

            // Give the Arduino time to receive the data before sending the next one.
            int delayInSeconds = 2;  // used to be 4
            long iterationDelay = Duration.ofSeconds(delayInSeconds).toMillis();
            Sleeper.sleepo(iterationDelay);
        }
    }
}
