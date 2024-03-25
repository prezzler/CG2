package kapitel02.loesungen;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFrustum;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScaled;
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

public class ModelViewProjectionMatrix extends LWJGLBasisFenster {
	private static String vertexShaderSource = ""
		+ "#version 130\n"
		+ ""
		+ "void main() {"
		+ "   gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;" 
		+ "}";

	private static String fragShaderSource = ""
		+ "#version 130\n"
		+ ""
		+ "void main() {" 
		+ "   gl_FragColor = vec4(1, 1, 0, 1);" 
		+ "}";

	public ModelViewProjectionMatrix() {
		super("Rotierendes Quad über Shader realisiert", 800, 600);
		initDisplay();
	}

	private void prepareShader() {
		int myProgram = glCreateProgram();

		int vertShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertShader, vertexShaderSource);
		glCompileShader(vertShader);
		System.out.println(glGetShaderInfoLog(vertShader, 1024));
		glAttachShader(myProgram, vertShader);

		int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragShader, fragShaderSource);
		glCompileShader(fragShader);
		System.out.println(glGetShaderInfoLog(fragShader, 1024));
		glAttachShader(myProgram, fragShader);

		glLinkProgram(myProgram);
		glUseProgram(myProgram);
		
		ShaderUtilities.testShaderProgram(myProgram);
	}

	@Override
	public void renderLoop() {
		prepareShader();

		glEnable(GL_DEPTH_TEST);
		long start = System.nanoTime();
		while (!Display.isCloseRequested()) {
			double now = (System.nanoTime() - start) / 1e9;
			POGL.setBackGroundColorClearDepth(0.0f, 0.0f, 0.0f);

			glLoadIdentity();
			double r = (double)HEIGHT/WIDTH;
			glFrustum(-1, 1, -r, r, 1, 20);
			glTranslated(0, 0, -3);
			glRotatef((float) now * 5.0f, 0, 0, 1);
			glRotatef(10 * (float) now * 5.0f, 1, 0, 0);
			glScaled(1.0f, 1.0f, 1.0f);

			POGL.renderViereck();
			Display.update();
		}
	}

	public static void main(String[] args) throws Exception {
		new ModelViewProjectionMatrix().start();
	}
}


