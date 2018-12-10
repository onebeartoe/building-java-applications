
package org.onebeartoe.continuous.integration.extreme.notifications;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
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
    
    @Test(groups = {"unit"}, expectedExceptions = IllegalArgumentException.class )
    public void serialEvent_fail_badSource()
    {
        SerialPort sp = null;
        
        SerialPortEvent event = new SerialPortEvent(sp, 0, false, false);
        
        implementation.serialEvent(event);
    }
}
