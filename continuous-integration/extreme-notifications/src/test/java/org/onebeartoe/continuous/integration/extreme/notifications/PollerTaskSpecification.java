
package org.onebeartoe.continuous.integration.extreme.notifications;

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
        
        Map<Integer, LedStatusIndicatorStrip> knownIndicatorStrips = new HashMap(0);
        pp.setKnownIndicatorStrips(knownIndicatorStrips);
        
        implementation = new PollerTask(pp);
    }
    
    @Test(expectedExceptions = NullPointerException.class)
    public void run()
    {
        implementation.run();        
    }
}
