
package org.onebeartoe.continuous.integration.extreme.notifications;

import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author Roberto Marquez
 */
public class ConfigHttpHandlerSpecification
{
    private ConfigHttpHandler implementation;
    
    @BeforeTest
    public void setup()
    {
        Map<Integer, LedStatusIndicatorStrip> knownIndicatorStrips = new HashMap();

        JenkinsRssPollerService pollerService = new JenkinsRssPollerService();
        
        JenkinsRssRunProfile runProfile = new JenkinsRssRunProfile();
        runProfile.setJobMappingPath( JenkinsRssPollerSpecification.jobMappingPath);
        
        implementation = new ConfigHttpHandler(knownIndicatorStrips, 
                                                pollerService, 
                                                runProfile);
    }

    @Test
    public void getHttpText()
    {
        HttpExchange exchange = null;
        
        String text = implementation.getHttpText(exchange);
        
        assert(text != null);
    }
}
