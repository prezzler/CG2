package kapitel01;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class FensterErzeugen { 
   public FensterErzeugen(int width, int heigth) {
      try {
         Display.setDisplayMode(new DisplayMode(width, heigth));
         Display.setTitle("Erblicke die Welt!");
         Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
         Display.setLocation((d.width-width)/2, (d.height-heigth)/2);
         Display.create();
      } catch (LWJGLException e) {
         e.printStackTrace();
      }  
      
      renderLoop();
      
      Display.destroy();
      System.exit(0);
   }
   
   private void renderLoop() {
      while (!Display.isCloseRequested()) {
         Display.update();
         Display.sync(60);
      }
   }
   
   public static void main(String[] args) {
      new FensterErzeugen(800, 450);
   }
}
