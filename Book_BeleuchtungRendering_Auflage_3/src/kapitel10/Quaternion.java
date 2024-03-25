package kapitel10;

import org.ejml.simple.SimpleMatrix;

import kapitel04.LineareAlgebra;
import kapitel04.Vektor3D;

public class Quaternion {
	public float s, x, y, z;

	public Quaternion() {
		this(0, 0, 0, 0);
	}

	public Quaternion(Vektor3D v) {
		this(0, (float) v.x, (float) v.y, (float) v.z);
	}

	public Quaternion(float s, Vektor3D v) {
		this(s, (float) v.x, (float) v.y, (float) v.z);
	}

	public Quaternion(Quaternion q) {
		this(q.s, q.x, q.y, q.z);
	}

	public Quaternion(float s, float x, float y, float z) {
		this.s = s;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static Quaternion getRotationsQuaternion(Vektor3D achse, float winkel) {
		double winkelHalb = LineareAlgebra.degreeToRad(winkel/2);
		return new Quaternion(
				(float)Math.cos(winkelHalb),
				(float)(Math.sin(winkelHalb)*achse.x),
				(float)(Math.sin(winkelHalb)*achse.y),
				(float)(Math.sin(winkelHalb)*achse.z));
	}

	public static Quaternion getInverseRotationsQuaternion(Vektor3D achse, float winkel) {
		double winkelHalb = LineareAlgebra.degreeToRad(winkel/2);
		return new Quaternion(
				(float)Math.cos(winkelHalb),
				(float)(-Math.sin(winkelHalb)*achse.x),
				(float)(-Math.sin(winkelHalb)*achse.y),
				(float)(-Math.sin(winkelHalb)*achse.z));
	}

	public String toString() {
		return "(" + s + ", (" + x + ", " + y + ", " + z + "))";
	}

	public void negate() {
		this.s = -this.s;
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
	}

	public float length() {
		return (float) Math.sqrt(s * s + x * x + y * y + z * z);
	}

	public void normalize() {
		float length = length();
		this.s = this.s / length;
		this.x = this.x / length;
		this.y = this.y / length;
		this.z = this.z / length;
	}

	public static Quaternion normalize(Quaternion qV) {
		Quaternion result = new Quaternion(qV);
		result.normalize();
		return result;
	}

	public Quaternion conjugate(Quaternion qV) {
		return new Quaternion(qV.s, -qV.x, -qV.y, -qV.z);
	}

	public static Quaternion add(Quaternion qV, Quaternion qW) {
		return new Quaternion(qV.s + qW.s, qV.x + qW.x, qV.y + qW.y, qV.z + qW.z);
	}

	public static Quaternion mult(Quaternion qV, Quaternion qW) {
		return new Quaternion(
				qV.s * qW.s - qV.x * qW.x - qV.y * qW.y - qV.z * qW.z,
				qV.s * qW.x + qV.x * qW.s + qV.y * qW.z - qV.z * qW.y,
				qV.s * qW.y - qV.x * qW.z + qV.y * qW.s + qV.z * qW.x,
				qV.s * qW.z + qV.x * qW.y - qV.y * qW.x + qV.z * qW.s);
	}

	public static float cosWinkel(Quaternion qV, Quaternion qW) {
		float len = qV.length() * qW.length();
		return qV.s * qW.s / len + qV.x * qW.x / len + qV.y * qW.y / len + qV.z * qW.z / len;
	}

	public static float cosWinkelNormiert(Quaternion qV, Quaternion qW) {
		return qV.s * qW.s + qV.x * qW.x + qV.y * qW.y + qV.z * qW.z;
	}
	
	public static Quaternion rotiereFast(Vektor3D v, Vektor3D achse, float winkel) {
		return mult(mult(getRotationsQuaternion(achse, winkel), new Quaternion(v)), getInverseRotationsQuaternion(achse, winkel));
	}

	public static Quaternion rotiere(Vektor3D v, Vektor3D achse, float winkel) {
		Quaternion qV = new Quaternion(v);
		//System.out.println("qV: " + qV);
		Quaternion qR = getRotationsQuaternion(achse, winkel);
		//System.out.println("qR: " + qR);
		Quaternion qIR = getInverseRotationsQuaternion(achse, winkel);
		//System.out.println("qIR: " + qIR);

		Quaternion qRV = mult(qR, qV);
		//System.out.println("qRV: " + qRV);
		Quaternion qRVIR = mult(qRV, qIR);
		//System.out.println("qRVIR: " + qRVIR);

		return qRVIR;
	}
	
	public static Quaternion rotiereMatrixFast(Vektor3D v, Vektor3D achse, float winkel) {
		Quaternion qR = getRotationsQuaternion(achse, winkel);
		   
		float xy = qR.x*qR.y;		float xz = qR.x*qR.z;		float yz = qR.y*qR.z;
		float sx = qR.s*qR.x;		float sy = qR.s*qR.y;		float sz = qR.s*qR.z;
		float xx = qR.x*qR.x;		float yy = qR.y*qR.y;		float zz = qR.z*qR.z;
		return new Quaternion(0, 
				(float)((1-2*(yy + zz)) * v.x +    2*(xy - sz)  * v.y +    2*(xz + sy)  * v.z), 
				(float)   (2*(xy + sz)  * v.x + (1-2*(xx + zz)) * v.y +    2*(yz - sx)  * v.z),
				(float)   (2*(xz - sy)  * v.x +    2*(yz + sx)  * v.y + (1-2*(xx + yy)) * v.z));
	}

	public static Quaternion rotiereMatrixEJML(Vektor3D v, Vektor3D achse, float winkel) {
		Quaternion qR = getRotationsQuaternion(achse, winkel);
		   
		SimpleMatrix R = new SimpleMatrix(new double[][] { 
			{1,                           0,                           0,                           0},
			{0, 1-2*(qR.y*qR.y + qR.z*qR.z),   2*(qR.x*qR.y - qR.s*qR.z),   2*(qR.x*qR.z + qR.s*qR.y)}, 
			{0,   2*(qR.x*qR.y + qR.s*qR.z), 1-2*(qR.x*qR.x + qR.z*qR.z),   2*(qR.y*qR.z - qR.s*qR.x)},
			{0,   2*(qR.x*qR.z - qR.s*qR.y),   2*(qR.y*qR.z + qR.s*qR.x), 1-2*(qR.x*qR.x + qR.y*qR.y)}});
		   
		SimpleMatrix V = new SimpleMatrix(new double[][] {{0}, {v.x}, {v.y}, {v.z}});
		SimpleMatrix qRVIR = R.mult(V);
		   
		return new Quaternion((float)qRVIR.get(0,0), (float)qRVIR.get(1,0), (float)qRVIR.get(2,0), (float)qRVIR.get(3,0));
	}

	public static Quaternion lerp(Quaternion qV, Quaternion qW, float t) {
		return new Quaternion(
				qV.s * (1 - t) + qW.s * t, 
				qV.x * (1 - t) + qW.x * t, 
				qV.y * (1 - t) + qW.y * t,
				qV.z * (1 - t) + qW.z * t);
	}

	public static Quaternion slerp(Quaternion qV, Quaternion qW, float t) {
		float cosWinkel = cosWinkel(qV, qW);
		if (cosWinkel < 0.0f) {
			qW.negate();
			cosWinkel = -cosWinkel;
		}

		float k0, k1;
		if (cosWinkel > 0.9995f) {
			k0 = 1.0f - t;
			k1 = t;
		} else {
			float sinWinkel = (float) Math.sqrt(1.0f - cosWinkel * cosWinkel);
			float winkel = (float) Math.atan2(sinWinkel, cosWinkel);
			float einsDurchSinWinkel = 1.0f / sinWinkel;

			k0 = (float) (Math.sin((1.0f - t) * winkel) * einsDurchSinWinkel);
			k1 = (float) (Math.sin(t * winkel) * einsDurchSinWinkel);
		}

		return new Quaternion(
				qV.s * k0 + qW.s * k1, 
				qV.x * k0 + qW.x * k1, 
				qV.y * k0 + qW.y * k1,
				qV.z * k0 + qW.z * k1);
	}
}