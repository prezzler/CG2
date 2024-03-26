package kapitel04.loesungen;

import kapitel04.Vektor2D;

public class Performance_Test_TriangleInterpolation {

	public static double Performance_1000times_ms(GenericInterpolator interpolation) {
		Vektor2D inside = new Vektor2D(5, 5);
		Vektor2D t1 = new Vektor2D(0, 0);
		Vektor2D t2 = new Vektor2D(10, 0);
		Vektor2D t3 = new Vektor2D(0, 10);
		double value_t1 = 10, value_t2 = 5, value_t3 = 1;

		long nano_start = System.nanoTime();
		
		for(int i = 0; i< 1000; i++) { 
			interpolation.apply(inside, t1, t2, t3, value_t1, value_t2, value_t3);
		}
		
		long nano_end = System.nanoTime();
		
		double ms = (nano_end - nano_start)	/ 1_000_000.0;
		return ms;
	}
	
	public static void main(String[] args) {
	
		System.out.print("Performance-Test for Barycentric Interpolation with 4 different implementations "
				+ "\n1000 function calls each"
				+ "\nregular values used"
				+ "\n[in miliseconds]\n"
				+ "*****************************************************************************\n");
		GenericInterpolator interpolations[] = { TriangleInterpolation::barycentricInterpolation,
				TriangleInterpolation::barycentricInterpolation2, TriangleInterpolation::barycentricInterpolation3,
				TriangleInterpolation::barycentricInterpolation4 };
		double ret = 0.0;
		for(int i = 0; i<interpolations.length; i++) {
			System.out.printf("barycentricInterpolation %d\t", i);
			ret = Performance_1000times_ms(interpolations[i]);
			System.out.printf("%f ms\n", ret);
		}
		
	}

}
