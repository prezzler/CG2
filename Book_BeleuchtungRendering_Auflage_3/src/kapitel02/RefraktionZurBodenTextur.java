package kapitel02;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_COLOR;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_SRC_COLOR;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL12.GL_BGRA;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_RGB32F;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;

import java.awt.Canvas;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import kapitel01.LWJGLBasisFenster;
import kapitel01.POGL;

public class RefraktionZurBodenTextur extends LWJGLBasisFenster {
	public static String fragShaderWaterdynamic = "" 
		+ "#version 130\n"
		+ ""
		+ "uniform sampler2D tex1;" 
		+ "uniform sampler2D tex2;"
		+ "uniform vec2 s;" 
		+ "" 
		+ "void main (void) {" 
		+ "	gl_FragColor = normalize(("
		+ "		texture2D(tex1, gl_TexCoord[0].st+vec2(+s.x,0)) +"
		+ "		texture2D(tex1, gl_TexCoord[0].st+vec2(-s.x,0)) +"
		+ "		texture2D(tex1, gl_TexCoord[0].st+vec2(0,+s.y)) +"
		+ "		texture2D(tex1, gl_TexCoord[0].st+vec2(0,-s.y))"
		+ "		) * 0.5 - texture2D(tex2, gl_TexCoord[0].st)) * 0.998; " 
		+ "}";

	public static String fragShaderWaterDynamicVisualisation = "" 
		+ "#version 130\n"
		+ ""
		+ "uniform sampler2D tex1;" 
		+ "uniform sampler2D texT;" 
		+ ""
		+ "uniform vec2 s;" 
		+ "uniform float scale;" 
		+ "" 
		+ "void main (void) {"
		+ "	   float g_x = (texture2D(tex1, gl_TexCoord[0].st + vec2(-s.x, 0)) - "
		+ "	   			 texture2D(tex1, gl_TexCoord[0].st + vec2(+s.x, 0))).x;"
		+ "	   float g_y = (texture2D(tex1, gl_TexCoord[0].st + vec2(0, -s.y)) - "
		+ "	   			 texture2D(tex1, gl_TexCoord[0].st + vec2(0, +s.y))).y;" 
		+ "	"
		+ "	   vec3 n_surface 	= normalize(cross(vec3(1.0, 0.0, g_x), vec3(0.0, 1.0, g_y)));"
		+ "	   vec3 n_view 	= vec3(0.0, 0.0, 1.0);" 
		+ "	   vec3 refractVec	= refract(n_view, n_surface, 1.33);"
		+ "" 
		+ "	   gl_FragColor 	= texture2D(texT, gl_TexCoord[0].st + refractVec.xy * s * scale);" 
		+ "}";

	final int WB = WIDTH/4, HB = HEIGHT/4;
	
	private int waterTexture1, waterTexture1FB;
	private int waterTexture2, waterTexture2FB;
	private int waterTexture3, waterTexture3FB; 
	private int gaussTexture;
	private int groundTexture;
	private int programWaterdynamic, programWaterdynamivVisualisation;
	private int uniform_fragShader_waterDynamic_s, uniform_fragShader_waterDynamic_tex1, uniform_fragShader_waterDynamic_tex2;
	private int uniform_programWaterdynamivVisualisation_s, uniform_programWaterdynamivVisualisation_scale;
	private int uniform_programWaterdynamivVisualisation_tex1, uniform_programWaterdynamivVisualisation_texT;
	
	public RefraktionZurBodenTextur(String title, int width, int height) {
        super(title, width, height);
		
		JFrame f = new JFrame();
		Canvas c = new Canvas();

		f.getContentPane().add(c);
		f.setBounds(0, 0, WIDTH, HEIGHT);
		f.setTitle(title);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		initDisplay(c);
	}
	
	public void prepareDreiRotierendeFrameBuffer() {
		// Für die Dynamik des Wassers benötigen wir zunächst drei FrameBuffer,
		// die ihre Plätze im Laufe der Iterationen zyklisch tauschen
		waterTexture1 = glGenTextures();
		waterTexture1FB = glGenFramebuffers();
		glBindTexture(GL_TEXTURE_2D, waterTexture1);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, WB, HB, 0, GL_BGRA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glBindFramebuffer(GL_FRAMEBUFFER, waterTexture1FB);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, waterTexture1, 0);

