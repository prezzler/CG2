package kapitel01;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPointSize;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class PunkteErzeugen extends LWJGLBasisFensterEigenesCanvas {
   public PunkteErzeugen(String title, int width, int height) {
      super(title, width, height);
   }

   @Override
   public void renderLoop() {
      glPointSize(5);
      while (!Display.isCloseRequested()) {
         glClearColor(0.95f, 0.95f, 0.95f, 1.0f);           
         glClear(GL_COLOR_BUFFER_BIT);
         for (float p=0.0f, r=0.0f; p<=Math.PI*6.0f; p+=0.3f, r+=0.014f) {
            glBegin(GL_POINTS);
               glColor3d(0.35 + 0.30*Math.sin(p), 0.63 + 0.24*Math.sin(p), 0.73 + 0.22*Math.sin(p));
               glVertex3f(r*(float)Math.sin(p), r*(float)Math.cos(p), 0.0f);
            glEnd();
         }

         Display.update();
      }
   }

   public static void main(String[] args) throws LWJGLException, IOException {
      new PunkteErzeugen("Kapitel 1: PunkteErzeugen.java", 800, 450).start();
   }
}
