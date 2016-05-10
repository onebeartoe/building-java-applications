
package org.onebeartoe.continuous.integration.extream.notifications;

import java.awt.Color;



/**
 *
 * @author URHM020
 */
public enum JenkinsJobStatus 
{    
    STABLE(Color.BLUE),
    UNKOWN(Color.PINK);

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
