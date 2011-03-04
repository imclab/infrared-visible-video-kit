import processing.core.*; 
import processing.xml.*; 

import processing.video.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class infrared_visible_video_kit extends PApplet {



Capture video;
Capture irvideo;

float[] bright;
char[] chars;

PFont font;
float fontSize = 1.5f;
PFont fontA;

public void setup() {
  size(640, 480, P2D);
  //size(320, 240, P2D);
  // Or run full screen, more fun! Use with Sketch -> Present
  //size(screen.width, screen.height, OPENGL);

  // Uses the default video input, see the reference if this causes an error
  video = new Capture(this, width, height, 35);
  video.settings();
  
  //instantiate capture source for right stereo image
  irvideo = new Capture(this, width, height, 30);        
  //select webcam from quicktime dialog
  irvideo.settings();

}

public void captureEvent(Capture c) {
  c.read();
}

int res = 1;

public void draw() {
  loadPixels();
  
  background(0);

  float sum = 0.000f;
  float pixcount = 0.000f;

  int index = 0;
  for (int y = 0; y < PApplet.parseInt (video.height); y+=res) {

    for (int x = 0; x < PApplet.parseInt (video.width); x+=res) {
        int pixelColor = video.pixels[index];
        int irpixelColor = irvideo.pixels[index];

        // Faster method of calculating r, g, b than red(), green(), blue() 
        int r = (pixelColor >> 16) & 0xff;
        int g = (pixelColor >> 8) & 0xff;
        int b = pixelColor & 0xff;

        int irr = (irpixelColor >> 16) & 0xff;
        int irg = (irpixelColor >> 8) & 0xff;
        int irb = irpixelColor & 0xff;

        float vis = (r+b+g/3.000f);
        float ir = (irr+irb+irg/3.000f);
        float ndvi = 255*((ir-vis)/(ir+vis));
        int pixcolor = parseInt(ndvi);

        // Render the difference image to the screen
        pixels[index] = color(pixcolor, pixcolor, pixcolor);
        // The following line does the same thing much faster, but is more technical
        //pixels[index] = 0xFF000000 | (pixcolor << 16) | (pixcolor << 8) | pixcolor;
        
        index++;
    }
  }
updatePixels();
//  fill(255,255,255);
//  text(r2,22,22);
//  text((sum/pixcount),22,122);
}

  static public void main(String args[]) {
    PApplet.main(new String[] { "--present", "--bgcolor=#666666", "--stop-color=#cccccc", "infrared_visible_video_kit" });
  }
}
