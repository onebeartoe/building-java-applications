
package org.onebeartoe.building.applications.drinking.bird;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.RotateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.util.converter.IntegerStringConverter;

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
    
    @FXML
    private TextField textField;
    
    @FXML
    private ChoiceBox<String> durationChoiceBox;
    
    private TimerTask clickTask;
    
    private TimerTask cancelTask;
    
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
            scheduleTask();
        }
        else
        {
            // stop the automatic thread
            // note: you cancel the task and not the timer
            clickTask.cancel();
        }
    }
    
    @FXML
    private void onDurationChoiceChanged()
    {
        System.out.println("love!");
        
        scheduleTask();
    }
    @FXML
    private void onDurationValueChanged(KeyEvent ke)
    {
        System.out.println("key event love");
        
        scheduleTask();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        List l = new ArrayList();
        
        ObservableList durationList = FXCollections.observableArrayList();
        durationList.addAll("Only Once", "Seconds", "Minutes", "Hours");
        durationChoiceBox.setItems(durationList);
        durationChoiceBox.getSelectionModel()
                .selectFirst();
        durationChoiceBox.getSelectionModel()
                .selectNext();
        durationChoiceBox.valueProperty().addListener( new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
            {
                onDurationChoiceChanged();
            }
        });
        
        final TextFormatter<Integer> formatter = new TextFormatter<>(new IntegerStringConverter());
        textField.setTextFormatter(formatter);

        rotateTransition = new RotateTransition(javafx.util.Duration.millis(3000), birdBody);
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
        
        clickTask = new ClickTask();
        cancelTask = new CancelTask();
    }
    
    private void scheduleTask()
    {
        clickTask.cancel();
        cancelTask.cancel();
        
        // Get the duration value from the screen        
        String s = textField.getText();
        
        Integer value;
        
        if(s.trim().equals(""))
        {
            value = 0;
        }
        else
        {
            value = Integer.valueOf( s );
        }
        
        
        String durationTypeLabel = durationChoiceBox.getSelectionModel()
                .getSelectedItem();
        
        Duration cancelWait;
        
        switch(durationTypeLabel)
        {
            case "Seconds":
            {
                cancelWait = Duration.ofSeconds(value);
                
                break;
            }
            case "Minutes":
            {
                cancelWait = Duration.ofMinutes(value);
                
                break;
            }
            case "Hours":
            {
                cancelWait = Duration.ofHours(value);
                
                break;
            }
            default:
            {
                System.out.println("An unknown duration type was encountered: " + durationTypeLabel);
                cancelWait = null;
            }
        }
        
        // start the thread that clicks the button
        Date firstTime = new Date();
        clickTask = new ClickTask();

        if(cancelWait == null)
        {
            // "Just Once" is selected, and click task will not repeat.
            timer.schedule(clickTask, firstTime);
        }
        else
        {
            // The clicking will repeat...
            final long repeatDelay = Duration.ofSeconds(20).toMillis();
            timer.schedule(clickTask, firstTime, repeatDelay);

            // ...for the specified amount of time on the screen.
            cancelTask = new CancelTask();
            long cancelDelay = cancelWait.toMillis();
            timer.schedule(cancelTask, cancelDelay);
        }
    }
    
    public void stopThreads()
    {
        timer.cancel();
    }    

    private class CancelTask extends TimerTask
    {
        @Override
        public void run()
        {
            clickTask.cancel();
            
            System.out.println("The click task was canceled.");

//TODO:            
//            anyting else?
        }
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
    
//    private class DurationOption
//    {
//        Duration duration;       
//    }
}