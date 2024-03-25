package kapitel10;

import java.util.Locale;

public class SlerpInAction {
   public static void main(String[] args) {
      // Buchbeispiel
      //Quaternion qV = new Quaternion(0, 0, 1, 1);
      //Quaternion qW = new Quaternion(0, 1, 1, 0);
      
      Quaternion qV = new Quaternion(0, 0, 1, 0);
      Quaternion qW = new Quaternion(0, 1, 0, 0);
      
      Quaternion qResult;
      Locale.setDefault(Locale.ENGLISH);
      for (float t=0.0f; t<1.0f; t+=0.1f) {
         qResult = Quaternion.slerp(qV, qW, t);
         System.out.printf("t=%3.1f l=%4.2f q=%s\n", t, qResult.length(), qResult);
         //System.out.println("t="+t+" length="+qResult.length()+" qR="+qResult);
      }
      qResult = Quaternion.slerp(qV, qW, 1.0f);
      System.out.printf("t=%3.1f l=%4.2f q=%s\n", 1.0f, qResult.length(), qResult);
   }
}
