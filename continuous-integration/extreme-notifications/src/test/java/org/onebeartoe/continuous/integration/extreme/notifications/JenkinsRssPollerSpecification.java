
package org.onebeartoe.continuous.integration.extreme.notifications;

import java.io.IOException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
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
    }
    
    public void setRssUrl() throws Exception
    {
        String url = "some.host.tld/rss";
        
        implementation.setRssUrl(url);        
    }

    @Test(groups = {"unit"})
    public void start() throws Exception
    {
        String [] args = {};
//TODO: We can't test the implementation until there is a way to resolve the UnsatisfiedLinkError
//        implementation.start();
    }
    
//    @Test(groups = {"unit"}, expectedExceptions = IllegalArgumentException.class )
    public void serialEvent_fail_iniialize() throws IOException
    {
        implementation.initialize();
    }
}
