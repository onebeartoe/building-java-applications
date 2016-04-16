
package org.onebeartoe.continuous.integration.extream.notifications;

import java.time.Duration;
import java.time.Period;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Roberto Marquez
 */
public class JenkinsRssPoller 
{
    private final long delay;
    
    public JenkinsRssPoller()
    {
        delay = Duration.ofSeconds(30).toMillis();
    }
    public static void main (String [] args)
    {
        JenkinsRssPoller poller = new JenkinsRssPoller();
        
        poller.start();
    }
    
    public void start()
    {
        TimerTask pollerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                System.out.println("I go off every " + delay + " millieseconds");
            }
        };
        
        Timer timer = new Timer();
        
        Date firstTime = new Date();
        
        timer.schedule(pollerTask, firstTime, delay);
    }
}
