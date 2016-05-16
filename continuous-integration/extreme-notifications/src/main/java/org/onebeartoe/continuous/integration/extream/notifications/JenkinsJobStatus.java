
package org.onebeartoe.continuous.integration.extream.notifications;

import java.awt.Color;

/**
 *
 * @author URHM020
 */
public enum JenkinsJobStatus 
{    
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
