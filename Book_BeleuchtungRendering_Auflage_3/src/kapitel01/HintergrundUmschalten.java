package kapitel01;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3d;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JToggleButton;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class HintergrundUmschalten extends LWJGLBasisFenster {
   private JToggleButton tb;

   public HintergrundUmschalten(String title, int width, int height) {
      super(title, width, height);

      JFrame f = new JFrame();
      f.setTitle(title);
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      tb = new JToggleButton("Hintergrund umschalten");
      f.add(tb, BorderLayout.SOUTH);
      Canvas c = new Canvas();
      f.add(c);
      f.setBounds(0, 0, width, height - tb.getHeight());
      f.setLocationRelativeTo(null);
      f.setVisible(true);

      initDisplay(c);
   }

   @Override
   public void renderLoop() {
      while (!Display.isCloseRequested()) {
         double t = System.nanoTime() / 1e9;
         if (tb.isSelected())
            glClearColor(0.1f, 0.2f, 0.3f, 1.0f);
         else
            glClearColor(0.95f, 0.95f, 0.95f, 1.0f);
         glClear(GL_COLOR_BUFFER_BIT);
         glColor3d(0.35 + 0.30*Math.sin(t), 0.63 + 0.24*Math.sin(t), 0.73 + 0.22*Math.sin(t));

         glBegin(GL_TRIANGLES);
         glVertex3d(-0.5, -0.5 + 0.3f*Math.sin(t), 0);
         glVertex3d( 0.5,  0.3*Math.sin(t*1.3), 0);
         glVertex3d( 0.3*Math.sin(t*2), 0.5, 0);
         glEnd();

         Display.update();
      }
   }

   public static void main(String[] args) throws LWJGLException, IOException {
     new HintergrundUmschalten("vividus Wissenschaftsverlag. Warum sich der Dino furchtbar erschreckte. Kapitel 1: HintergrundUmschalten.java", 800, 450).start();
   }
}
