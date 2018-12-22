
package org.onebeartoe.continuous.integration.extreme.notifications;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.onebeartoe.network.TextHttpHandler;

/**
 * @author Roberto Marquez
 */
public class ConfigHttpHandler extends TextHttpHandler
{
    private Logger logger;
    
    private Map<Integer, LedStatusIndicatorStrip> knownIndicatorStrips;
    
    private JenkinsRssPollerService pollerService;
    
    JenkinsRssRunProfile runProfile;
            
    public ConfigHttpHandler(Map<Integer, LedStatusIndicatorStrip> knownIndicatorStrips,
                             JenkinsRssPollerService pollerService,
                             JenkinsRssRunProfile runProfile)
    {
        logger = Logger.getLogger( getClass().getName() );
        
        this.knownIndicatorStrips = knownIndicatorStrips;
        
        this.pollerService = pollerService;
        
        this.runProfile = runProfile;
    }
    
    @Override
    protected String getHttpText(HttpExchange exchange) 
    {
        String message = "The job configuration re-loaded.";

        knownIndicatorStrips.clear();
        try 
        {
            pollerService.loadConfiguration(runProfile,
                                            knownIndicatorStrips);
        } 
        catch (IOException ex) 
        {
            message = "An error occured while reloading the job configuration.";
            logger.log(Level.SEVERE, message, ex);
        }

        return message;
    }
}
