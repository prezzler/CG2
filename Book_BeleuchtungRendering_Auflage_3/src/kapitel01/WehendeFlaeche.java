package kapitel01;

import java.awt.Canvas;

import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class WehendeFlaeche extends LWJGLBasisFenster {
   public WehendeFlaeche(String title, int width, int height) {
      super(title, width, height);
      JFrame f = new JFrame();
      f.setTitle(title);
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      Canvas c = new Canvas();
      f.add(c);
      f.setBounds(100, 100, 600, 600);
      f.setLocationRelativeTo(null);
      f.setVisible(true);
      initDisplay(c);
   }

   @Override
   public void renderLoop() {
      long startTime = System.nanoTime();
      while (!Display.isCloseRequested()) {
         POGL.clearBackgroundWithColor(0.95f, 0.95f, 0.95f, 1.0f);
         double t = (System.nanoTime()-startTime)/1e9;
         POGL.renderWehendeFlaeche(t);
         Display.update();
      }
   }

   public static void main(String[] args) throws LWJGLException {
      new WehendeFlaeche("vividus Verlag. Dino-Buch. Kap. 1: WehendeFlaeche.java", 500, 500).start();
   }
}
