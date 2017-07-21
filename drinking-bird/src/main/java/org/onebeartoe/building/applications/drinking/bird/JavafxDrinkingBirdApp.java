
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
        loadDefaultGuiConfig();
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
    
    private void loadDefaultGuiConfig()
    {

        guiConiguration = new JavafxApplication(applicationId)
        {
            @Override
            public int defaultX() 
            {
                return 50;
            }

            @Override
            public int defaultY() 
            {
                return 100;
            }

            @Override
            public int defaultWidth() 
            {
                return 337;
            }

            @Override
            public int defaultHeight() 
            {
                return 319;
            }
        };                
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
        
        guiConiguration.restoreWindowProperties(stage);
        
        stage.setOnCloseRequest( new EventHandler<WindowEvent>() 
        {
            @Override
            public void handle(WindowEvent event) 
            {
                System.out.println("bye");

                guiConiguration.currentConfiguration(stage);
                guiConiguration.setApplicationId(applicationId);
                
                try
                {
                    guiConiguration.persistWindowProperties();
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