package kapitel10;
import org.ejml.simple.SimpleMatrix;

public class RotationExample {
	public static void main(String[] args) {
	   // Beispiel 1: 2D-Rotationsbeipiel
	   System.out.println("Beispiel 1: 2D-Rotationsbeipiel");
		SimpleMatrix R = new SimpleMatrix(new double[][] {
			{Math.cos(Math.PI/4), -Math.sin(Math.PI/4)},
			{Math.sin(Math.PI/4),  Math.cos(Math.PI/4)}});		
		SimpleMatrix V = new SimpleMatrix(new double[][] {{2},{1}});	
		SimpleMatrix V2 = R.mult(V);
		System.out.println(V2);
		SimpleMatrix R2 = R.transpose();
		System.out.println(R2.mult(V2));		
	}
}
