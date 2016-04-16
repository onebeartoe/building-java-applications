package org.onebeartoe.building.applications.drinking.bird;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class FXMLController implements Initializable
{    
    @FXML
    private Label label;
    
    private boolean lableToggle = true;
    
    @FXML
    private ImageView birdBody;
    
    private RotateTransition rotateTransition;
    
    @FXML
    private CheckBox checkbox;
    
    private TimerTask clickTask;
    
    private Timer timer;
    
    private Robot robot;
    
    @FXML
    private Button button;
    
    @FXML
    private void handleButtonAction(ActionEvent event) 
    {
        System.out.println("You clicked me!");
        
        String labelText;
        
        if(lableToggle)
        {
            labelText = "IT'S DRINKING THE WATER!";
        }
        else
        {
            labelText = "He's going back for more";
        }
        lableToggle = !lableToggle;
        
        label.setText(labelText);
        
        rotateTransition.play();
    }

    @FXML
    private void handleCheckBoxAction(ActionEvent event)
    {
        System.out.println("check box");
        
        if( checkbox.isSelected() )
        {
            // start the automatic thread
            Date firstTime = new Date();
            clickTask = new ClickTask();

            //TODO: Use the Duration class that was introduced in Java 8.
            final long period = 1000 * 20;
            timer.schedule(clickTask, firstTime, period);
        }
        else
        {
            // stop the automatic thread
            // note: you cancel the task and not the timer
            clickTask.cancel();
        }
    }    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        rotateTransition = new RotateTransition(Duration.millis(3000), birdBody);
        rotateTransition.setByAngle(90f);
        rotateTransition.setCycleCount(2);
        rotateTransition.setAutoReverse(true);

        try
        {
            robot = new Robot();
            robot.setAutoDelay(40);
            robot.setAutoWaitForIdle(true);
        }
        catch (AWTException ex)
        {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
        timer = new Timer();
    }
    
    public void stopThreads()
    {
        timer.cancel();
    }    

    private class ClickTask extends TimerTask
    {
        @Override
        public void run()
        {                
            if(robot == null)
            {
                System.out.println("robot is null");
            }
            else
            {
                // find the node location on the entire screen
                // resource: http://blog.crisp.se/2012/08/29/perlundholm/window-scene-and-node-coordinates-in-javafx

                final Scene scene = button.getScene();
                final Point2D windowCoord = new Point2D(scene.getWindow().getX(), scene.getWindow().getY());

                final Point2D sceneCoord = new Point2D(scene.getX(), scene.getY());

                final Point2D nodeCoord = button.localToScene(0.0, 0.0);

                final double buttonX = Math.round(windowCoord.getX() + sceneCoord.getX() + nodeCoord.getX());

                final double buttonY = Math.round(windowCoord.getY() + sceneCoord.getY() + nodeCoord.getY());

                double clickX = buttonX + 7;
                double clickY = buttonY + 7;

                robot.mouseMove((int)clickX, (int)clickY);
                robot.delay(200);

                robot.mousePress(InputEvent.BUTTON1_MASK);
                robot.delay(100);
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
            }
        }
    }
}