		waterTexture2 = glGenTextures();
		waterTexture2FB = glGenFramebuffers();
		glBindTexture(GL_TEXTURE_2D, waterTexture2);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, WB, HB, 0, GL_BGRA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glBindFramebuffer(GL_FRAMEBUFFER, waterTexture2FB);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, waterTexture2, 0);

		waterTexture3 = glGenTextures();
		waterTexture3FB = glGenFramebuffers();
		glBindTexture(GL_TEXTURE_2D, waterTexture3);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, WB, HB, 0, GL_BGRA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glBindFramebuffer(GL_FRAMEBUFFER, waterTexture3FB);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, waterTexture3, 0);

		// wir wollen die drei FrameBuffer (die in drei Texturen schreiben) allerdings
		// "unsichtbar" im Hintergrund füllen und erst im letzten Schritt ausgewählte Inhalte
		// anzeigen
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glBindTexture(GL_TEXTURE_2D, 0); // steht nur hier, damit die Blöcke austauschbar bleiben
	}
	
	public void prepareGroundTexture() {
		File file = new File("images/steine_kaustik.jpg");
		final int WU 	= 640;
		final int HU 	= 480;			
		
		groundTexture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, groundTexture);			
		try {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, WU, HU, 0, GL_BGRA, GL_UNSIGNED_BYTE,
				(IntBuffer)ByteBuffer.allocateDirect(WU*HU*4).order(ByteOrder.nativeOrder()).asIntBuffer().put(
				ImageIO.read(file).getRGB(0,0, WU, HU, new int[WU*HU], 0, WU)).rewind());
		} catch (IOException e) {
			e.printStackTrace();
		}
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);					
		
		glBindTexture(GL_TEXTURE_2D, 0);  		// steht nur hier, damit die Blöcke austauschbar bleiben
	}
	
	public void prepareShaderWaterEffect() {
		programWaterdynamic = glCreateProgram();

		int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragShader, fragShaderWaterdynamic);
		glCompileShader(fragShader);
		System.out.println(glGetShaderInfoLog(fragShader, 1024));
		glAttachShader(programWaterdynamic, fragShader);

		glLinkProgram(programWaterdynamic);
		uniform_fragShader_waterDynamic_s = glGetUniformLocation(programWaterdynamic, "s");
		uniform_fragShader_waterDynamic_tex1 = glGetUniformLocation(programWaterdynamic, "tex1");
		uniform_fragShader_waterDynamic_tex2 = glGetUniformLocation(programWaterdynamic, "tex2");
		glUseProgram(programWaterdynamic);
		
		glUniform2f(uniform_fragShader_waterDynamic_s, 1.0f / WB, 1.0f / HB);
		glUniform1i(uniform_fragShader_waterDynamic_tex1, 1);
		glUniform1i(uniform_fragShader_waterDynamic_tex2, 0);
		
		ShaderUtilities.testShaderProgram(programWaterdynamic);
	}
	
	public void prepareShaderVisualisierung() {
		programWaterdynamivVisualisation = glCreateProgram();

		int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragShader, fragShaderWaterDynamicVisualisation);
		glCompileShader(fragShader);
		System.out.println(glGetShaderInfoLog(fragShader, 1024));
		glAttachShader(programWaterdynamivVisualisation, fragShader);

		glLinkProgram(programWaterdynamivVisualisation);
		uniform_programWaterdynamivVisualisation_s 		= glGetUniformLocation(programWaterdynamivVisualisation, "s");
		uniform_programWaterdynamivVisualisation_scale 	= glGetUniformLocation(programWaterdynamivVisualisation, "scale");
		uniform_programWaterdynamivVisualisation_tex1 	= glGetUniformLocation(programWaterdynamivVisualisation, "tex1");
		uniform_programWaterdynamivVisualisation_texT 	= glGetUniformLocation(programWaterdynamivVisualisation, "texT");
		glUseProgram(programWaterdynamivVisualisation);
		
		glUniform2f(uniform_programWaterdynamivVisualisation_s, 1.0f/WB, 1.0f/HB);			
		glUniform1f(uniform_programWaterdynamivVisualisation_scale, -100.0f);
		glUniform1i(uniform_programWaterdynamivVisualisation_tex1, 0);
		glUniform1i(uniform_programWaterdynamivVisualisation_texT, 1);
		
		ShaderUtilities.testShaderProgram(programWaterdynamivVisualisation);
	}
	
	@Override
	public void renderLoop() {
		prepareDreiRotierendeFrameBuffer();
		gaussTexture = ShaderUtilities.prepareGaussTexture();
		prepareGroundTexture();
		
		prepareShaderWaterEffect();
		prepareShaderVisualisierung();

		while (!Display.isCloseRequested()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// wir tauschen die Framebuffer (und Texturen!) gegen den Uhrzeigersinn aus
			int helpFB 			= waterTexture1FB;
			int helpT  			= waterTexture1;
			waterTexture1FB 	= waterTexture2FB;
			waterTexture1 		= waterTexture2;
			waterTexture2FB 	= waterTexture3FB;
			waterTexture2 		= waterTexture3;
			waterTexture3FB 	= helpFB;
			waterTexture3 		= helpT;

			// **************************************
			// Wenn die Maustaste gedrückt wurde, wollen wir die Gauss-Verteilung, die wir
			// in der gaussTexture erzeugt haben, verwenden, um diese in die Wasserdynamik
			// einfließen zu lassen. Schwarze Werte werden dabei durch die additive
			// Verknüpfung
			// vernachlässigt und lassen sich daher als transparent interpretieren.
			if (Mouse.isButtonDown(0)) {
				glBindFramebuffer(GL_FRAMEBUFFER, waterTexture2FB);
				glUseProgram(0);

				glViewport(0, 0, WB, HB);
				glLoadIdentity();
				glTranslated(Mouse.getX() * 2f / WIDTH - 1, Mouse.getY() * 2f / HEIGHT - 1, 0);
				glScaled(0.1, 0.1, 0.1); // wenn Bild kleiner, dann 0.2

				// Durch das Blenden manipulieren wir die Wasserdynamik mit der Gauss-Verteilung
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_COLOR, GL_ONE_MINUS_SRC_COLOR);

				glEnable(GL_TEXTURE_2D);
				glBindTexture(GL_TEXTURE_2D, gaussTexture);
				glColor3f(1, 1, 1);
				
				POGL.renderViereckMitTexturbindung();
				
				glDisable(GL_TEXTURE_2D);
				glDisable(GL_BLEND);
			}
			// **************************************

			// **************************************
			// Der Shader WasserDynamik erzeugt aus den ersten beiden
			// Texturen (waterTexture_1 und waterTexture_2) die Dynamik
			// des Wassers und speichert diese in FrameBuffer
			// (waterTexture_3_FBO). Damit steht es später in der Texture
			// waterTexture_3 zur Verfügung.
			glBindFramebuffer(GL_FRAMEBUFFER, waterTexture3FB);
			glUseProgram(programWaterdynamic);
			glViewport(0, 0, WB, HB);

			glClearColor(0, 0, 0, 1);
			glClear(GL_COLOR_BUFFER_BIT);
			glLoadIdentity();

			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, waterTexture1);
			glActiveTexture(GL_TEXTURE1);
			glBindTexture(GL_TEXTURE_2D, waterTexture2);

			POGL.renderViereckMitTexturbindung();

			glActiveTexture(GL_TEXTURE1);
			glBindTexture(GL_TEXTURE_2D, 0);
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, 0);
			// **************************************

			// **************************************
			// Die Wasserdynamik wird jetzt von Frame zu
			// Frame berechnet und das im nicht-sichtbaren
			// Bereich. Um das Ganze jetzt anschaulich zu
			// machen verwenden wir den FrameBuffer, der den
			// Ausgabebereich definiert, in unserem Fall das
			// erzeugte Canvasobjekt im JFrame.
			glBindFramebuffer(GL_FRAMEBUFFER, 0);				
			glUseProgram(programWaterdynamivVisualisation);					
			glViewport(0, 0, WIDTH, HEIGHT);

			glEnable(GL_TEXTURE_2D);
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, waterTexture3);
			glActiveTexture(GL_TEXTURE1);
			glBindTexture(GL_TEXTURE_2D, groundTexture);
			
			POGL.renderViereckMitTexturbindung();
			glDisable(GL_TEXTURE_2D);
			
			// Gebundene Texturen in der umgekehrten Reihenfolge
			// frei geben.
			glActiveTexture(GL_TEXTURE1);
			glBindTexture(GL_TEXTURE_2D, 0);
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, 0);

			glUseProgram(0);
			// **************************************

			Display.update();
		}
		Display.destroy();
	}
	
	public static void main(String[] args) throws LWJGLException {
		new RefraktionZurBodenTextur("vividus Verlag. Dino-Buch. Kapitel 2: RefraktionZurBodenTextur.java", 800, 450).start();
	}
}
