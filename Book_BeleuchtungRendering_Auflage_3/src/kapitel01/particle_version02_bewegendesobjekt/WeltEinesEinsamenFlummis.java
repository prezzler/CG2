package kapitel01.particle_version02_bewegendesobjekt;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glOrtho;

import org.lwjgl.opengl.Display;

import kapitel01.LWJGLBasisFenster;
import kapitel01.POGL;

public class WeltEinesEinsamenFlummis extends LWJGLBasisFenster {
   private Flummi flummi;

   public WeltEinesEinsamenFlummis(String title, int width, int height) {
      super(title, width, height);
      initDisplay();
      flummi = new Flummi(width/2, height/2, 0.2f);
   }

   @Override
   public void renderLoop() {
      glEnable(GL_DEPTH_TEST);

      while (!Display.isCloseRequested()) {
         POGL.clearBackgroundWithColor(0.1f, 0.2f, 0.3f, 1.0f);

         glLoadIdentity();
         glOrtho(0, WIDTH, HEIGHT, 0, 0, 1);
         glDisable(GL_DEPTH_TEST);

         // einen Flummi anzeigen
         flummi.render();

         // Physik updaten
         flummi.update();

         Display.update();
      }
      Display.destroy();
   }

   public static void main(String[] args) {
      new WeltEinesEinsamenFlummis(
            "vividus Verlag. Dino-Buch. Kap. 1 (particle_version02_bewegendesobjekt): WeltEinesEinsamenFlummis.java",
            800, 450).start();
   }
}
