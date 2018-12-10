
package org.onebeartoe.continuous.integration.extreme.notifications;

import org.testng.annotations.Test;

/**
 *
 * @author Roberto Marquez
 */
public class JenkinsRssPollerSpecification
{
    private JenkinsRssPoller implementation;
    
    @Test(groups = {"unit"}, expectedExceptions = UnsatisfiedLinkError.class)
    public void construtor_fail() throws Exception
    {
        String port = "/some/fake/port";
        
        implementation = new JenkinsRssPoller(port);
    }
}
