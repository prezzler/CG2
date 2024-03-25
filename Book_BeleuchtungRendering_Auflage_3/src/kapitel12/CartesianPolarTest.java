package kapitel12;

import kapitel04.Vektor3D;

public class CartesianPolarTest {
	public static void main(String[] args) {
		Vektor3D v = new Vektor3D(1.0, 2.0, 3.0);		
		System.out.println("Vektor        : " + v);
		Vektor3D vPolar = LineareAlgebra.cartesianToPolar(v);
		System.out.println("Vektor (Polar): " + vPolar);
		Vektor3D v2 = LineareAlgebra.polarToCartesian(vPolar);
		System.out.println("Vektor        : " + v2);
		
		v = new Vektor3D(3.0, 2.0, -1.0);		
		System.out.println("Vektor        : " + v);
		vPolar = LineareAlgebra.cartesianToPolar(v);
		System.out.println("Vektor (Polar): " + vPolar);
		v2 = LineareAlgebra.polarToCartesian(vPolar);
		System.out.println("Vektor        : " + v2);

		v = new Vektor3D(1.0, -3.0, 1.0);		
		System.out.println("Vektor        : " + v);
		vPolar = LineareAlgebra.cartesianToPolar(v);
		System.out.println("Vektor (Polar): " + vPolar);
		v2 = LineareAlgebra.polarToCartesian(vPolar);
		System.out.println("Vektor        : " + v2);
		
		v = new Vektor3D(1.0, 0.0, 0.0);		
		System.out.println("Vektor        : " + v);
		vPolar = LineareAlgebra.cartesianNormToPolar(v);
		System.out.println("Vektor (Polar): " + vPolar);
		v2 = LineareAlgebra.polarToCartesianNorm(vPolar);
		System.out.println("Vektor        : " + v2);
	}
}
