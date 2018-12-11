
package org.onebeartoe.continuous.integration.extreme.notifications;

/**
 *
 * @author Roberto Marquez
 */
public class JenkinsRssRunProfile
{
    private String jobMappingPath;
    
    private String port;
    
    private String rssUrl;

    public String getJobMappingPath()
    {
        return jobMappingPath;
    }

    public void setJobMappingPath(String jobMappingPath)
    {
        this.jobMappingPath = jobMappingPath;
    }

    public String getPort()
    {
        return port;
    }

    public void setPort(String port)
    {
        this.port = port;
    }

    public String getRssUrl()
    {
        return rssUrl;
    }

    public void setRssUrl(String rssUrl)
    {
        this.rssUrl = rssUrl;
    }    
}
