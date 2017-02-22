
/**
 * 
 * This sketch is intended for the Arduino Uno.
 *
 */

#include <Adafruit_NeoPixel.h>

// these define the Arduino digial pins the connect to the Neopixel stips data line.
#define PIN1 6
#define PIN2 8

#define NEOPIXEL_COUNT 30

// Parameter 1 = number of pixels in strip
// Parameter 2 = Arduino pin number (most are valid)
// Parameter 3 = pixel type flags, add together as needed:
//   NEO_KHZ800  800 KHz bitstream (most NeoPixel products w/WS2812 LEDs)
//   NEO_KHZ400  400 KHz (classic 'v1' (not v2) FLORA pixels, WS2811 drivers)
//   NEO_GRB     Pixels are wired for GRB bitstream (most NeoPixel products)
//   NEO_RGB     Pixels are wired for RGB bitstream (v1 FLORA pixels, not v2)
Adafruit_NeoPixel strip0 = Adafruit_NeoPixel(NEOPIXEL_COUNT, PIN1, NEO_GRB + NEO_KHZ800);

Adafruit_NeoPixel strip1 = Adafruit_NeoPixel(NEOPIXEL_COUNT, PIN2, NEO_GRB + NEO_KHZ800);

int stripCount = 2;
Adafruit_NeoPixel strips[] = {strip0, strip1};

int MAX_BRIGHTNESS = 254;

/**
 * Holds the status of whether the LED is in pulsing mode or not.
 * The initial status of all LEDs is not pulsing.
 */
int pulseStatuses [3] [NEOPIXEL_COUNT] = {0}; 

/**
 * Valid brighness values are 0 - 255.
 */
int stripBrightness [3] [NEOPIXEL_COUNT] = {MAX_BRIGHTNESS};  // set initial bright

// IMPORTANT: To reduce NeoPixel burnout risk, add 1000 uF capacitor across
// pixel power leads, add 300 - 500 Ohm resistor on first pixel's data input
// and minimize distance between Arduino and first pixel.  Avoid connecting
// on a live circuit...if you must, connect GND first.

/**
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

  strip0.begin();
  strip1.begin();

  for(int p=0; p<NEOPIXEL_COUNT; p++)
  {
      // turn off all neopixels
      strip0.setPixelColor(p, 0);
      strip1.setPixelColor(p, 0);
  }
  
  // Initialize all pixels to 'off'
  strip0.show(); 
  strip1.show();
}

/**
 * this is the loops over and over.
 * 
 * If there is data available on the Serial buffer, then it parses the line as follows:
 * 
 * sample input:
 *           1 
 * 01234567890123456789
 * 002:000:155:000    
 * 
 * 0-2 -> neopixel index on the stip
 *     4-6 -> red component (0-255)
 *         8-10 -> green component (0-225)  
 *             12-14 -> blue component (0-225)
 */
void loop() 
{
    byte numBytesAvailable= Serial.available();

    if (numBytesAvailable > 0)
    {
        // there is something to read from the serial connection
        String s = Serial.readString();

        Serial.println();
        Serial.print("Arduino Received: ");
        Serial.println(s);

        const long stripIndex = s.toInt();
        Serial.print("strip index: ");
        Serial.println(stripIndex);

        if(stripIndex >= 0)
        {
            // the strip index is valid

            int ledIndex = s.substring(4,7).toInt();
            Serial.print("LED index: ");
            Serial.println(ledIndex);
        
            int r = s.substring(8, 11).toInt();
            int g = s.substring(12, 15).toInt();
            int b = s.substring(16, 19).toInt();
            
            int pulsing = (int) s.charAt(20) - 48;  // subtract 48 to map from the ASCII value
            Serial.print("pulsing is: ");
            Serial.println(pulsing);
            pulseStatuses[stripIndex][ledIndex] = pulsing;
            
            uint32_t color = strips[stripIndex].Color(r, g, b);
            
            strips[stripIndex].setPixelColor(ledIndex, color);
            strips[stripIndex].show();

            Serial.print("color set: ");
            Serial.print(r);
            Serial.print(", ");
            Serial.print(g);
            Serial.print(", ");
            Serial.println(b);
        }
    }
    
    updatePulsingLeds();
}

int nextPulseValue(int stripIndex, int ledIndex)
{
    stripBrightness[stripIndex][ledIndex]++;
    
    if(stripBrightness[stripIndex][ledIndex] > MAX_BRIGHTNESS)
    {
        stripBrightness[stripIndex][ledIndex] = 0;
    }
    
    return stripBrightness[stripIndex][ledIndex];
}

void updatePulsingLeds()
{
    for(int s=0; s<stripCount; s++)
    {
        for(int l=0; l<NEOPIXEL_COUNT; l++)
        {
            if(pulseStatuses[s][l] == 1)  // the Java application sends '1' to indicate the job is in progress
            {                  
//TODO: get the old value with strip.getColor() and do the maths to get the individual R,G,B values
                int g = nextPulseValue(s, l);
                
                strips[s].setPixelColor(l, g);
                strips[s].show();
                        
//                Serial.print("gonna pulse:");
//                Serial.println(g);
            }            
        }
    }
    
    
}