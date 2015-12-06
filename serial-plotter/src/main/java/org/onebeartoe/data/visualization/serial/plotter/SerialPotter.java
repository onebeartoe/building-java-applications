
package org.onebeartoe.data.visualization.serial.plotter;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.stage.WindowEvent;
import static javafx.application.Application.launch;
import org.onebeartoe.io.serial.SerialPorts;
import org.onebeartoe.system.Sleeper;

/**
 * This application uses JavaFX and to visualize data (float values) as it streams 
 * in from a serial input connection.
 * 
 * This was tested with the Adafruit EZ Link (adafruit.com/products/1628) using this command:
 * 
 * java -Djava.library.path="c:\opt\rxtx" -jar target/serial-plotter-0.0.1-SNAPSHOT.jar
 * 
 * @author Roberto Marquez
 */
public class SerialPotter extends Application implements SerialPortEventListener
{
    private Logger logger;
    
    private NumberAxis xAxis;
    
    private static final int MAX_DATA_POINTS = 50;
    
    private XYChart.Series internalTemperatureSeries;
    private ConcurrentLinkedQueue<Number> internalTemperatureMessageQueue = new ConcurrentLinkedQueue();
    
    private XYChart.Series internalHumiditySeries;
    private ConcurrentLinkedQueue<Number> internalHumidityMessageQueue = new ConcurrentLinkedQueue();
    
    private XYChart.Series externalTemperatureSeries;
    private ConcurrentLinkedQueue<Number> externalTemperatureMessageQueue = new ConcurrentLinkedQueue();
    
//    private ExecutorService executor;
    
//    private AddToQueue addToQueue;
    
    private int xSeriesData = 0;

//TODO: is this still needed?    
    private final long dataRefreshDelay = 2000;
    
    private long lastId = 0;
    
    private FXMLLoader loader;
    
    private Map<String, XYChart.Series> dataMap;
    
    private final String DATA_PROVIDER_KEY = "dataProvider";
    
private volatile List<String> messages;

    private BufferedReader input;
    
    private SerialPort serialPort;
    
    private void addDataToSeries()
    {
        if( !internalTemperatureMessageQueue.isEmpty() )
        {
            AreaChart.Data data = new AreaChart.Data(xSeriesData++, internalTemperatureMessageQueue.remove());
            internalTemperatureSeries.getData().add(data);
        }        
        // remove points to keep us at no more than MAX_DATA_POINTS
        if(internalTemperatureSeries.getData().size() > MAX_DATA_POINTS) 
        {
            internalTemperatureSeries.getData().remove(0, internalTemperatureSeries.getData().size() - MAX_DATA_POINTS);
        }
        
        if( !internalHumidityMessageQueue.isEmpty() )
        {
            AreaChart.Data data = new AreaChart.Data(xSeriesData-1, internalHumidityMessageQueue.remove() );
            internalHumiditySeries.getData().add(data);
        }
        if(internalHumiditySeries.getData().size() > MAX_DATA_POINTS)
        {
            internalHumiditySeries.getData().remove(0, internalHumiditySeries.getData().size() - MAX_DATA_POINTS);
        }
        
        if( !externalTemperatureMessageQueue.isEmpty() )
        {
            AreaChart.Data data = new AreaChart.Data(xSeriesData-1, externalTemperatureMessageQueue.remove() );
            externalTemperatureSeries.getData().add(data);
        }
        if(externalTemperatureSeries.getData().size() > MAX_DATA_POINTS)
        {
            int size = externalTemperatureSeries.getData().size() - MAX_DATA_POINTS;
            ObservableList data = externalTemperatureSeries.getData();
            data.remove(0, size);
        }
                
        // update 
        xAxis.setLowerBound(xSeriesData-MAX_DATA_POINTS);
        xAxis.setUpperBound(xSeriesData-1);
    }
    
    @Override
    /**
     * 
     */
    public void init()
    {
        logger = Logger.getLogger( getClass().getName() );
       
        dataMap = new HashMap();
        
        messages = new ArrayList();
        
        initializeSerialPort();
    }    
    
