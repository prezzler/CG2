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
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;

import org.lwjgl.opengl.Display;

import kapitel01.LWJGLBasisFenster;
import kapitel01.POGL;
import kapitel02.ShaderUtilities;

public class QuadAmbientMaterial extends LWJGLBasisFenster {
	public QuadAmbientMaterial(String title, int width, int height) {
        super(title, width, height);
		initDisplay();
	}

	private void prepareShader() {
		int myProgram = glCreateProgram();
		int vertShader = glCreateShader(GL_VERTEX_SHADER);
		String vertexShaderSource = "";
		try {
			vertexShaderSource = ShaderUtilities.readShadercode("shader/quadambientmaterial.vs");
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
			fragShaderSource = ShaderUtilities.readShadercode("shader/quadambientmaterial.fs");
		} catch (Exception e) {
			e.printStackTrace();
		}
		glShaderSource(fragShader, fragShaderSource);
		glCompileShader(fragShader);
		System.out.println(glGetShaderInfoLog(fragShader, 1024));
		glAttachShader(myProgram, fragShader);

		glLinkProgram(myProgram);
		glUseProgram(myProgram);
		
		// shadercode testen und Fehlermeldung ausgeben
		ShaderUtilities.testShaderProgram(myProgram);
	}

	@Override
	public void renderLoop() {
		prepareShader();
		
		glEnable(GL_DEPTH_TEST);
		long start = System.nanoTime();
		while (!Display.isCloseRequested()) {
			double now = (System.nanoTime() - start) / 1e9;

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			double r = HEIGHT * 1. / WIDTH;
			glFrustum(-1, 1, -r, r, 1, 20);
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			glTranslated(0, 0, -4);
			glRotatef((float) now * 5.0f, 0, 0, 1);
			glRotatef(10 * (float) now * 5.0f, 1, 0, 0);

			POGL.renderViereck();
			Display.update();
		}
		Display.destroy();
	}

	public static void main(String[] args) throws Exception {
		new QuadAmbientMaterial("vividus Verlag. Dino-Buch. Kapitel 5: QuadAmbientMaterial.java", 800, 450).start();
	}
}