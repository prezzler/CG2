package kapitel01;

import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glFrustum;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTranslated;

import java.awt.Canvas;

import javax.swing.JFrame;

import org.lwjgl.opengl.Display;

public class FlaecheSchwingt extends LWJGLBasisFenster {
   public FlaecheSchwingt(String title, int width, int height) {
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
      // nur Verbindungslinien rendern
      glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

      long start = System.nanoTime();
      while (!Display.isCloseRequested()) {
         double t = (System.nanoTime() - start)/1e9;
         POGL.clearBackgroundWithColor(0.95f, 0.95f, 0.95f, 1.0f);

         // Wir konstruieren eine schwingende Projektionsfläche
         // M = I
         glLoadIdentity();

         // M*P
         glFrustum(-1, 1, -1, 1, 3, 10);

         // M*T
         glTranslated(0, 0, -4);

         // M*R
         glRotatef((float)Math.sin(t)*120.0f, 0.0f, 1.0f, 0.0f);

         // M*S
         glScaled(0.8, 0.8, 0.8);

         // Object-Space
         glColor3d(0.05, 0.39, 0.51);
         glLineWidth(3.0f);
         POGL.renderViereck();

         // Es wird also folgende Berechnungkette vollzogen:
         // M = I -- zur Initialisierung
         // M = P*T*R*S -- Transformationskette vom Object-Space zur Ausgabe (von recht
         // nach links)
         // v_neu = M * v_alt -- Transformation aller Vektoren mit M
         
         Display.update();
      }
   }

   public static void main(String[] args) {
      new FlaecheSchwingt("vividus Verlag. Dino-Buch. Kap. 1: FleacheSchwingt.java", 500, 500).start();
   }
}
