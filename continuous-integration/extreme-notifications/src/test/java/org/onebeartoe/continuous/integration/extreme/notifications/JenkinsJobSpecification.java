
package org.onebeartoe.continuous.integration.extreme.notifications;

import org.testng.annotations.Test;

/**
 * @author Roberto Marquez
 */
public class JenkinsJobSpecification
{
    private JenkinsJob implementation;
  
    public JenkinsJobSpecification()
    {
        implementation = new JenkinsJob();
    }

    @Test
    public void fromRssTitle_unknown()
    {
        String rssTitle = "none#after(some stuff)other";
        String uri = "some.host.tld/path/rss";
        
        JenkinsJob jj = JenkinsJob.fromRssTitle(rssTitle, uri);
        
        assert(jj.getJobStatus() == JenkinsJobStatus.UNKOWN);
    }
    
    @Test
    public void fromRssTitle_unstable()
    {
        String rssTitle = "none#after(4 test failures)other";
        String uri = "some.host.tld/path/rss";
        
        JenkinsJob jj = JenkinsJob.fromRssTitle(rssTitle, uri);
        
        assert(jj.getJobStatus() == JenkinsJobStatus.UNSTABLE);
    }
    
    @Test(expectedExceptions = StringIndexOutOfBoundsException.class)
    public void fromRssTitle_fail_badTitle()
    {
        String rssTitle = "bad-title";
        String uri = "http://some.url/";
        
        JenkinsJob jenkinsJob = JenkinsJob.fromRssTitle(rssTitle, uri);
    }
    
    @Test
    public void setJobStatus()
    {
        implementation.setJobStatus("none");
    }

    

    @Test
    public void getJobStatus()
    {
        JenkinsJobStatus jobStatus = implementation.getJobStatus();
        
        assert(jobStatus != JenkinsJobStatus.UNKOWN);
    }
    
    @Test
    public void getNeopixelIndex()
    {
        int i = implementation.getNeopixelIndex();
        
        assert(i >= 0);
    }
    
    @Test
    public void jobStatusDescription()
    {
        implementation.setJobStatusDescription("some description");

        String description = implementation.getJobStatusDescription();
        
        assert(description != null);
    }

    @Test
    public void toString_pass()
    {
        String s = implementation.toString();
        
        assert(s != null);
    }
}
