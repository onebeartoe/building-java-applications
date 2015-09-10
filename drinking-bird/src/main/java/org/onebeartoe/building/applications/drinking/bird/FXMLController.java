package org.onebeartoe.building.applications.drinking.bird;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class FXMLController implements Initializable 
{    
    @FXML
    private Label label;
    
    private boolean lableToggle = true;
    
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
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
