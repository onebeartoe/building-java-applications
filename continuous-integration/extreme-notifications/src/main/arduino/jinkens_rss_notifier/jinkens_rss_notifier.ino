
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
