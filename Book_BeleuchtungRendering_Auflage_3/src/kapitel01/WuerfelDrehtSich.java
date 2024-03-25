package kapitel01;

import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glFrustum;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTranslated;

import java.awt.Canvas;

import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class WuerfelDrehtSich extends LWJGLBasisFenster {
   public WuerfelDrehtSich(String title, int width, int height) {
      super(title, width, height);
      JFrame f = new JFrame();
      f.setTitle(title);
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      Canvas c = new Canvas();
      f.add(c);
      f.setBounds(0, 0, 500, 500);
      f.setLocationRelativeTo(null);
      f.setVisible(true);
      initDisplay(c);
   }

   @Override
   public void renderLoop() {
      glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
      long start = System.nanoTime();
      while (!Display.isCloseRequested()) {
         double t = (System.nanoTime() - start)/1e9;
         POGL.clearBackgroundWithColor(0.95f, 0.95f, 0.95f, 1.0f);

         glLoadIdentity();
         glFrustum(-1, 1, -1, 1, 4, 10);
         glTranslated(0, 0, -5);
         glRotatef((float)t*100.0f, 1.0f, 0.0f, 0.0f);
         glRotatef((float)t*100.0f, 0.0f, 1.0f, 0.0f);
         glScaled(0.5, 0.5, 0.5);
         glLineWidth(2.0f);
         glColor3d(0.1, 0.2, 0.3);
         POGL.renderWuerfel();

         Display.update();
      }
   }

   public static void main(String[] args) throws LWJGLException {
      new WuerfelDrehtSich("vividus Verlag. Dino-Buch. Kap. 1: WuerfelDrehtSich.java", 500, 500).start();
   }
}
