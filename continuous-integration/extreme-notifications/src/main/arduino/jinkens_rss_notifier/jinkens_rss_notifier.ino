
/**
 * The project uses an Arduino Uno.
 * 
 * To setup the board in the Arduino IDE use these configurations under the Tools menu:
 * 
Tools

 Board -> Arduino/Genuino Uno

  Mac OS X

    Port -> /dev/cu.usbmodem411

    Programmer -> Arduino ISP 
 * 
 * 
 */

char inData[64];
char inChar=-1;

void setup() 
{
   Serial.begin(9600);
   Serial.println("Hello Jenkins RSS notifier world!\n");
}

void loop() 
{
    byte numBytesAvailable= Serial.available();

    // if there is something to read
    if (numBytesAvailable > 0)
    {    
        String s = Serial.readString();
 
        Serial.print("Arduino Received: ");
        Serial.println(s);
    }
}
