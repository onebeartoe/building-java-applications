
package org.onebeartoe.continuous.integration.extreme.notifications;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import org.onebeartoe.io.serial.SerialPorts;
import org.onebeartoe.system.Sleeper;

/**
 * @author Roberto Marquez
 */
public class ArduinoListener implements SerialPortEventListener
{
    private PrintWriter output;
    
    private BufferedReader input;
    
    public ArduinoListener(String port) throws Exception
    {
        System.out.println("obtaining serial port");
        SerialPort serialPort = SerialPorts.get(port);
        
        System.out.println();
        System.out.println("serial port obtained");
        Sleeper.sleepo(2000);

        InputStream is = serialPort.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        input = new BufferedReader(isr);
        
        OutputStream outputStream = serialPort.getOutputStream();
        
        output = new PrintWriter(outputStream);

        serialPort.addEventListener(this);        
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
                
                if(inputLine.trim().equals(""))
                {
                    System.out.println();
                }
                else
                {
                    System.out.println("\t\t\t\t\t" + "received from Arduino: " + inputLine);
                }
            } 
            catch (Exception e) 
            {
                System.err.println(e.toString());
            }
        }        
    }    

    public BufferedReader getInputStream()
    {
        return input;
    }

    public PrintWriter getOutputStream()
    {
        return output;
    }
}
