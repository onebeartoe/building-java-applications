
#include <Adafruit_NeoPixel.h>

#define PIN 6

#define NEOPIXEL_COUNT 4

// Parameter 1 = number of pixels in strip
// Parameter 2 = Arduino pin number (most are valid)
// Parameter 3 = pixel type flags, add together as needed:
//   NEO_KHZ800  800 KHz bitstream (most NeoPixel products w/WS2812 LEDs)
//   NEO_KHZ400  400 KHz (classic 'v1' (not v2) FLORA pixels, WS2811 drivers)
//   NEO_GRB     Pixels are wired for GRB bitstream (most NeoPixel products)
//   NEO_RGB     Pixels are wired for RGB bitstream (v1 FLORA pixels, not v2)
Adafruit_NeoPixel strip = Adafruit_NeoPixel(NEOPIXEL_COUNT, PIN, NEO_GRB + NEO_KHZ800);

// IMPORTANT: To reduce NeoPixel burnout risk, add 1000 uF capacitor across
// pixel power leads, add 300 - 500 Ohm resistor on first pixel's data input
// and minimize distance between Arduino and first pixel.  Avoid connecting
// on a live circuit...if you must, connect GND first.

char inData[64];
char inChar=-1;

void setup() 
{
   Serial.begin(9600);
   Serial.println("Hello Jenkins RSS notifier world!\n");

  strip.begin();

  for(int p=0; p<NEOPIXEL_COUNT; p++)
  {
      // turn off all neopixels
      strip.setPixelColor(p, 0);   
  }
  
  strip.show(); // Initialize all pixels to 'off'
}

void loop() 
{
    byte numBytesAvailable= Serial.available();

    // if there is something to read
    if (numBytesAvailable > 0)
    {    
        String s = Serial.readString();

        Serial.println();
        Serial.print("Arduino Received: ");
        Serial.println(s);

        const long i = s.toInt();
        Serial.print("int: ");
        Serial.println(i);

        if(i > 0)
        {
            uint32_t color = strip.Color(0, 155, 0);
            strip.setPixelColor(i, color);
            strip.show();

            Serial.println("color set");
        }
    }
}
