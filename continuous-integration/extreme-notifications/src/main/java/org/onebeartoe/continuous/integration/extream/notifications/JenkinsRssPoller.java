
package org.onebeartoe.continuous.integration.extream.notifications;

import com.sun.syndication.io.FeedException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.awt.Color;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import org.onebeartoe.io.TextFileReader;
import org.onebeartoe.io.buffered.BufferedTextFileReader;

import org.onebeartoe.io.serial.SerialPorts;
import org.onebeartoe.system.Sleeper;

/**
 * 
 * I found a 64bit version of rxtx for Windows here:
 * 
 *      http://www.openremote.org/display/forums/Zwave+rxtxSerial+dll+Can't+load+IA+32bit
 * 
 * Start the app on MS Windows with this command:
 * 
 *      java -Djava.library.path="C:\home\world\installs\software-development\java\ch-rxtx-2.2-20081207-win-x64\ch-rxtx-2.2-20081207-win-x64" -jar continuous-integration/extreme-notifications/target/extreme-notifications-1.0-SNAPSHOT-jar-with-dependencies.jar COM7 strip-job.mapping http://jenkins-server/view/All/rssLatest
 *
 * On Linux this command works with the RXTX provided by apt-get:
 *
 *       java -Djava.library.path=/usr/lib/jni/ -jar extreme-notifications-1.0-SNAPSHOT-jar-with-dependencies.jar /dev/ttyACM0 strip-job.mapping
 *
 * To run/debug from Netbeans, add the -Djava.library.path="C:\home\......" path above to the VM arguments via 'Right Click Project' -> Properties -> Run.
 * 
 * @author Roberto Marquez
 */
public class JenkinsRssPoller implements SerialPortEventListener
{
    private final long POLL_DELAY;
    
    private BufferedReader input;
    
    private PrintWriter output;
    
    private JenkinsRssFeedService rssService;
    
    public static String rssUrl =
//      "https://onebeartoe.ci.cloudbees.com/rssAll";
        "https://onebeartoe.ci.cloudbees.com/rssLatest";
    
    private Logger logger;
    
    private String configPath;
     
      private Map<Integer, LedStatusIndicatorStrip> knownIndicatorStrips;
      
    public JenkinsRssPoller(String port) throws Exception
    {        
        this(port, "./src/main/resources/strip-job.mapping");
    }
    
