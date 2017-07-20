
package org.onebeartoe.building.applications.drinking.bird;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.onebeartoe.application.ui.JavafxApplication;
import org.onebeartoe.application.ui.WindowProperties;

public class JavafxDrinkingBirdApp extends Application
{
    private FXMLLoader loader;
    
    private WindowProperties wp;
    
    private Logger logger;
    
    private JavafxApplication guiConiguration;
    
    private final String applicationId;
    
    public JavafxDrinkingBirdApp()
    {
        applicationId = getClass().getName();
    }
    
    @Override
    public void init()
    {
        guiConiguration = new JavafxApplication();        
    }
    
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        launch(args);
    }    
    
    private void restoreWindowProperties(Stage stage)
    {
        try
        {
            wp = new WindowProperties();
            wp.id = applicationId;
            wp = guiConiguration.loadWindowProperties(wp);
            guiConiguration.restoreWindowProperties(wp, stage);
        } 
        catch (IOException | ClassNotFoundException ex) 
        {
            Logger.getLogger(JavafxDrinkingBirdApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(wp == null)
        {
            // Prvoide default values if someting goes wrong with retoring the 
            // persisted values.
            wp = new WindowProperties();
            
            wp.id = getClass().getName();
            wp.applicationName = getClass().getSimpleName();
            
            // use the default values
            wp.width = 337;
            wp.height = 319;
            
            wp.locationX = 50;
            wp.locationY = 100;
            
            guiConiguration.restoreWindowProperties(wp, stage);
        }
    }
    
    @Override
    public void start(final Stage stage) throws Exception 
    {
        URL url = getClass().getResource("/fxml/Scene.fxml");
        
        loader = new FXMLLoader(url);
        
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("Drinking Bird Bot");
        stage.setScene(scene);
        
        restoreWindowProperties(stage);
        
        stage.setOnCloseRequest( new EventHandler<WindowEvent>() 
        {
            @Override
            public void handle(WindowEvent event) 
            {
                System.out.println("bye");
                
                wp = guiConiguration.currentConfiguration(stage);
                wp.id = applicationId;
                
                try
                {
                    guiConiguration.persistWindowProperties(wp);
                } 
                catch (IOException ex) 
                {
                    ex.printStackTrace();
                }
            }
        });
        
        stage.show();
    }

    @Override
    public void stop()
    {
        final FXMLController controller = loader.getController();
        controller.stopThreads();
    }    
}