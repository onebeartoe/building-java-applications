
package org.onebeartoe.continuous.integration.extreme.notifications;

import java.io.PrintWriter;
import java.util.Map;

/**
 * @author Roberto Marquez
 */
public class PollerProfile
{
    private Map<Integer, LedStatusIndicatorStrip> knownIndicatorStrips;
    
    private long POLL_DELAY;
    
    private PrintWriter output;

    private String rssUrl;
    
    private JenkinsRssFeedService rssService;
    
    public Map<Integer, LedStatusIndicatorStrip> getKnownIndicatorStrips()
    {
        return knownIndicatorStrips;
    }

    public void setKnownIndicatorStrips(Map<Integer, LedStatusIndicatorStrip> knownIndicatorStrips)
    {
        this.knownIndicatorStrips = knownIndicatorStrips;
    }

    public void setPollDelay(long POLL_DELAY)
    {
        this.POLL_DELAY = POLL_DELAY;
    }

    public long getPollDelay()
    {
        return POLL_DELAY;
    }

    void setOutput(PrintWriter output)
    {
        this.output = output;
    }

    public PrintWriter getOutput()
    {
        return output;
    }

    public void setRssUrl(String rssUrl)
    {
        this.rssUrl = rssUrl;
    }

    public String getRssUrl()
    {
        return rssUrl;
    }

    public void setRssServie(JenkinsRssFeedService rssService)
    {
        this.rssService = rssService;
    }

    public JenkinsRssFeedService getRssService()
    {
        return rssService;
    }
    
    
}
