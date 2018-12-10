
package org.onebeartoe.continuous.integration.extreme.notifications;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Roberto Marquez
 */
public class JenkinsJobSpecification
{
    private JenkinsJob implementation;
  
    public JenkinsJobSpecification()
//    @BeforeClass
//    public void initialize()
    {
        implementation = new JenkinsJob();
    }
    
    @Test(groups = {"unit"}, expectedExceptions = StringIndexOutOfBoundsException.class)
    public void fromRssTitle_fail_badTitle()
    {
        String rssTitle = "bad-title";
        String uri = "http://some.url/";
        
        JenkinsJob jenkinsJob = JenkinsJob.fromRssTitle(rssTitle, uri);
    }
    
    @Test(groups = {"unit"})
    public void toString_pass()
    {
        String s = implementation.toString();
        
        assert(s != null);
    }
}
