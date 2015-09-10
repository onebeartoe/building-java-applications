package org.onebeartoe.building.applications.drinking.bird;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        rotateTransition = new RotateTransition(Duration.millis(3000), birdBody);
        rotateTransition.setByAngle(90f);
        rotateTransition.setCycleCount(2);
        rotateTransition.setAutoReverse(true);
    }    
}