    private void initializeSerialPort()
    {
        try
        {
            System.out.println("obtaining serial port");
            serialPort = SerialPorts.get();                        
            System.out.println("serial port obtained");
            Sleeper.sleepo(2000);
            
            InputStream is = serialPort.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            input = new BufferedReader(isr);
            
            serialPort.addEventListener(this);
        }
        catch (Exception | NoClassDefFoundError ex)
        {
            logger.log(Level.SEVERE, null, ex);
        }
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
    
    /**
     * Timeline gets called in the JavaFX Main thread
     */ 
    private void prepareTimeline() 
    {        
        // Every frame to take any data from queue and add to chart
        new AnimationTimer() 
        {
            // is this the proper Thread to kill in primaryStage.setOnCloseRequest() ????
            @Override 
            public void handle(long now) 
            {
                addDataToSeries();
            }
        }.start();
    }    

    /**
     * Handle an event on the serial port. Read the data and print it.
     */
    @Override
    public synchronized void serialEvent(SerialPortEvent event) 
    {
        if(event.getEventType() == SerialPortEvent.DATA_AVAILABLE) 
        {
            try 
            {
                String inputLine = input.readLine();

                messages.add(inputLine);
 
                String [] split = inputLine.split(":");
                
                if( split.length >= 2)
                {
                    String dataName = split[0];

                    String s = split[1];                    
                    double d  = Double.valueOf(s);

                    switch(dataName)
                    {
                        case "S":
                        {
                            internalTemperatureMessageQueue.add(d);
                            System.out.println(inputLine);

                            break;
                        }
                        case "B":
                        {
                            externalTemperatureMessageQueue.add(d);
                            System.out.println(inputLine);

                            break;
                        }
                        case "Q":
                        {
                            internalHumidityMessageQueue.add(d);
                            System.out.println(inputLine);

                            break;
                        }
                    }

                    int size = messages.size();
                    if(size >= 50)
                    {
                        messages.remove(0);
                    }

                    System.out.println(inputLine);                    
                }
                else
                {
                    System.out.println("bad data: " + inputLine);
                }
            } 
            catch (Exception e) 
            {
                System.err.println(e.toString());
            }
        }
        else
        {
            // Ignore all the other eventTypes
        }        
    }

    @Override
    public void start(Stage stage) throws Exception 
    {
        URL url = getClass().getResource("/fxml/Scene.fxml");
                
        loader = new FXMLLoader(url);
                
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");

        logger = Logger.getLogger(getClass().getName());
        
        xAxis = new NumberAxis(0,MAX_DATA_POINTS,MAX_DATA_POINTS/10);
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(true);
        
        
        // Chart
        final AreaChart<Number, Number> sc = new AreaChart<Number, Number>(xAxis, yAxis) 
        {
            @Override 
            protected void dataItemAdded(XYChart.Series<Number, Number> series, int itemIndex, XYChart.Data<Number, Number> item) 
            {
                ; // This method is overriden with empty statement, to remove symbols on each data point.
            }
        };
        sc.setAnimated(false);
        sc.setId("liveAreaChart");
        sc.setTitle("Lizard Enclosure Sensor Readings");
        
        final AreaChart<Number, Number> sc2 = new AreaChart<Number, Number>(xAxis, yAxis) 
        {
            @Override 
            protected void dataItemAdded(XYChart.Series<Number, Number> series, int itemIndex, XYChart.Data<Number, Number> item) 
            {
                ; // This method is overriden with empty statement, to remove symbols on each data point.
            }
        };
        sc2.setAnimated(false);
        sc2.setId("sssliveAreaChart");
        sc2.setTitle("ssLizard Enclosure Sensor Readings");
        
        SerialPotterFxmlController controller = loader.getController();
        AnchorPane anchorPane = controller.getAnchorPane();
        ObservableList<Node> children = anchorPane.getChildren();
        
        children.add(sc);
//        children.add(sc2);

        // internal temperature Series
        internalTemperatureSeries = new AreaChart.Series<Number, Number>();
        internalTemperatureSeries.setName("Internal Temperature");
        sc.getData().add(internalTemperatureSeries);        
        
        externalTemperatureSeries = new AreaChart.Series<Number, Number>();
        externalTemperatureSeries.setName("External Temperature");
        sc.getData().add(externalTemperatureSeries);
        
        internalHumiditySeries = new AreaChart.Series<Number, Number>();
        internalHumiditySeries.setName("Internal Humidity");
        sc.getData().add(internalHumiditySeries);

        stage.setScene(scene);      
//        stage.setScene( new Scene(sc));
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() 
        {
            @Override
            public void handle(WindowEvent t) 
            {
                Platform.exit();
                System.exit(0);
            }
        });
       
        stage.setTitle("Serial Plotter");
        stage.setScene(scene);
        stage.show();
        
//        executor = Executors.newCachedThreadPool();
//        addToQueue = new AddToQueue();
//        executor.execute(addToQueue);
        
        prepareTimeline();        
    }
    
    @Override
    public void stop()
    {
        if (serialPort != null) 
        {
            serialPort.removeEventListener();
            serialPort.close();
        }        
    }

    private class AddToQueueeeee implements Runnable 
    {
        @Override
        public void run() 
        {
            try 
            {   
                ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                
                List<String> sensorIds = new ArrayList();
                sensorIds.add("S");
                sensorIds.add("B");
                sensorIds.add("Q");
                sensorIds.add("P");
                
                sensorIds.stream()
                        .forEach( s -> 
                        {
                            try
                            {
                                outstream.write(s.getBytes());                                                
                                outstream.write(':');                                                
                                float f = (new Random()).nextFloat();
                                outstream.write( String.valueOf(f).getBytes() );
                                outstream.write( System.lineSeparator().getBytes() );
                            }
                            catch (IOException ex)
                            {
                                logger.log(Level.SEVERE, null, ex);
                            }
                        });
                
                byte[] bytes = outstream.toByteArray();
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                
                InputStream instream = bais;
                InputStreamReader reader = new InputStreamReader(instream);
                BufferedReader in = new BufferedReader(reader);

                internalTemperatureMessageQueue.add(4);                

                String inputLine;
                
                while ( (inputLine = in.readLine()) != null)
                {
                    String [] split = inputLine.split(":");
                    
                    String dataName = split[0];
                    
                    String s = split[1];                    
                    double d  = Double.valueOf(s);

                    switch(dataName)
                    {
                        case "S":
                        {
                            internalTemperatureMessageQueue.add(d);
                            System.out.println(inputLine);
                            
                            break;
                        }
                        case "B":
                        {
                            externalTemperatureMessageQueue.add(d);
                            System.out.println(inputLine);
                            
                            break;
                        }
                        case "Q":
                        {
                            internalHumidityMessageQueue.add(d);
                            System.out.println(inputLine);
                            
                            break;
                        }
                    }
                }
                in.close();

                Thread.sleep(dataRefreshDelay);
                
  //              executor.execute(this);
            } 
            catch(Exception ex) 
            {
                logger.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }
}
