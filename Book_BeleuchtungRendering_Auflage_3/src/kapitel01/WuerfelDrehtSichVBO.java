package kapitel01;

import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glColorPointer;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glFrustum;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.awt.Canvas;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.swing.JFrame;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;

public class WuerfelDrehtSichVBO extends LWJGLBasisFenster {
   private int vboiId;
   private int indicesCount;
   private int vaoId;
   private int vboId;
   private int vbocId;
   private int VERTEX_POS   = 0;
   private int COLOR_POS    = 1;
   private int VERTEX_DIM   = 3;
   private int COLOR_DIM    = 4;

   public WuerfelDrehtSichVBO(String title, int width, int height) {
      super(title, width, height);

      JFrame f = new JFrame();
      f.setTitle(title);
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      Canvas c = new Canvas();
      f.add(c);
      f.setBounds(100, 100, 600, 600);
      f.setLocationRelativeTo(null);
      f.setVisible(true);
      

      initDisplay(c);
   }

   private void createWuerfel() {
      float[] vertices = { 
            -1, -1, -1,     // 0
             1, -1, -1,     // 1
             1,  1, -1,     // 2
            -1,  1, -1,     // 3
            -1, -1,  1,     // 4
             1, -1,  1,     // 5
             1,  1,  1,     // 6
            -1,  1,  1};    // 7

      byte[] indices = { 
            3, 7, 6, 2,     // top
            3, 0, 4, 7,     // left
            0, 4, 5, 1,     // bottom
            1, 2, 6, 5,     // right
            4, 5, 6, 7,     // front
            0, 3, 2, 1};    // back

      float[] colors = { 0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1,
            1, };

      FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
      verticesBuffer.put(vertices);
      verticesBuffer.flip();

      indicesCount = indices.length;
      ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indicesCount);
      indicesBuffer.put(indices);
      indicesBuffer.flip();

      FloatBuffer colorsBuffer = BufferUtils.createFloatBuffer(colors.length);
      colorsBuffer.put(colors);
      colorsBuffer.flip();

      vaoId = glGenVertexArrays();
      glBindVertexArray(vaoId);

      vboId = glGenBuffers();
      glBindBuffer(GL_ARRAY_BUFFER, vboId);
      glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
      glEnableVertexAttribArray(VERTEX_POS);
      glVertexAttribPointer(VERTEX_POS, VERTEX_DIM, GL_FLOAT, false, 0, 0);
      glVertexPointer(VERTEX_DIM, GL_FLOAT, 0, 0);
      glBindBuffer(GL_ARRAY_BUFFER, 0);

      vboiId = glGenBuffers();
      glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboiId);
      glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
      glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

      vbocId = glGenBuffers();
      glBindBuffer(GL_ARRAY_BUFFER, vbocId);
      glBufferData(GL_ARRAY_BUFFER, colorsBuffer, GL_DYNAMIC_DRAW);
      glEnableVertexAttribArray(COLOR_DIM);
      glVertexAttribPointer(COLOR_POS, COLOR_DIM, GL_FLOAT, false, 0, 0);
      glColorPointer(COLOR_DIM, GL_FLOAT, 0, 0);

      glBindBuffer(GL_ARRAY_BUFFER, 0);
      glBindVertexArray(0);
   }

   private void render() {
      glBindVertexArray(vaoId);
      glEnableVertexAttribArray(VERTEX_POS);
      glEnableVertexAttribArray(COLOR_POS);
      glEnableClientState(GL_VERTEX_ARRAY);
      glEnableClientState(GL_COLOR_ARRAY);
      glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboiId);

      glDrawElements(GL_QUADS, indicesCount, GL_UNSIGNED_BYTE, 0);

      glDisableVertexAttribArray(VERTEX_POS);
      glDisableVertexAttribArray(COLOR_POS);
      glDisableClientState(GL_VERTEX_ARRAY);
      glDisableClientState(GL_COLOR_ARRAY);
      glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
      glBindVertexArray(0);
   }

   @Override
   public void renderLoop() {
      glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
      long start = System.nanoTime();

      createWuerfel();
      while (!Display.isCloseRequested()) {
         double t = (System.nanoTime()-start)/1e9;
         POGL.clearBackgroundWithColor(0.7f, 0.7f, 0.7f, 1.0f);
 
         glLoadIdentity();
         glFrustum(-1, 1, -1, 1, 4, 10);
         glTranslated(0, 0, -5);
         glRotatef((float)t*100.0f, 1.0f, 0.0f, 0.0f);
         glRotatef((float)t*100.0f, 0.0f, 1.0f, 0.0f);
         glScaled(0.5, 0.5, 0.5);
         glLineWidth(2.0f);
         render();

         Display.update();
      }
   }

   public static void main(String[] args) {
      new WuerfelDrehtSichVBO("vividus Verlag. Dino-Buch. Kap. 1: WuerfelDrehtSichVBO.java", 500, 500).start();
   }
}