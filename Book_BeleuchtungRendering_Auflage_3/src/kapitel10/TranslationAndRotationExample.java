package kapitel10;
import org.ejml.simple.SimpleMatrix;

public class TranslationAndRotationExample {
	public static void main(String[] args) {
	   // Beispiel 1: Translation und Rotation im 2D-Beispiel
      System.out.println("Beispiel 1: Translation und Rotation im 2D-Beispiel");
		SimpleMatrix V = new SimpleMatrix(new double[][] 
				{{1,2,1.5,0},
		     	 {0,0,1  ,1},
		     	 {1,1,1  ,0}});
		
		SimpleMatrix A = new SimpleMatrix(new double[][] 
				{{1,0,3},
			     {0,1,0},
			     {0,0,1}});
		
		double w = Math.PI/2;
		SimpleMatrix B = new SimpleMatrix(new double[][] 
				{{Math.cos(w),-Math.sin(w),0},
			    {Math.sin(w), Math.cos(w),0},
			    {0,0,1}});
		
		long startZeit = System.currentTimeMillis();
		for (int i=0; i<100000; i++) 
			B.mult(A.mult(V));		
		long stopZeit  = System.currentTimeMillis();
		System.out.println("Transformation B(A*V) (100000 Mal "+(stopZeit-startZeit)+" ms)");
		System.out.println(B.mult(A.mult(V)));
		
		startZeit = System.currentTimeMillis();
		SimpleMatrix C = B.mult(A);
		for (int i=0; i<100000; i++) 
			C.mult(V);		
		stopZeit  = System.currentTimeMillis();
		System.out.println("Transformation (B*A)V (100000 Mal "+(stopZeit-startZeit)+" ms)");
		System.out.println(C.mult(V));
		
		if (B.mult(A.mult(V)).isIdentical(C.mult(V), 0.0001))
			System.out.println("B(A*V) und (B*A)V liefern das gleiche Ergebnis.\n");
		
      // Beispiel 2: Inverse Transformation im 2D-Beispiel
      System.out.println("Beispiel 2: Inverse Transformation im 2D-Beispiel");
		System.out.println((A.invert().mult(B.invert()).mult(C.mult(V))));
		//System.out.println((B.invert().mult(A.invert()).mult(C.mult(V))));
	}
}
