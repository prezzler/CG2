package kapitel10;

import java.util.Locale;

public class LerpVersusSlerp {
	public static void main(String[] args) {
		Quaternion qV = new Quaternion(0, 0, 1, 0);
		Quaternion qW = new Quaternion(0, 1, 0, 0);

		Quaternion qSlerpResult, qSlerpResultLast=null, qLerpResult, qNormLerpResult, qNormLerpResultLast=null;
		double arcLerpCurrent = 0.0f, arcSlerpCurrent = 0.0f;
		Locale.setDefault(Locale.ENGLISH);
		for (float t = 0.0f; t < 1.0f; t += 0.1f) {
			qLerpResult 	= Quaternion.lerp(qV, qW, t);
			qNormLerpResult = Quaternion.normalize(qLerpResult);
			arcLerpCurrent = (qNormLerpResultLast != null) ? 
					Math.acos(Quaternion.cosWinkel(qNormLerpResult, qNormLerpResultLast)) : 0.0f;  
			qSlerpResult 	= Quaternion.slerp(qV, qW, t);
			arcSlerpCurrent = (qSlerpResultLast != null) ? 
					Math.acos(Quaternion.cosWinkel(qSlerpResult, qSlerpResultLast)) : 0.0f;
			
			if (qSlerpResultLast != null)
				System.out.printf("t=%3.1f lerp (norm): a=%6.4f slerp: a=%6.4f \n", t, arcLerpCurrent, arcSlerpCurrent);

			qNormLerpResultLast = qNormLerpResult;
			qSlerpResultLast = qSlerpResult;
		}
		qLerpResult 	= Quaternion.lerp(qV, qW, 1.0f);
		qNormLerpResult = Quaternion.normalize(qLerpResult);
		arcLerpCurrent  = Math.acos(Quaternion.cosWinkel(qNormLerpResult, qNormLerpResultLast));  
		qSlerpResult 	= Quaternion.slerp(qV, qW, 1.0f);
		arcSlerpCurrent = Math.acos(Quaternion.cosWinkel(qSlerpResult, qSlerpResultLast));
	    System.out.printf("t=%3.1f lerp (norm): a=%6.4f slerp: a=%6.4f \n", 1.0f, arcLerpCurrent, arcSlerpCurrent);
	}
}
