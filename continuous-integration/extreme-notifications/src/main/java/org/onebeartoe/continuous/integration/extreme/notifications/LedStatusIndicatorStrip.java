
package org.onebeartoe.continuous.integration.extreme.notifications;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Roberto Marquez
 */
//@Deprecated
public class LedStatusIndicatorStrip 
{
    public String rssUrl;

    @Deprecated
    public int stripIndex;

    public List<JenkinsJob> jobs;
    
    public LedStatusIndicatorStrip()
    {
        jobs = new ArrayList();
    }
}
