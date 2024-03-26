package kapitel04.loesungen;

import kapitel04.Vektor2D;
import kapitel04.loesungen.*;
import java.awt.*;
import java.awt.event.*;

public class Interpolation_Mouse extends Frame {
   
    private int[] clickX = new int[100]; // Array to store X coordinates of the first 3 clicks
    private int[] clickY = new int[100]; // Array to store Y coordinates of the first 3 clicks
    private Color[] color = new Color[100];
    private int clickCount = 0; // Counter to track the number of clicks

    public Interpolation_Mouse(String titel, int w, int h) {
        setTitle(titel);
        setSize(w, h);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // Store the first 3 click positions
                if (clickCount < 100) {
                    clickX[clickCount] = e.getX();
                    clickY[clickCount] = e.getY();
                }
                if (clickCount < 3) {
                	color[clickCount] = Color.GREEN;
                }
                else {
                	color[clickCount] = interpolation_result();
//                	color[clickCount] = Color.blue;
                	
                }
                // Redraw the window to update the drawings
                clickCount++;
                repaint();
            }
        });

        setVisible(true);
    }

    public static double triangleArea(double x1, double y1, double x2, double y2, double x3, double y3) {
        return Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0);
    }
    
    // Method to check if a point (x, y) is inside a triangle formed by three points (x1, y1), (x2, y2), (x3, y3)
    public static boolean isInsideTriangle(double x1, double y1, double x2, double y2, double x3, double y3, double x, double y) {
        // Calculate the area of the triangle formed by the three points
        double totalArea = triangleArea(x1, y1, x2, y2, x3, y3);

        // Calculate the areas of the three triangles formed by the given point and each pair of the triangle's vertices
        double area1 = triangleArea(x, y, x1, y1, x2, y2);
        double area2 = triangleArea(x, y, x2, y2, x3, y3);
        double area3 = triangleArea(x, y, x3, y3, x1, y1);

        // Check if the sum of the areas of the three smaller triangles is equal to the area of the original triangle
        return (totalArea == area1 + area2 + area3);
    }
    
    public Color interpolation_result() {
		Vektor2D P = new Vektor2D(clickX[clickCount], clickY[clickCount]);
		
		Vektor2D A = new Vektor2D(clickX[0],clickY[0]);
		Vektor2D B = new Vektor2D(clickX[1],clickY[1]);
		Vektor2D C = new Vektor2D(clickX[2],clickY[2]);
		double w_a=1, w_b=1, w_c=1; 
		
		boolean result = isInsideTriangle(clickX[0],clickY[0],clickX[1],clickY[1],clickX[2],clickY[2],clickX[clickCount], clickY[clickCount]);
//		double result = TriangleInterpolation.barycentricInterpolation4(P, A, B, C, w_a, w_b, w_c);
    	System.out.println(result);
		if(result == false) {
    		return Color.RED;
    	}else {
    		return Color.YELLOW;
    	}
    		
    }
    
    public void paint(Graphics g) {
        super.paint(g);

        // Draw the first 3 clicks in red
        g.setColor(Color.GREEN);
        for (int i = 0; i < clickCount; i++) {
            g.fillOval(clickX[i], clickY[i], 10, 10);
        }

        if(clickCount > 3) {
        	
        	for (int i = 3; i < clickCount; i++) {
        		g.setColor(color[i]);
        		g.fillOval(clickX[i], clickY[i], 10, 10);
        	}
        	
        }
    }

    public static void main(String[] args) {
        Interpolation_Mouse f = new Interpolation_Mouse("Interpolation Mouse!", 500, 500);
    }
}


//package kapitel04.loesungen;
//
//import java.awt.*;
//import java.awt.event.*;
//
//
//public class MausKlick extends BasisFenster {
//   
//	int t1_x, t1_y;
//	int t2_x, t2_y;
//	int t3_x, t3_y;
//	
//	public MausKlick(String titel, int w, int h) {
//      super(titel, w, h);
//      setSize(w, h);
//      
//      addWindowListener(new WindowAdapter() {
//         public void windowClosing(WindowEvent e) {
//            System.exit(0);
//         }
//      });
//      
//      addMouseListener(new MouseAdapter() {
//         public void mousePressed(MouseEvent e) {
//            Graphics g = getGraphics();
//            g.setColor(Color.green);
//            g.fillOval(e.getX(), e.getY(), 10, 10);
//            
//         }
//      });
//      
//      setVisible(true);
//   }
//
//   public static void main(String[] args) {
//      MausKlick f = new MausKlick("Schliesse mich!", 500, 200);
//   }
//}
