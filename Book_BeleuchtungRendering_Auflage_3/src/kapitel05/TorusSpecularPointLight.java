package kapitel05;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFrustum;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.awt.BorderLayout;
import java.awt.Canvas;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JSlider;

import org.lwjgl.opengl.Display;

import kapitel01.LWJGLBasisFenster;
import kapitel01.POGL;
import kapitel02.ShaderUtilities;

public class TorusSpecularPointLight extends LWJGLBasisFenster {
   private int specularPercent, specularPow;
   private JSlider slider, sliderPow;
   private final static int sliderMAXI = 100;

   public TorusSpecularPointLight(String title, int width, int height) {
      super(title, width, height);
      JFrame f = new JFrame();
      Canvas c = new Canvas();

      slider = new JSlider();
      slider.setBorder(BorderFactory.createTitledBorder("Specular Weight"));
      slider.setMaximum(sliderMAXI);
      slider.setValue(50);
      slider.setMajorTickSpacing(sliderMAXI / 5);
      slider.setMinorTickSpacing(sliderMAXI / 20);
      slider.setPaintTicks(true);
      slider.setPaintLabels(true);

      sliderPow = new JSlider();
      sliderPow.setBorder(BorderFactory.createTitledBorder("SpecularPow"));
      sliderPow.setMaximum(sliderMAXI);
      sliderPow.setValue(10);
      sliderPow.setMajorTickSpacing(sliderMAXI / 5);
      sliderPow.setMinorTickSpacing(sliderMAXI / 20);
      sliderPow.setPaintTicks(true);
      sliderPow.setPaintLabels(true);

      f.getContentPane().add(c);
      f.getContentPane().add(slider, BorderLayout.NORTH);
      f.getContentPane().add(sliderPow, BorderLayout.SOUTH);
      f.setBounds(0, 0, WIDTH, HEIGHT);
      f.setTitle(title);
      f.setLocationRelativeTo(null);
      f.setVisible(true);
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      initDisplay(c);
   }

   private void prepareShader() {
      int myProgram = glCreateProgram();

      int vertShader = glCreateShader(GL_VERTEX_SHADER);
      String vertexShaderSource = "";
      try {
         vertexShaderSource = ShaderUtilities.readShadercode("shader/torusspecularpointlight.vs");
      } catch (Exception e) {
         e.printStackTrace();
      }
      glShaderSource(vertShader, vertexShaderSource);
      glCompileShader(vertShader);
      System.out.println(glGetShaderInfoLog(vertShader, 1024));
      glAttachShader(myProgram, vertShader);

      int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
      String fragShaderSource = "";
      try {
         fragShaderSource = ShaderUtilities.readShadercode("shader/torusspecularpointlight.fs");
      } catch (Exception e) {
         e.printStackTrace();
      }
      glShaderSource(fragShader, fragShaderSource);
      glCompileShader(fragShader);
      System.out.println(glGetShaderInfoLog(fragShader, 1024));
      glAttachShader(myProgram, fragShader);

      glLinkProgram(myProgram);
      specularPercent = glGetUniformLocation(myProgram, "specularWeight");
      specularPow = glGetUniformLocation(myProgram, "specularPow");
      glUseProgram(myProgram);

      ShaderUtilities.testShaderProgram(myProgram);
   }

   @Override
   public void renderLoop() {
      prepareShader();

      glEnable(GL_DEPTH_TEST);
      POGL.createTorus(0, 0, 0, 1, 0.3, 20.0f);
      long start = System.nanoTime();
      while (!Display.isCloseRequested()) {
         double now = (System.nanoTime() - start) / 1e9;
         glUniform1f(specularPercent, slider.getValue() / 100.0f);
         glUniform1f(specularPow, sliderPow.getValue());

         glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

         glMatrixMode(GL_PROJECTION);
         glLoadIdentity();
         double r = HEIGHT * 1. / WIDTH;
         glFrustum(-1, 1, -r, r, 1, 20);
         glMatrixMode(GL_MODELVIEW);
         glLoadIdentity();
         glTranslated(0, 0, -4);
         glRotatef((float) now * 4.0f, 0, 0, 1);
         glRotatef(10 * (float) now * 4.0f, 1, 0, 0);

         POGL.renderTorus();
         Display.update();
      }
      Display.destroy();
   }

   public static void main(String[] args) throws Exception {
      new TorusSpecularPointLight("vividus Verlag. Dino-Buch. Kapitel 5: TorusSpecularPointLight.java", 800,
            450).start();
   }
}