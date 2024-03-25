package kapitel01;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GLContext;

public class TestOpenGL {
   public static void main(String[] args) {
      try {
         Display.create();

         System.out.println("> Testing available OpenGL modes");
         if (GLContext.getCapabilities().OpenGL11)
            System.out.println("   OpenGL 1.1 supported");
         if (GLContext.getCapabilities().OpenGL12)
            System.out.println("   OpenGL 1.2 supported");
         if (GLContext.getCapabilities().OpenGL13)
            System.out.println("   OpenGL 1.3 supported");
         if (GLContext.getCapabilities().OpenGL14)
            System.out.println("   OpenGL 1.4 supported");
         if (GLContext.getCapabilities().OpenGL15)
            System.out.println("   OpenGL 1.5 supported");
         if (GLContext.getCapabilities().OpenGL20)
            System.out.println("   OpenGL 2.0 supported");
         if (GLContext.getCapabilities().OpenGL21)
            System.out.println("   OpenGL 2.1 supported");
         if (GLContext.getCapabilities().OpenGL30)
            System.out.println("   OpenGL 3.0 supported");
         if (GLContext.getCapabilities().OpenGL31)
            System.out.println("   OpenGL 3.1 supported");
         if (GLContext.getCapabilities().OpenGL32)
            System.out.println("   OpenGL 3.2 supported");
         if (GLContext.getCapabilities().OpenGL33)
            System.out.println("   OpenGL 3.3 supported");
         if (GLContext.getCapabilities().OpenGL40)
            System.out.println("   OpenGL 4.0 supported");
         if (GLContext.getCapabilities().OpenGL41)
             System.out.println("   OpenGL 4.1 supported");
         if (GLContext.getCapabilities().OpenGL42)
             System.out.println("   OpenGL 4.2 supported");
         if (GLContext.getCapabilities().OpenGL43)
             System.out.println("   OpenGL 4.3 supported");
         if (GLContext.getCapabilities().OpenGL44)
             System.out.println("   OpenGL 4.4 supported");
         if (GLContext.getCapabilities().OpenGL45)
             System.out.println("   OpenGL 4.5 supported");

         Display.destroy();
      } catch (LWJGLException e) {
         e.printStackTrace();
      }
   }
}
