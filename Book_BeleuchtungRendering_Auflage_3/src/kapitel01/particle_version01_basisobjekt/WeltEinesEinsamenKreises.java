package kapitel01.particle_version01_basisobjekt;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glOrtho;

import org.lwjgl.opengl.Display;

import kapitel01.LWJGLBasisFenster;
import kapitel01.POGL;

public class WeltEinesEinsamenKreises extends LWJGLBasisFenster {
   private Kreis kreis;

   public WeltEinesEinsamenKreises(String title, int width, int height) {
      super(title, width, height);
      initDisplay();
      kreis = new Kreis(width/2, height/2);
   }

   @Override
   public void renderLoop() {
      glEnable(GL_DEPTH_TEST);

      while (!Display.isCloseRequested()) {
         POGL.clearBackgroundWithColor(0.95f, 0.95f, 0.95f, 1.0f);

         glLoadIdentity();
         glOrtho(0, WIDTH, HEIGHT, 0, 0, 1);
         glDisable(GL_DEPTH_TEST);

         // einen Kreis anzeigen
         kreis.render();
         
         Display.update();
      }
      Display.destroy();
   }

   public static void main(String[] args) {
      new WeltEinesEinsamenKreises("vividus Verlag. Dino-Buch. Kap. 1 (particle_version01_basisobjekt): WeltEinesEinsamenKreises.java", 800, 450).start();
   }
}
