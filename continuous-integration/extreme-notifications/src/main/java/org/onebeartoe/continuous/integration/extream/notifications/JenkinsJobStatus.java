
package org.onebeartoe.continuous.integration.extream.notifications;

import java.awt.Color;

/**
 * @author RobertoMarquez
 */
public enum JenkinsJobStatus 
{
//    todo: get this working correctly
    IN_PROGRESS(Color.YELLOW),
    
//    todo: account for this job status: '1 less test is failing (total 3)'

    DISABLED(Color.GRAY),
    FAILING(Color.RED),
    STABLE(Color.GREEN),
    UNKOWN(Color.PINK),
    UNSTABLE(Color.YELLOW);

    private JenkinsJobStatus(Color color) 
    {
        this.color = color;
    }
            
    private Color color;
    
    public Color getColor()
    {
        return color;
    }
}
