
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

    private Map<Integer, LedStatusIndicatorStrip> knownIndicatorStrips = new HashMap();

    private JenkinsRssPollerService pollerService = new JenkinsRssPollerService();

    private HttpExchange exchange = null;
    
    @BeforeTest
    public void setup()
    {        
        JenkinsRssRunProfile runProfile = new JenkinsRssRunProfile();
        runProfile.setJobMappingPath( JenkinsRssPollerSpecification.jobMappingPath);
        
        implementation = new ConfigHttpHandler(knownIndicatorStrips,
                                                pollerService, 
                                                runProfile);
    }

    @Test
    public void getHttpText()
    {    
        String text = implementation.getHttpText(exchange);
        
        assert(text != null);
    }
    
    @Test
    public void getHttpText_fail()
    {
        JenkinsRssRunProfile runProfile = new JenkinsRssRunProfile();
        runProfile.setJobMappingPath("nonsense.path");
        
        ConfigHttpHandler implementation = new ConfigHttpHandler(knownIndicatorStrips,
                                                pollerService, 
                                                runProfile);
        
        String text = implementation.getHttpText(exchange);
        
        assert( text.contains("error") );
    }
}
