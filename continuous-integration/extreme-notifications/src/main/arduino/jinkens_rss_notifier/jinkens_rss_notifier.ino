
char inData[64];
char inChar=-1;

void setup() 
{
   Serial.begin(9600);
   Serial.println("Waiting for Raspberry Pi to send a signal...\n");
}

void loop() 
{
  // put your main code here, to run repeatedly:
    byte numBytesAvailable= Serial.available();

    // if there is something to read
    if (numBytesAvailable > 0){
        // store everything into "inData"

// doh!
        Serial.readLine();
        
        int i;
        for (i=0;i<numBytesAvailable;i++){
            inChar= Serial.read();
            inData[i] = inChar;
        }

        inData[i] = '\0';


        Serial.print("Arduino Received: ");
        Serial.println(inData);
    }
}
