// Lösung von Dustin Heyer
package kapitel04.loesungen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import kapitel04.LineareAlgebra;
import kapitel04.Vektor2D;

public class BaryzentrischeInterpolationInteraktiv {
   int ident = 0;
   Vektor2D P, A, B, C;
   double w_a, w_b, w_c, w_p;
   int x, y;
   
   public BaryzentrischeInterpolationInteraktiv(String title, int WIDTH, int HEIGHT) {
      JFrame f = new JFrame();
      f.setBounds(0, 0, WIDTH, HEIGHT);
      f.setTitle(title);
      f.setLocationRelativeTo(null);
      f.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            System.exit(0);
         }
      });
      f.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {
            Graphics g = f.getGraphics();
            x = e.getX();
            y = e.getY();

            switch (ident) {
            case 0:
               w_a = 0.0;
               Color mycolor = new Color((float) w_a, (float) w_a, (float) w_a);
               g.setColor(mycolor);
               g.fillOval(x, y, 15, 15);
               A = new Vektor2D(x, y);
               ident++;
               break;
            case 1:
               w_b = 0.5;
               mycolor = new Color((float) w_b, (float) w_b, (float) w_b);
               g.setColor(mycolor);
               g.fillOval(x, y, 15, 15);
               B = new Vektor2D(x, y);
               ident++;
               break;
            case 2:
               w_c = 1.0;
               mycolor = new Color((float) w_c, (float) w_c, (float) w_c);
               g.setColor(mycolor);
               g.fillOval(x, y, 15, 15);
               C = new Vektor2D(x, y);
               ident++;
               break;
            default:
               P = new Vektor2D(x, y);
               w_p = LineareAlgebra.barycentricInterpolation(P, A, B, C, w_a, w_b, w_c);
               if (insideTriangle(P, A, B, C)) {
                  mycolor = new Color((float) w_p, (float) w_p, (float) w_p);
                  g.setColor(mycolor);
                  ident++;
                  g.fillOval(x, y, 15, 15);
               } else {
                  g.setColor(Color.red);
                  g.fillOval(x, y, 15, 15);
                  ident++;
               }
               break;
            }
         }
      });
      f.setVisible(true);
   }
   
   public static boolean insideTriangle(Vektor2D P, Vektor2D A, Vektor2D B, Vektor2D C) {
       double as_x  = P.x-A.x;
       double as_y  = P.y - A.y;
       boolean s_ab = (B.x - A.x) * as_y - (B.y - A.y) * as_x > 0;
       if ((C.x - A.x) * as_y - (C.y - A.y) * as_x > 0 == s_ab)
          return false;
       if ((C.x - B.x) * (P.y - B.y) - (C.y - B.y) * (P.x - B.x) > 0 != s_ab)
          return false;
       return true;
   }

   public static void main(String[] args) {
      new BaryzentrischeInterpolationInteraktiv("vividus Verlag. Dino-Buch. Kapitel 4: BaryzentrischeInterpolationInteraktiv.java", 800, 450);
   }
}
