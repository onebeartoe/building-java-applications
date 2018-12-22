
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
        
        String url = "https://jenkins.mono-project.com/rssLatest";

        String path = "src/main/resources/strip-job.mapping";
        
        String [] args = {
                            "--port", port,
                            "--rssUrl", url,
                            "--jobMappingPath", path
                         };
                
        implementation = new JenkinsRssPoller(args);
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
