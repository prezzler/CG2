package kapitel01;

import jcuda.Pointer;
import jcuda.runtime.JCuda;

public class TestCUDA {
   public static void main(String args[])
   {
       Pointer pointer = new Pointer();
       try {
          JCuda.cudaMalloc(pointer, 4);
       } catch(Exception e) {
          System.out.println("no CUDA available");
       }
       System.out.println("Pointer: "+pointer);
       JCuda.cudaFree(pointer);
   }
}
