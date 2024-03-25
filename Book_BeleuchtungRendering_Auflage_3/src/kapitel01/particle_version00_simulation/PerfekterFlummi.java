package kapitel01.particle_version00_simulation;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.opengl.Display;

import kapitel01.LWJGLBasisFenster;
import kapitel01.POGL;

public class PerfekterFlummi extends LWJGLBasisFenster {
   private float xPos, yPos;
   private float ySpeed;

   public PerfekterFlummi(String title, int width, int height) {
      super(title, width, height);
      initDisplay();
      xPos = width/2.0f;
      yPos = 0;
      ySpeed = 0.2f;
   }

   @Override
   public void renderLoop() {
      glEnable(GL_DEPTH_TEST);
      while (!Display.isCloseRequested()) {
         POGL.clearBackgroundWithColor(0.95f, 0.95f, 0.95f, 1.0f);

         glLoadIdentity();
         glOrtho(0, WIDTH, HEIGHT, 0, 0, 1);
         glDisable(GL_DEPTH_TEST);
         
         glColor3d(0.05, 0.39, 0.51);
         glBegin(GL_TRIANGLE_FAN);
         glVertex2f(xPos, yPos);
         for (int angle=0; angle<360; angle+=5)
            glVertex2f(xPos + (float)Math.sin(angle)*20.0f, yPos + (float)Math.cos(angle)*20.0f);
         glEnd();

         // Position in Abhängigkeit zu Beschleunigung
         yPos = yPos + ySpeed;

         // an den Rändern Richtung umkehren
         if (yPos > HEIGHT || yPos < 0)
            ySpeed = ySpeed * -1;

         Display.update();
      }
      Display.destroy();
   }

   public static void main(String[] args) {
      new PerfekterFlummi("vividus Verlag. Dino-Buch. Kap. 1 (particle_version00_simulation): PerfekterFlummi.java", 800, 450).start();
   }
}
