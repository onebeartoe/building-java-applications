
package org.onebeartoe.continuous.integration.extream.notifications;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import org.onebeartoe.io.serial.SerialPorts;
import org.onebeartoe.system.Sleeper;

/**
 * @author Roberto Marquez
 */
public class JenkinsRssPoller implements SerialPortEventListener
{
    private final long delay;
    
    private BufferedReader input;
    
    private PrintWriter output;
    
    public JenkinsRssPoller() throws Exception
    {
        delay = Duration.ofSeconds(30).toMillis();
        
        initializeSerialPort();
    }
    
    private void initializeSerialPort() throws Exception
    {
        System.out.println("obtaining serial port");
        SerialPort serialPort = SerialPorts.get("COM7");
        System.out.println("serial port obtained");
        Sleeper.sleepo(2000);

        InputStream is = serialPort.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        input = new BufferedReader(isr);
        
        OutputStream outputStream = serialPort.getOutputStream();
        
        output = new PrintWriter(outputStream);

        serialPort.addEventListener(this);        
    }
    
    public static void main (String [] args) throws Exception
    {
        JenkinsRssPoller poller = new JenkinsRssPoller();
        
        poller.start();
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

                System.out.println("received: " + inputLine);
            } 
            catch (Exception e) 
            {
                System.err.println(e.toString());
            }
        }        
    }

// TODO: Remove the int once testing is done.    
    private int i = 0;
    public void start()
    {
        TimerTask pollerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                System.out.println("I go off every " + delay + " millieseconds");
                
                output.println(i);
                output.flush();
                
                i++;
            }
        };
        
        Timer timer = new Timer();
        
        Date firstTime = new Date();
        
        timer.schedule(pollerTask, firstTime, delay);
    }
}
