
package org.onebeartoe.continuous.integration.extreme.notifications;

import java.io.IOException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author Roberto Marquez
 */
public class JenkinsRssPollerSpecification
{
    private JenkinsRssPoller implementation;
    
    @BeforeTest
    public void setup() throws Exception
    {
        String port = "/some/fake/port";
        
        JenkinsRssRunProfile rp = new JenkinsRssRunProfile();
        rp.setPort(port);
        
        implementation = new JenkinsRssPoller(rp);
        
        String url = "https://jenkins.mono-project.com/rssLatest";
        
        implementation.setRssUrl(url);
        
        String path = "src/main/resources/strip-job.mapping";
        rp.setJobMappingPath(path);
    }

    @Test
    public void start() throws Exception
    {
        implementation.start();
    }
    
    @Test(expectedExceptions = UnsatisfiedLinkError.class)
    public void serialEvent_fail_iniialize() throws IOException
    {
        implementation.initialize();
    }
}
