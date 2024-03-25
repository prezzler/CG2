package kapitel01;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class TestLWJGL {
   public static void main(String[] args) {
      try {
         Display.create();
         Thread.sleep(5000);
      } catch (LWJGLException e) {
         e.printStackTrace();
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }
}
