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
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;

import org.lwjgl.opengl.Display;

import kapitel01.LWJGLBasisFenster;
import kapitel01.POGL;
import kapitel02.ShaderUtilities;

public class TorusAmbientDiffuseSpecularFinal extends LWJGLBasisFenster {
   private int uniform_ambient, uniform_diffuseWeight, uniform_specularWeight, uniform_specularPow,
         uniform_lightDirection;
   private JSlider sliderSpeed, sliderStep, sliderAmbient, sliderDiffuse, sliderSpecular, sliderSpecularPow;
   private final static int sliderMAXI = 100;

   public TorusAmbientDiffuseSpecularFinal(String title, int width, int height) {
      super(title, width, height);
      JFrame f = new JFrame();
      Canvas c = new Canvas();
      JPanel p = new JPanel();

      sliderSpeed = new JSlider();
      sliderSpeed.setBorder(BorderFactory.createTitledBorder("SPEED"));
      sliderSpeed.setMaximum(sliderMAXI);
      sliderSpeed.setValue(5); // 2
      sliderSpeed.setMajorTickSpacing(sliderMAXI / 5);
      sliderSpeed.setMinorTickSpacing(sliderMAXI / 20);
      sliderSpeed.setPaintTicks(true);
      sliderSpeed.setPaintLabels(true);
      sliderSpeed.setOrientation(JSlider.HORIZONTAL);

      sliderStep = new JSlider();
      sliderStep.setBorder(BorderFactory.createTitledBorder("TORUS STEPs"));
      sliderStep.setMaximum(sliderMAXI);
      sliderStep.setValue(20);
      sliderStep.setMajorTickSpacing(sliderMAXI / 5);
      sliderStep.setMinorTickSpacing(sliderMAXI / 20);
      sliderStep.setPaintTicks(true);
      sliderStep.setPaintLabels(true);
      sliderStep.setOrientation(JSlider.HORIZONTAL);

      sliderAmbient = new JSlider();
      sliderAmbient.setBorder(BorderFactory.createTitledBorder("ANBIENT LIGHT [in %]"));
      sliderAmbient.setMaximum(sliderMAXI);
      sliderAmbient.setValue(0); // 15
      sliderAmbient.setMajorTickSpacing(sliderMAXI / 5);
      sliderAmbient.setMinorTickSpacing(sliderMAXI / 20);
      sliderAmbient.setPaintTicks(true);
      sliderAmbient.setPaintLabels(true);
      sliderAmbient.setOrientation(JSlider.HORIZONTAL);

      sliderDiffuse = new JSlider();
      sliderDiffuse.setBorder(BorderFactory.createTitledBorder("DIFFUSE [in %]"));
      sliderDiffuse.setMaximum(sliderMAXI);
      sliderDiffuse.setValue(0); // 50
      sliderDiffuse.setMajorTickSpacing(sliderMAXI / 5);
      sliderDiffuse.setMinorTickSpacing(sliderMAXI / 20);
      sliderDiffuse.setPaintTicks(true);
      sliderDiffuse.setPaintLabels(true);
      sliderDiffuse.setOrientation(JSlider.HORIZONTAL);

      sliderSpecular = new JSlider();
      sliderSpecular.setBorder(BorderFactory.createTitledBorder("SPECULAR [in %]"));
      sliderSpecular.setMaximum(sliderMAXI);
      sliderSpecular.setValue(0); // 50
      sliderSpecular.setMajorTickSpacing(sliderMAXI / 5);
      sliderSpecular.setMinorTickSpacing(sliderMAXI / 20);
      sliderSpecular.setPaintTicks(true);
      sliderSpecular.setPaintLabels(true);
      sliderSpecular.setOrientation(JSlider.HORIZONTAL);

      sliderSpecularPow = new JSlider();
      sliderSpecularPow.setBorder(BorderFactory.createTitledBorder("SpecularPow"));
      sliderSpecularPow.setMaximum(sliderMAXI);
      sliderSpecularPow.setValue(0); // 0
      sliderSpecularPow.setMajorTickSpacing(sliderMAXI / 5);
      sliderSpecularPow.setMinorTickSpacing(sliderMAXI / 20);
      sliderSpecularPow.setPaintTicks(true);
      sliderSpecularPow.setPaintLabels(true);

      p.setLayout(new GridLayout(6, 1));
      p.add(sliderSpeed);
      p.add(sliderStep);
      p.add(sliderAmbient);
      p.add(sliderDiffuse);
      p.add(sliderSpecular);
      p.add(sliderSpecularPow);

      f.getContentPane().add(c);
      f.getContentPane().add(p, BorderLayout.EAST);
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
         vertexShaderSource = ShaderUtilities.readShadercode("shader/torusambientdiffusespecularfinal.vs");
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
         fragShaderSource = ShaderUtilities.readShadercode("shader/torusambientdiffusespecularfinal.fs");
      } catch (Exception e) {
         e.printStackTrace();
      }
      glShaderSource(fragShader, fragShaderSource);
      glCompileShader(fragShader);
      System.out.println(glGetShaderInfoLog(fragShader, 1024));
      glAttachShader(myProgram, fragShader);

      glLinkProgram(myProgram);
      uniform_ambient = glGetUniformLocation(myProgram, "ambient");
      uniform_diffuseWeight = glGetUniformLocation(myProgram, "diffuseWeight");
      uniform_specularWeight = glGetUniformLocation(myProgram, "specularWeight");
      uniform_specularPow = glGetUniformLocation(myProgram, "specularPow");
      uniform_lightDirection = glGetUniformLocation(myProgram, "lightDirection");
      glUseProgram(myProgram);

      ShaderUtilities.testShaderProgram(myProgram);
   }

   @Override
   public void renderLoop() {
      prepareShader();

      glEnable(GL_DEPTH_TEST);
      POGL.createTorus(0, 0, 0, 1.6, 0.5, 20.0f);
      long start = System.nanoTime();

      float[] lightpos = { 0, -1, -1 };
      glUniform3f(uniform_lightDirection, lightpos[0], lightpos[1], lightpos[2]);
      while (!Display.isCloseRequested()) {
         double now = (System.nanoTime() - start) / 1e9;
         glUniform1f(uniform_ambient, sliderAmbient.getValue() / 100.0f);
         glUniform1f(uniform_diffuseWeight, sliderDiffuse.getValue() / 100.0f);
         glUniform1f(uniform_specularWeight, sliderSpecular.getValue() / 100.0f);
         glUniform1f(uniform_specularPow, sliderSpecularPow.getValue());
         POGL.createTorus(0, 0, 0, 1.6, 0.5, sliderStep.getValue());

         glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
         POGL.clearBackgroundWithColor(1f, 1f, 1f, 1);

         glMatrixMode(GL_PROJECTION);
         glLoadIdentity();
         double r = HEIGHT * 1. / WIDTH;
         glFrustum(-1, 1, -r, r, 1, 20);
         glMatrixMode(GL_MODELVIEW);

         glLoadIdentity();

         glTranslated(0, 0.4f, -5);
         glRotatef((float) now * sliderSpeed.getValue(), 0, 0, 1);
         glRotatef((float) now * sliderSpeed.getValue() * 5.0f, 1, 0, 0);

         POGL.renderTorus();
         Display.update();
      }
      Display.destroy();
   }

   public static void main(String[] args) throws Exception {
      new TorusAmbientDiffuseSpecularFinal(
            "vividus Verlag. Dino-Buch. Kapitel 5: TorusAmbientDiffuseSpecularFinal.java", 800, 450).start();
   }
}