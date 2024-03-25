package kapitel10;

import kapitel04.Vektor3D;

public class QuaternionTest {
   public static void main(String[] args) {
      Vektor3D v = new Vektor3D(0, 1, 1);
      Vektor3D r = new Vektor3D(0, 1, 0);
      float winkel = 90.0f;
      
      int durchlaeufe = 1_000_000;
      
      Quaternion qV2 = null;
      long startZeit = System.currentTimeMillis();
      for (int i=1; i<durchlaeufe; i++)
    	   qV2 = Quaternion.rotiere(v, r, winkel);
      long stopZeit  = System.currentTimeMillis();
      System.out.println("Dauer "+(stopZeit-startZeit)+" ms mit Quaternionsalgebra");
      System.out.println("Ergebnis: "+qV2);
      
      startZeit = System.currentTimeMillis();
      for (int i=1; i<durchlaeufe; i++)
    	  qV2 = Quaternion.rotiereFast(v, r, winkel);      
      stopZeit  = System.currentTimeMillis();
      System.out.println("Dauer "+(stopZeit-startZeit)+" ms mit Quaternionsalgebra (fast)");
      System.out.println("Ergebnis: "+qV2);

      startZeit = System.currentTimeMillis();
      for (int i=1; i<durchlaeufe; i++)
    	  qV2 = Quaternion.rotiereMatrixEJML(v, r, winkel);      
      stopZeit  = System.currentTimeMillis();
      System.out.println("Dauer "+(stopZeit-startZeit)+" ms mit Matrizendarstellung (EJML)");
      System.out.println("Ergebnis: "+qV2);
      
      startZeit = System.currentTimeMillis();
      for (int i=1; i<durchlaeufe; i++)
    	  qV2 = Quaternion.rotiereMatrixFast(v, r, winkel);      
      stopZeit  = System.currentTimeMillis();
      System.out.println("Dauer "+(stopZeit-startZeit)+" ms mit Matrizendarstellung (fast)");
      System.out.println("Ergebnis: "+qV2);
   }
}
