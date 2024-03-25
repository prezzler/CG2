package kapitel04.loesungen;

import kapitel04.LineareAlgebra;
import kapitel04.Vektor2D;

public class TriangleInterpolation {
	public static double barycentricInterpolation(Vektor2D P, Vektor2D A, Vektor2D B, Vektor2D C, double w_a, double w_b, double w_c)
	{
		Vektor2D v0 = LineareAlgebra.sub(B, A);
		Vektor2D v1 = LineareAlgebra.sub(C, A);
		Vektor2D v2 = LineareAlgebra.sub(P, A);
	    double d00 = LineareAlgebra.dotProduct(v0, v0);
	    double d01 = LineareAlgebra.dotProduct(v0, v1);
	    double d11 = LineareAlgebra.dotProduct(v1, v1);
	    double d20 = LineareAlgebra.dotProduct(v2, v0);
	    double d21 = LineareAlgebra.dotProduct(v2, v1);
	    double denom = d00 * d11 - d01 * d01;
	    double v = (d11 * d20 - d01 * d21) / denom;
	    double w = (d00 * d21 - d01 * d20) / denom;
	    double u = 1.0 - v - w;
	    
	    return u*w_a+v*w_b+w*w_c;
	}
	
	public static double barycentricInterpolation2(Vektor2D P, Vektor2D A, Vektor2D B, Vektor2D C, double w_a, double w_b, double w_c)
	{
		Vektor2D v0 = LineareAlgebra.sub(B, A);
		Vektor2D v1 = LineareAlgebra.sub(C, A);
		Vektor2D v2 = LineareAlgebra.sub(P, A);
	    double denom = v0.x * v1.y - v1.x * v0.y;
	    double v = (v2.x * v1.y - v1.x * v2.y) / denom;
	    double w = (v0.x * v2.y - v2.x * v0.y) / denom;
	    double u = 1.0f - v - w;
	    
	    return u*w_a+v*w_b+w*w_c;
	}
	
	public static double barycentricInterpolation3(Vektor2D P, Vektor2D A, Vektor2D B, Vektor2D C, double w_a, double w_b, double w_c)
	{
		double denom = 1./((B.y-C.y) * (A.x-C.x) + (C.x-B.x) * (A.y-C.y));
		double u = ((B.y-C.y) * (P.x-C.x) + (C.x-B.x) * (P.y-C.y)) * denom; 
		double v = ((C.y-A.y) * (P.x-C.x) + (A.x-C.x) * (P.y-C.y)) * denom; 
		double w = 1. - u - v;
		
	    return u*w_a+v*w_b+w*w_c;
	}
	
	public static double barycentricInterpolation4(Vektor2D P, Vektor2D A, Vektor2D B, Vektor2D C, double w_a, double w_b, double w_c)
	{
		double denom = 1./((B.x-A.x) * (C.y-B.y) - (B.y-A.y) * (C.x-B.x));
		double u = ((B.x-P.x) * (C.y-P.y) - (C.x-P.x) * (B.y-P.y)) * denom; 
		double v = ((C.x-P.x) * (A.y-P.y) - (A.x-P.x) * (C.y-P.y)) * denom; 
		double w = 1. - u - v;
		
	    return u*w_a+v*w_b+w*w_c;
	}
	
	public static void main(String[] args) {
		Vektor2D P = new Vektor2D(7,5);
		Vektor2D A = new Vektor2D(1,3);
		Vektor2D B = new Vektor2D(10,6);
		Vektor2D C = new Vektor2D(12,2);
		double w_a=10, w_b=8, w_c=4;
		
		double w_p = barycentricInterpolation4(P, A, B, C, w_a, w_b, w_c);
		double w_p2 = barycentricInterpolation4(P, A, A, C, w_a, w_b, w_c);
		
		System.out.println(w_p);
		System.out.println(w_p2);
	}
}
