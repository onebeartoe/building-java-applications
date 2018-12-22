
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
    
    public static final String jobMappingPath = "src/main/resources/strip-job.mapping";
    
    @BeforeTest
    public void setup() throws Exception
    {
        String port = "/some/fake/port";
        
        String url = "https://jenkins.mono-project.com/rssLatest";
        
        String [] args = {
                            "--port", port,
                            "--rssUrl", url,
                            "--jobMappingPath", jobMappingPath
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
