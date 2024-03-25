package kapitel01;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public abstract class LWJGLBasisFensterEigenesCanvas {
   public int WIDTH, HEIGHT;
   public String TITLE;

   public LWJGLBasisFensterEigenesCanvas() {
      this("BasisFenster", 800, 450);
   }

   public LWJGLBasisFensterEigenesCanvas(String title, int width, int height) {
      WIDTH = width;
      HEIGHT = height;
      TITLE = "vividus Wissenschaftsverlag. Warum sich der Dino furchtbar erschreckte. " + title;
   }

   public void initDisplay() {
      try {
         Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
         Display.setTitle(TITLE);
         Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
         Display.setLocation((d.width-WIDTH)/2, (d.height-HEIGHT)/2);
         Display.create();
      } catch (LWJGLException e) {
         e.printStackTrace();
      }
   }

   public abstract void renderLoop();

   public void start() {
      initDisplay();
      renderLoop();
      Display.destroy();
      System.exit(0);
   }
}
