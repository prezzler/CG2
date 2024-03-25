// Lösung von Dustin Heyer
package kapitel01.loesungen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import kapitel04.Vektor2D;

public class DreieckZeichnenNormal {
	int ident = 0;
	Vektor2D P, A, B, C;
	double w_a, w_b, w_c, w_p;
	int x, y;

	public static double barycentricInterpolation3(Vektor2D P, Vektor2D A, Vektor2D B, Vektor2D C, double w_a,
			double w_b, double w_c) {
		double denom = 1. / ((B.y - C.y) * (A.x - C.x) + (C.x - B.x) * (A.y - C.y));
		double u = ((B.y - C.y) * (P.x - C.x) + (C.x - B.x) * (P.y - C.y)) * denom;
		double v = ((C.y - A.y) * (P.x - C.x) + (A.x - C.x) * (P.y - C.y)) * denom;
		double w = 1. - u - v;
		
		return u * w_a + v * w_b + w * w_c;
	}

	public static boolean imDreieck(Vektor2D P, Vektor2D A, Vektor2D B, Vektor2D C) {
		// E:P=OA+r*AB+s*AC
		double s, r;
		s = -((C.x-A.x)*P.y + (A.y-C.y)*P.x + A.x*C.y - A.y*C.x) / 
	         ((B.x-A.x)*C.y + (A.y-B.y)*C.x + A.x*B.y - A.y*B.y);
		r = (-((B.x - A.x) * s - P.x + A.x) / (C.x - A.x));
		
		return ((0<s) && (s<=1) && (0<r) && (r<=1) && (0<(s+r)) && ((s+r)<=1));
	}

	public DreieckZeichnenNormal(String titel, int w, int h) {
		JFrame f = new JFrame();
		f.setSize(w, h);
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
					Color mycolor = new Color((float)w_a, (float)w_a, (float)w_a); 
					g.setColor(mycolor);
					g.fillOval(x, y, 20, 20);
					A = new Vektor2D(x, y);
					ident++;
					break;
				case 1:
					w_b = 0.5;
					mycolor = new Color((float)w_b, (float)w_b, (float)w_b); 
					g.setColor(mycolor);
					g.fillOval(x, y, 20, 20);
					B = new Vektor2D(x, y);
					ident++;
					break;
				case 2:
					w_c = 1.0;
					mycolor = new Color((float)w_c, (float)w_c, (float)w_c); 
					g.setColor(mycolor);
					g.fillOval(x, y, 20, 20);
					C = new Vektor2D(x, y);
					ident++;
					break;
				default:
					P = new Vektor2D(x, y);
					w_p = barycentricInterpolation3(P, A, B, C, w_a, w_b, w_c);
					if (imDreieck(P, A, B, C)) {
						mycolor = new Color((float)w_p, (float)w_p, (float)w_p); 
						g.setColor(mycolor);
						ident++;
						g.fillOval(x, y, 30, 30);
					} else {
						g.setColor(Color.red);
						g.fillOval(x, y, 5, 5);
						ident++;
					}
					break;
				}
			}
		});
		f.setVisible(true);
	}

	public static void main(String[] args) {
		new DreieckZeichnenNormal("Schliesse mich!", 640, 480);
	}
}
