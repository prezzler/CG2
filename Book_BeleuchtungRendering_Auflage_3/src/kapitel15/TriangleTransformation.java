package kapitel15;

import org.ejml.simple.SimpleMatrix;

public class TriangleTransformation {
	public static void main(String[] args) {
		// Dreieck 1
		// A=(1,0,0), B=(3,1,0), C=(2,4,0)
		SimpleMatrix A = new SimpleMatrix(new double[][] 
				{{1,3,2}, 
			     {0,1,4}, 
			     {1,1,1}});
		
		System.out.println("Determinante von A: "+A.determinant());

		SimpleMatrix T = new SimpleMatrix(new double[][] 
				{{1,0,2},
			     {0,1,0},
			     {0,0,1}});
		
		double w = Math.PI/2;
		SimpleMatrix R = new SimpleMatrix(new double[][] 
				{{Math.cos(w),-Math.sin(w),0},
			    {Math.sin(w), Math.cos(w),0},
			    {0,0,1}});
		
		SimpleMatrix S = new SimpleMatrix(new double[][] 
				{{2,0,0},
			     {0,2,0},
			     {0,0,1}});
		
		S.mult(R.mult(T.mult(A)));
		
		System.out.println(A);
		System.out.println("Ergebnis:" + S.mult(R.mult(T.mult(A))));

		// Dreieck 2 (Resultat)
		// A=(1,0,0), B=(3,1,0), C=(2,2,0)
		SimpleMatrix A2 = new SimpleMatrix(new double[][] 
				{{0,-2,-8}, 
			     {6,10, 8}, 
			     {1, 1, 1}});
		
		System.out.println("Determinante von A2: "+A2.determinant());

		// Jetzt wollen wir die 2 Dreiecke A und A2 als gegeben ansehen
		// und suchen die Transformation automatisch:
		System.out.println("Transformation automatisch bestimmen");
		System.out.println(A);
		System.out.println(A2);
		
		SimpleMatrix M = A2.mult(A.invert());
		//SimpleMatrix M = A2.solve(A);
		System.out.println("Transformation M: "+M);
		
		System.out.println("M jetzt anwenden");
		System.out.println(M.mult(A));
	}
}
