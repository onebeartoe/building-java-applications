
package org.onebeartoe.continuous.integration.extreme.notifications;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author Roberto Marquez
 */
public class PollerTaskSpecification
{
    private PollerTask implementation;
    
    @BeforeTest
    private void setup()
    {
        PollerProfile pp = new PollerProfile();
                     
        pp.setRssUrl(RssFeedSpecification.rssUrl);
        
        pp.setRssServie( new HttpJenkinsRssFeedService() );

        LedStatusIndicatorStrip s1 = new LedStatusIndicatorStrip();
        String name = "build-package-osx-llvm-release60";
        JenkinsJob job1 = new JenkinsJob();
        job1.setJobName(name);
        s1.jobs.add(job1);
        Map<Integer, LedStatusIndicatorStrip> knownIndicatorStrips = new HashMap();
        knownIndicatorStrips.put(0, s1);
        knownIndicatorStrips.put(1, s1);
        knownIndicatorStrips.put(2, s1);
        pp.setKnownIndicatorStrips(knownIndicatorStrips);
        
        
        pp.setOutput( new PrintWriter(System.out) );
        
        implementation = new PollerTask(pp);
    }
    
    @Test
    public void run()
    {
        implementation.run();        
    }
}
