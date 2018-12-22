
package org.onebeartoe.continuous.integration.extreme.notifications;

import java.awt.Color;
import org.testng.annotations.Test;

/**
 *
 * @author Roberto Marquez
 */
public class JenkinsJobStatusSpecification
{
    private JenkinsJobStatus implementation;
    
    public JenkinsJobStatusSpecification()
    {
        implementation = JenkinsJobStatus.STABLE;
    }
    
    @Test(groups = {"unit"})
    public void getColor()
    {
        Color actual = implementation.getColor();

        assert( Color.green.getRGB() == actual.getRGB() );
    }
}
