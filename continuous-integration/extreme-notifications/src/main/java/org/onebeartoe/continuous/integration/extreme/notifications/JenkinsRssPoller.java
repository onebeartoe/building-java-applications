
package org.onebeartoe.continuous.integration.extreme.notifications;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.onebeartoe.io.TextFileReader;
import org.onebeartoe.io.buffered.BufferedTextFileReader;

import org.onebeartoe.network.ClasspathResourceHttpHandler;
import org.onebeartoe.network.EndOfRunHttpHandler;
import org.onebeartoe.network.TextHttpHandler;

/**
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
public class JenkinsRssPoller
{
    private final static String JOB_MAPPING_PATH = "jobMappingPath";
    
    private final static String PORT = "port";
    
    private final static String RSS_URL = "rssUrl";
    
    private final long POLL_DELAY;
    
    private BufferedReader input;
    
    private PrintWriter output;
    
    private JenkinsRssFeedService rssService;
    
    private String rssUrl = "https://onebeartoe.ci.cloudbees.com/rssLatest";
    
    private Logger logger;
     
    private Map<Integer, LedStatusIndicatorStrip> knownIndicatorStrips;
    
    private HttpServer server;

    private JenkinsRssRunProfile runProfile;
    
    public JenkinsRssPoller(String [] args) throws Exception
    {
        Options options = buildOptions();

        runProfile = parseRunProfile(args, options);

        String className = getClass().getName();
        logger = Logger.getLogger(className);
        
        POLL_DELAY = Duration.ofSeconds(30).toMillis();
        
        rssService = new HttpJenkinsRssFeedService();
    }
    
    public static Options buildOptions()
    {
        Option port = Option.builder()
                                .hasArg()
                                .longOpt(PORT)
                                .build();
        
        Option jobMappingPath = Option.builder()
                .hasArg()
                .longOpt(JOB_MAPPING_PATH)
                .build();
        
        Option rssUrl = Option.builder()
                .hasArg()
                .longOpt(RSS_URL)
                .build();
        
        Options options = new Options();
        options.addOption(port);
        options.addOption(jobMappingPath);
        options.addOption(rssUrl);
        
        return options;
    }    
    
    /**
     * 
     * @param args
     * @param options
     * @return
     * @throws Exception 
     */
    private JenkinsRssRunProfile parseRunProfile(String [] args, Options options) throws Exception
    {
        // The app uses the following as the defalut port descriptor on a MS 
        // Windows environment.  On Linux environments, use a path to the serial 
        // communication port.
        String port = null;
        
        String configPath = null;

        CommandLineParser parser = new DefaultParser();
        CommandLine cl = parser.parse(options, args);
        
        if( cl.hasOption(PORT) )
        {
            port = cl.getOptionValue(PORT);
        }
        else
        {
            port = "COM7";
        }
        JenkinsRssRunProfile rp = new JenkinsRssRunProfile();
        rp.setPort(port);
        
        if( cl.hasOption(JOB_MAPPING_PATH) )
        {
            String mappingPath = cl.getOptionValue(JOB_MAPPING_PATH);
            rp.setJobMappingPath(mappingPath);
        }
        else
        {
            configPath =  "./src/main/resources/strip-job.mapping";
        }

        if( cl.hasOption(RSS_URL) )
        {
            // This command line argument is the URL of the Jenkins RSS feed.
            String rssUrl = cl.getOptionValue(RSS_URL);
            rp.setRssUrl(rssUrl);
        }

        System.out.println("starting poller with COM port and config path of: " + port + " / " + configPath);
        
        return rp;
    }
 
    public void initialize() throws IOException
    {
        knownIndicatorStrips = new HashMap();
        
        loadConfiguration();
        
        String port = runProfile.getPort();
        try
        {
            initializeSerialPort(port);
        }
        catch(Exception e)
        {
            String message = "The serial port was not obtained.";
            System.err.println(message);
            e.printStackTrace();
        }
    }
    
    private void initializeSerialPort(String port) throws Exception
    {
        ArduinoListener arduinoListener = new ArduinoListener(port);
        
        input = arduinoListener.getInputStream();
        
        output = arduinoListener.getOutputStream();
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

        JenkinsRssPoller poller = new JenkinsRssPoller(args);

        poller.initialize();
        
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
        
        String configPath = runProfile.getJobMappingPath();
        
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

//    public void setRssUrl(String url)
//    {
//        rssUrl = url;
//    }

    public void start()
    {
        PollerProfile pp = new PollerProfile();
        pp.setKnownIndicatorStrips(knownIndicatorStrips);
        pp.setPollDelay(POLL_DELAY);
        pp.setOutput(output);
        pp.setRssUrl( runProfile.getRssUrl() );
        pp.setRssServie(rssService);
        
 //       pp.set
                
        TimerTask pollerTask = new PollerTask(pp);

        Timer timer = new Timer();
        
        Date firstTime = new Date();
        
        timer.schedule(pollerTask, firstTime, POLL_DELAY);
        
        startHttpServer();
    }
    
    private void startHttpServer()
    {
        try 
        {
            InetSocketAddress anyhost = new InetSocketAddress(1951);
            int connectionMax = 10;
            
            server = HttpServer.create(anyhost, connectionMax);
            
            HttpHandler userInterfaceHttpHander = new ClasspathResourceHttpHandler();
            HttpHandler quitHttpHandler = new EndOfRunHttpHandler(server);
            HttpHandler configHttpHandler = new ConfigHttpHandler();
            
            server.createContext("/", userInterfaceHttpHander);
            server.createContext("/quit", quitHttpHandler);
            server.createContext("/reload-configurtion", configHttpHandler);
            
            server.start();
        } 
        catch (IOException ex) 
        {
            logger.log(Level.SEVERE, null, ex);
        }
    }
    
    private class ConfigHttpHandler extends TextHttpHandler
    {
        @Override
        protected String getHttpText(HttpExchange exchange) 
        {
            String message = "The job configuration re-loaded.";
            
            knownIndicatorStrips.clear();
            try 
            {
                loadConfiguration();
            } 
            catch (IOException ex) 
            {
                message = "An error occured while reloading the job configuration.";
                logger.log(Level.SEVERE, message, ex);
            }
            
            return message;
        }
    }
}
