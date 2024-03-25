package kapitel01.particle_version04_strategypattern;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glOrtho;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.lwjgl.opengl.Display;

import kapitel01.LWJGLBasisFenster;
import kapitel01.POGL;

public class WeltDerBuntenFlummis extends LWJGLBasisFenster {
   private ObjektManager flummis;

   public WeltDerBuntenFlummis(String title, int width, int height) {
      super(title, width, height);
      initDisplay();
      flummis = ObjektManager.getExemplar();
      erzeugeFlummies(1000);
   }

   private void erzeugeFlummies(int anz) {
      Random rand = ThreadLocalRandom.current();
      for (int i = 0; i < anz; i++) {
         Flummi flummi = new Flummi(rand.nextInt(WIDTH), rand.nextInt(HEIGHT), rand.nextFloat() + 1, rand.nextInt(3) + 1,
               rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
         if (i < anz / 3)
            flummi.setVerhalten(new VerhaltenPerfekterFlummi(flummi));
         else if (i > anz / 3 && i < 2 * anz / 3)
            flummi.setVerhalten(new VerhaltenZufälligerFlummi(flummi));
         flummis.registriereFlummi(flummi);
      }
   }

   @Override
   public void renderLoop() {
      glEnable(GL_DEPTH_TEST);

      while (!Display.isCloseRequested()) {
         POGL.clearBackgroundWithColor(0.95f, 0.95f, 0.95f, 1.0f);

         glLoadIdentity();
         glOrtho(0, WIDTH, HEIGHT, 0, 0, 1);
         glDisable(GL_DEPTH_TEST);

         for (int i = 1; i <= flummis.getFlummiSize(); i++) {
            Flummi aktFlummi = flummis.getFlummi(i);
            aktFlummi.render();
            aktFlummi.update();
         }

         Display.update();
      }
      Display.destroy();
   }

   public static void main(String[] args) {
      new WeltDerBuntenFlummis(
            "vividus Verlag. Dino-Buch. Kap. 1 (particle_version04_strategypattern): WeltDerBuntenFlummis.java", 800,
            450).start();
   }
}
