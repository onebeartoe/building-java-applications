
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
    
    @Test(groups = {"unit"})
    public void toString_pass()
    {
        String s = implementation.toString();
        
        assert(s != null);
    }
}
