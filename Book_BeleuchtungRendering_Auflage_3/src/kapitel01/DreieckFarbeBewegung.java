package kapitel01;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3d;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class DreieckFarbeBewegung extends LWJGLBasisFensterEigenesCanvas {
   public DreieckFarbeBewegung(String title, int width, int height) {
      super(title, width, height);
   }

   private void clearBackground(float r, float g, float b, float a) {
      glClearColor(r, g, b, a);
      glClear(GL_COLOR_BUFFER_BIT);
   }

   @Override
   public void renderLoop() {
      while (!Display.isCloseRequested()) {
         double t = System.nanoTime() / 1e9;
         clearBackground(0.95f, 0.95f, 0.95f, 1.0f);
         glColor3d(0.35 + 0.30*Math.sin(t), 0.63 + 0.24*Math.sin(t), 0.73 + 0.22*Math.sin(t));

         glBegin(GL_TRIANGLES);
         glVertex3d(-0.5, -0.5 + 0.3*Math.sin(t), 0);
         glVertex3d( 0.5,  0.3*Math.sin(t*1.3), 0);
         glVertex3d( 0.3*Math.sin(t*2), 0.5, 0);
         glEnd();

         Display.update();
      }
   }

   public static void main(String[] args) throws LWJGLException, IOException {
      new DreieckFarbeBewegung("Kapitel 1: DreieckFarbeBewegung.java", 800, 450).start();
   }
}
