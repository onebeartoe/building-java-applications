
package org.onebeartoe.continuous.integration.extream.notifications;

import java.awt.Color;

/**
 * @author RobertoMarquez
 */
public enum JenkinsJobStatus 
{
    IN_PROGRESS(Color.YELLOW),
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
