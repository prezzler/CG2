package kapitel04.loesungen;

import kapitel04.Vektor2D;
import kapitel04.loesungen.*;
import java.awt.*;
import java.awt.event.*;

public class Interpolation_GUI_Farbwerte extends Frame {
   
    private int[] clickX = new int[100]; // Array to store X coordinates of the first 3 clicks
    private int[] clickY = new int[100]; // Array to store Y coordinates of the first 3 clicks
    private Color[] color = new Color[100];
    private int clickCount = 0; // Counter to track the number of clicks

    public Interpolation_GUI_Farbwerte(String titel, int w, int h) {
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
                    if (clickCount < 3) {
//                	color[clickCount] = Color.GREEN;
                    }
                    else {
                    	
                    	color[clickCount] = interpolation_result();
//                	color[clickCount] = Color.blue;
                    	
                    }
                    // Redraw the window to update the drawings
                    clickCount++;
                    repaint();
                }
                System.out.println("Too Many Points (Maximum 100)");
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
		double a_red = 255, a_green = 0, a_blue = 0;
		double b_red = 0, b_green = 255, b_blue = 0;
		double c_red = 0, c_green = 0, c_blue = 255; 
		
		int result_red   = (int) TriangleInterpolation.barycentricInterpolation4(P, A, B, C, a_red, b_red, c_red);
		int result_green = (int) TriangleInterpolation.barycentricInterpolation4(P, A, B, C, a_green, b_green, c_green);
		int result_blue  = (int) TriangleInterpolation.barycentricInterpolation4(P, A, B, C, a_blue, b_blue, c_blue);
    	
		System.out.println("Point with RGB:");
		System.out.println(result_red);
    	System.out.println(result_green);
    	System.out.println(result_blue);
    	System.out.println();
    	
		if (result_red > 255 || result_green > 255 || result_blue > 255 || result_red < 0 || result_green < 0
				|| result_blue < 0) {
			return Color.black; // Return default color if any component is out of range
		} else {
			return new Color(result_red, result_green, result_blue); // Return interpolated color
		}

    		
    }
   
    public void paint(Graphics g) {
        super.paint(g);
        color[0] = new Color(255,0,0);
        color[1] = new Color(0,255,0);
        color[2] = new Color(0,0,255);

        // Draw the first 3 clicks in red
        for (int i = 0; i < clickCount; i++) {
        	g.setColor(color[i]);
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
    	Interpolation_GUI_Farbwerte f = new Interpolation_GUI_Farbwerte("Interpolation Farbwerte!", 500, 500);
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
