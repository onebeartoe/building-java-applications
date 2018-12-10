
package org.onebeartoe.continuous.integration.extreme.notifications;

import org.onebeartoe.continuous.integration.extream.notifications.JenkinsJob;
import org.testng.annotations.Test;

/**
 * @author Roberto Marquez
 */
public class JenkinsJobSpecification
{
    private JenkinsJob implementation;
    
    @Test(groups = {"unit"})
    public void toString_pass()
    {
        String s = implementation.toString();
        
        
    }
}
