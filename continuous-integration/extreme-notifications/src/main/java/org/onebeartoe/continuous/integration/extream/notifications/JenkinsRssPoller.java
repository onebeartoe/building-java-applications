
package org.onebeartoe.continuous.integration.extream.notifications;

import com.sun.syndication.io.FeedException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.onebeartoe.io.serial.SerialPorts;
import org.onebeartoe.system.Sleeper;

/**
 * 
 * I found a 64bit version of rxtx for Windows here:
 * 
 *      http://www.openremote.org/display/forums/Zwave+rxtxSerial+dll+Can't+load+IA+32bit
 * 
 * Start the app with this command:
 * 
 *      java -Djava.library.path="C:\home\world\installs\software-development\java\ch-rxtx-2.2-20081207-win-x64\ch-rxtx-2.2-20081207-win-x64" -jar continuous-integration/extreme-notifications/target/extreme-notifications-1.0-SNAPSHOT-jar-with-dependencies.jar
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
    
    public static final String rssUrl =
//      "https://onebeartoe.ci.cloudbees.com/rssAll";
        "https://onebeartoe.ci.cloudbees.com/rssLatest";
    
    private Map<String, Integer> jobsToNeopixels;
    
    private Logger logger;
    
    public JenkinsRssPoller() throws Exception
    {
        String className = getClass().getName();
        logger = Logger.getLogger(className);
        
//        POLL_DELAY = Duration.ofSeconds(30).toMillis();
        POLL_DELAY = Duration.ofMinutes(5).toMillis();
        
        rssService = new HttpJenkinsRssFeedService();
        
        initializeSerialPort();
        
        mapJobsToNeopixels();
    }
    
    private void initializeSerialPort() throws Exception
    {
        System.out.println("obtaining serial port");
        SerialPort serialPort = SerialPorts.get("COM7");
        System.out.println("serial port obtained");
        Sleeper.sleepo(2000);

        InputStream is = serialPort.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        input = new BufferedReader(isr);
        
        OutputStream outputStream = serialPort.getOutputStream();
        
        output = new PrintWriter(outputStream);

        serialPort.addEventListener(this);        
    }
    
    public static void main (String [] args) throws Exception
    {
        JenkinsRssPoller poller = new JenkinsRssPoller();
        
        poller.start();
    }
    
    private void mapJobsToNeopixels()
    {
        jobsToNeopixels = new HashMap();

        jobsToNeopixels.put("building-java-applications", 0);
        jobsToNeopixels.put("electronic-signs",           1);
        jobsToNeopixels.put("electronics",             2);
        jobsToNeopixels.put("pixel",                      3);
    }

    private List<JenkinsJob> obtainJobs() throws MalformedURLException, FeedException, IOException
    {
        URL url = new URL(rssUrl);
        
        List<JenkinsJob> jobs = rssService.getJobs(url);
        
        return jobs;
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

                System.out.println("\t\t\t\t\t" + "received: " + inputLine);
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
    
    private void updateNeopixels(List<JenkinsJob> jobs)
    {
        for(JenkinsJob jj : jobs)
        {
            String key = jj.getJobName();
            Integer neopixelId = jobsToNeopixels.get(key);
            
            if(neopixelId == null)
            {
                System.out.println("There is no pixel id for " + key);
            }
            else
            {
                System.out.println("\t\t\t\t\t" + "Sending data for " + key + " (neopixel " + neopixelId + ")");
                output.println(neopixelId);
                output.flush();                
            
                // Give the Arduino time to receive the data before sending the next one.
                long iterationDelay = Duration.ofSeconds(4).toMillis();            
                Sleeper.sleepo(iterationDelay);   
            }
        }
    }
    
    class PollerTask extends TimerTask
    {
        @Override
        public void run() 
        {
            System.out.println("I go off every " + POLL_DELAY + " millieseconds");

            List<JenkinsJob> jobs;
            try 
            {
                jobs = obtainJobs();
                updateNeopixels(jobs);
            } 
            catch (FeedException | IOException ex) 
            {
                String message = "An error occured while obtaining the Jenkins jobs.";
                logger.log(Level.SEVERE, message, ex);
            }
        }
    }
}