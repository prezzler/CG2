package kapitel07;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_RGB8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glVertex2d;
import static org.lwjgl.opengl.GL12.GL_BGRA;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL14.GL_GENERATE_MIPMAP;

import java.awt.Canvas;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import kapitel01.LWJGLBasisFenster;

public class TextureMappingImageFile extends LWJGLBasisFenster {
   public TextureMappingImageFile(String title, int width, int height) {
      super(title, width, height);
      JFrame f = new JFrame();
      f.setTitle(title);
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      Canvas c = new Canvas();
      f.add(c);
      f.setBounds(0, 0, WIDTH, HEIGHT);
      f.setLocationRelativeTo(null);
      f.setVisible(true);
      initDisplay(c);
   }

   @Override
   public void renderLoop() {
      BufferedImage bi = null;
      try {
         bi = ImageIO.read(new File("images/trex.png"));
      } catch (IOException e) {
         e.printStackTrace();
      }
      int width = bi.getWidth(), height = bi.getHeight();
      IntBuffer pixels = BufferUtils.createByteBuffer(width * height * 4).asIntBuffer();
      pixels.put(bi.getRGB(0, 0, bi.getWidth(), bi.getHeight(), new int[bi.getWidth() * bi.getHeight()], 0, bi.getWidth()));
      pixels.rewind();

      glTexParameteri(GL_TEXTURE_2D, GL_GENERATE_MIPMAP, GL_TRUE);
      glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB8, width, height, 0, GL_BGRA, GL_UNSIGNED_BYTE, pixels);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
      glEnable(GL_TEXTURE_2D);

      long start = System.nanoTime();
      while (!Display.isCloseRequested()) {
         double elapsedTime = (System.nanoTime() - start) / 1e9, t = elapsedTime;
         glClear(GL_COLOR_BUFFER_BIT);
         glLoadIdentity();
         glRotatef((float) (elapsedTime * 20), 0, 0, 1);

         double s = Math.sin(t) * .5 + 0.9;
         glScaled(s, s, s);

         glBegin(GL_QUADS);
         glTexCoord2f(0, 1);
         glVertex2d(-.5, -.5);
         glTexCoord2f(1, 1);
         glVertex2d(.5, -.5);
         glTexCoord2f(1, 0);
         glVertex2d(.5, .5);
         glTexCoord2f(0, 0);
         glVertex2d(-.5, .5);
         glEnd();

         Display.update();
      }
      Display.destroy();
      System.exit(0);
   }

   public static void main(String[] args) throws LWJGLException, IOException {
      new TextureMappingImageFile(
            "vividus Wissenschaftsverlag. Warum sich der Dino furchtbar erschreckte. Kapitel 7: TextureMappingImageFile.java",
            800, 450).start();
   }
}