package kapitel04.loesungen;

import org.junit.Test;

import static org.junit.Assert.*;
import kapitel04.loesungen.TriangleInterpolation;

import kapitel04.Vektor2D;

//import kapitel04.LineareAlgebra;
//import kapitel04.Vektor2D;

// GenericInterpolator ist eine Interface-Function, die die gewünschte (BarycentricInterpolation-Methode) Signatur entspricht
// heißt: gleicher return , public, und gleiceh parametertypen, anzahl
// apply ist hierbei ein wichtiges festes Stichwort (https://www.geeksforgeeks.org/function-interface-in-java-with-examples/) 
interface GenericInterpolator {
	public double apply(Vektor2D P, Vektor2D A, Vektor2D B, Vektor2D C, double w_a, double w_b, double w_c);
}

public class tests {

	// Erste Test Variablen

	Vektor2D inside = new Vektor2D(5, 5);
	Vektor2D outside = new Vektor2D(10, 10);
	Vektor2D t1 = new Vektor2D(0, 0);
	Vektor2D t2 = new Vektor2D(10, 0);
	Vektor2D t3 = new Vektor2D(0, 10);
	double value_t1 = 10, value_t2 = 5, value_t3 = 1;
	double expected_inside = 3.0;
	double expected_outside = -4.0;
	double expected_t1 = 10;

	GenericInterpolator interpolations[] = { TriangleInterpolation::barycentricInterpolation,
			TriangleInterpolation::barycentricInterpolation2, TriangleInterpolation::barycentricInterpolation3,
			TriangleInterpolation::barycentricInterpolation4 };

	@Test
	public void teste_BarycentricInterpolation_point_inside() {

		double result;

//			for (int i = 0; i<4; i++) {
		for (GenericInterpolator interpol_function : interpolations) {
			result = interpol_function.apply(inside, t1, t2, t3, value_t1, value_t2, value_t3);
			assertEquals(expected_inside, result, 0.0001);
		}

	}

	@Test
	public void teste_BarycentricInterpolation_point_outside() {

		double result;

		for (GenericInterpolator interpol_function : interpolations) {
			result = interpol_function.apply(outside, t1, t2, t3, value_t1, value_t2, value_t3);
			assertEquals(expected_outside, result, 0.0001);
		}
	}

	@Test
	public void teste_BarycentricInterpolation_point_t1() {

		double result;

		for (GenericInterpolator interpol_function : interpolations) {
			result = interpol_function.apply(t1, t1, t2, t3, value_t1, value_t2, value_t3);
			assertEquals(expected_t1, result, 0.0001);
		}
	}

	@Test
	public void teste_BarycentricInterpolation_same_Vertice() {

		double result;

		for (GenericInterpolator interpol_function : interpolations) {
			result = interpol_function.apply(t1, t1, t1, t3, value_t1, value_t2, value_t3);
			assertEquals(Double.NaN, result, 0.0001);
		}
	}

	@Test
	public void teste_BarycentricInterpolation_collinear_Points() {

		Vektor2D t3b = new Vektor2D(5, 0);
		double result;

		for (GenericInterpolator interpol_function : interpolations) {
			result = interpol_function.apply(t1, t1, t2, t3b, value_t1, value_t2, value_t3);
			assertEquals(Double.NaN, result, 0.0001);
		}
	}

	@Test
	public void teste_BarycenticInterpolation_negative_Weights() {
		double neg_value_t1 = -value_t1;
		double neg_value_t2 = -value_t2;
		double neg_value_t3 = -value_t3;

		double result;

		for (GenericInterpolator interpol_function : interpolations) {
			result = interpol_function.apply(inside, t1, t2, t3, neg_value_t1, neg_value_t2, neg_value_t3);
			assertEquals(-expected_inside, result, 0.0001);
		}
	}
}