    public JenkinsRssPoller(String port, String configPath) throws Exception
    {
        this.configPath = configPath;
        
        String className = getClass().getName();
        logger = Logger.getLogger(className);
        
        POLL_DELAY = Duration.ofSeconds(30).toMillis();
        
        rssService = new HttpJenkinsRssFeedService();
        
        initializeSerialPort(port);
        
        knownIndicatorStrips = new HashMap();
        
        loadConfiguration();
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
    
    private static JenkinsRssPoller initialzeRssPoller(String [] args) throws Exception
    {
        // The app uses the following as the defalut port descriptor on a MS 
        // Windows environment.  On Linux environments, use a path to the serial 
        // communication port.
        String port = "COM7";
        
        String configPath = null;
        
        JenkinsRssPoller poller = null;
        
        if(args.length == 0)
        {
            poller = new JenkinsRssPoller(port);
        }
        
        if(args.length == 1)
        {
            // grab the serial communiation port
            port = args[0];
            
            poller = new JenkinsRssPoller(port);
        }
        
        if(args.length >= 2)
        {
            // There are at least 2 command line arguments.
            // The first two are the same as above.
            
            port = args[0];
            
            // grab the configuration path
            configPath = args[1];
            
            poller = new JenkinsRssPoller(port, configPath);
            
            if(args.length > 2)
            {
                // The third command line argument is the URL of the Jenkins RSS feed.
                rssUrl = args[2];
            }
        }
        
        System.out.println("starting poller with COM port and config path of: " + port + " / " + configPath);
        
        return poller;
    }
    
    private void initializeSerialPort(String port) throws Exception
    {
        System.out.println("obtaining serial port");
        SerialPort serialPort = SerialPorts.get(port);
        
        System.out.println();
        System.out.println("serial port obtained");
        Sleeper.sleepo(2000);

        InputStream is = serialPort.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        input = new BufferedReader(isr);
        
        OutputStream outputStream = serialPort.getOutputStream();
        
        output = new PrintWriter(outputStream);

        serialPort.addEventListener(this);        
    }
    
    /**
     * 
     * @param args The first argument is either the path or name of the serial 
     * communication port.
     * 
     * @throws Exception 
     */
    public static void main (String [] args) throws Exception
    {
        System.out.println("args: ");
        for(String a : args)
        {
           System.out.println(a + " ") ;
        }
        
        JenkinsRssPoller poller = initialzeRssPoller(args);
                
        poller.start();
    }
    
    /**
     * This method is only called once per application run.
     * It loads a configuration file with the LED strip to RSS feed mapping, 
     * as well as the mapping per strip of the Jenkins job to strip index mapping.
     * 
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private void loadConfiguration() throws FileNotFoundException, IOException
    {
        TextFileReader textFileReader = new BufferedTextFileReader();
        File infile = new File(configPath);
        InputStream instream = new FileInputStream(infile);
        List<String> lines = textFileReader.readLines(instream);
        
        lines.stream()
            .filter( s -> !s.trim().startsWith("#") )
            .filter( s -> s.trim().length() != 0 )
            .forEach(c -> 
            {
                String[] split = c.split(":");

                int ledStrip = Integer.valueOf(split[0]);
                int ledIndex = Integer.valueOf(split[1]);
                String jobName = split[2];

                JenkinsJob jj = new JenkinsJob();
                jj.setJobName(jobName);
                jj.setNeopixelIndex(ledIndex);
                
                LedStatusIndicatorStrip lsis = knownIndicatorStrips.get(ledStrip);
                
                if(lsis == null)
                {
                    lsis = new LedStatusIndicatorStrip();
                    knownIndicatorStrips.put(ledStrip, lsis);
                }

                lsis.jobs.add(jj);
            });
    }

    private List<JenkinsJob> obtainAllRssJobs() throws MalformedURLException, FeedException, IOException
    {
        URL url = new URL(rssUrl);
        
        List<JenkinsJob> jobs = rssService.getJobs(url);
        
        return jobs;
    }
    
    private void sendArduinoMessage(int strip, JenkinsJob job)
    {
        String message = buildArduinoMessage(strip, job);

        output.println(message);
        output.flush();
    }
    
    /**
     * Handle an event on the serial port. Read the data and print it.
     */
    @Override
    public synchronized void serialEvent(SerialPortEvent event) 
    {
        if(event.getEventType() == SerialPortEvent.DATA_AVAILABLE) 
        {
            try 
            {
                String inputLine = input.readLine();
                
                if(inputLine.trim().equals(""))
                {
                    System.out.println();
                }
                else
                {
                    System.out.println("\t\t\t\t\t" + "received from Arduino: " + inputLine);
                }
            } 
            catch (Exception e) 
            {
                System.err.println(e.toString());
            }
        }        
    }


    public void start()
    {
        TimerTask pollerTask = new PollerTask();
        
        Timer timer = new Timer();
        
        Date firstTime = new Date();
        
        timer.schedule(pollerTask, firstTime, POLL_DELAY);
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
            long iterationDelay = Duration.ofSeconds(4).toMillis();            
            Sleeper.sleepo(iterationDelay);
        }
    }
    
    /**
     *  The task class is to 
     *  poll the RSS feed for all jobs status entries.
     */
    class PollerTask extends TimerTask
    {   
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
                
                // TODO: remove the hard-coded nature of assuming there will always be 2 strips
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
    }
}
