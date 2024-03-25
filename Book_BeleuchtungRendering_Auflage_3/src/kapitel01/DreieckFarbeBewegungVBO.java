package kapitel01;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.GL_DOUBLE;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.io.IOException;
import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class DreieckFarbeBewegungVBO extends LWJGLBasisFensterEigenesCanvas {
   public DreieckFarbeBewegungVBO(String title, int width, int height) {
      super(title, width, height);
   }

   private void clearBackground(float r, float g, float b, float a) {
      glClearColor(r, g, b, a);
      glClear(GL_COLOR_BUFFER_BIT);
   }

   private int createTriangle(double t) {
      double[] vertices = {
            -0.5, -0.5 + 0.3*Math.sin(t), 0, 
             0.5,  0.3*Math.sin(t*1.3), 0, 
             0.3*Math.sin(t*2), 0.5, 0};

      DoubleBuffer verticesBuffer = BufferUtils.createDoubleBuffer(vertices.length);
      verticesBuffer.put(vertices);
      verticesBuffer.flip();

      int vaoId = glGenVertexArrays();
      glBindVertexArray(vaoId);

      int vboId = glGenBuffers();
      glBindBuffer(GL_ARRAY_BUFFER, vboId);
      glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_DYNAMIC_DRAW);
      glVertexAttribPointer(0, 3, GL_DOUBLE, false, 0, 0);
      glBindBuffer(GL_ARRAY_BUFFER, 0);

      glBindVertexArray(0);
      return vaoId;
   }

   private void render(int vaoId) {
      glBindVertexArray(vaoId);
      glEnableVertexAttribArray(0);
      
      glDrawArrays(GL_TRIANGLES, 0, 3);
      
      glDisableVertexAttribArray(0);
      glBindVertexArray(0);
   }

   @Override
   public void renderLoop() {
      while (!Display.isCloseRequested()) {
         double t = System.nanoTime() / 1e9;
         clearBackground(0.95f, 0.95f, 0.95f, 1.0f);
         glColor3d(0.35 + 0.30*Math.sin(t), 0.63 + 0.24*Math.sin(t), 0.73 + 0.22*Math.sin(t));
         int vaoId = createTriangle(t);
         render(vaoId);
         Display.update();
      }
   }

   public static void main(String[] args) throws LWJGLException, IOException {
      new DreieckFarbeBewegungVBO("Kapitel 1: DreieckFarbeBewegungVBO.java", 800, 450).start();
   }
}
