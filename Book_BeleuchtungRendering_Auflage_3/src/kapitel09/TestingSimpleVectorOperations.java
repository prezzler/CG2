package kapitel09;

import org.ejml.simple.SimpleMatrix;

public class TestingSimpleVectorOperations {
	public static void main(String[] args) {
		// Beispiel 1: eine Matrix wird erzeugt und zweimal transponiert
		System.out.println("Beispiel 1: eine Matrix wird erzeugt und zweimal transponiert");
		SimpleMatrix A = new SimpleMatrix(new double[][] { { 1, 2, 3 }, { 0, 0, 4 } });
		System.out.println(A);
		A = A.transpose();
		System.out.println(A);
		A = A.transpose();
		System.out.println(A);

		// Beispiel 2: Addition von Matrizen
		System.out.println("Beispiel 2: Addition von Matrizen");
		A = new SimpleMatrix(new double[][] { { 1, -1 }, { 4, 0 }, { -2, 3 } });
		SimpleMatrix B = new SimpleMatrix(new double[][] { { 2, 0 }, { -1, -2 }, { 1, 0 } });
		System.out.println(A.plus(B));

		// Beispiel 3: Skalierung
		System.out.println("Beispiel 3: Skalierung");
		A = new SimpleMatrix(new double[][] { { 1, 0 }, { 1, 2 }, { 3, 0 } });
		System.out.println(A.scale(2));

		// Beispiel 4: Matrixmultiplikation
		System.out.println("Beispiel 4: Matrixmultiplikation");
		A = new SimpleMatrix(new double[][] { { -2, 0 }, { 1, 3 } });
		B = new SimpleMatrix(new double[][] { { 1 }, { 2 } });
		System.out.println(A.mult(B));

		// Beispiel 5: Determinante einer 2x2-Matrix
		System.out.println("Beispiel 5: Determinante einer 2x2-Matrix");
		A = new SimpleMatrix(new double[][] { { -2, 1 }, { 0, 4 } });
		System.out.println(A.determinant());
	}
}
