package kapitel08;

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
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.GL_TEXTURE3;
import static org.lwjgl.opengl.GL13.GL_TEXTURE4;
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
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_RGB32F;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import kapitel01.LWJGLBasisFenster;
import kapitel01.POGL;
import kapitel02.MusicPlayer;
import kapitel02.ShaderUtilities;

public class InteraktiveWassersimulationNormalMapping extends LWJGLBasisFenster {
	public static String fragShaderWaterdynamic = "" 
		+ "#version 130\n"
		+ ""
		+ "uniform sampler2D tex1;" 
		+ "uniform sampler2D tex2;"
		+ "uniform vec2 s;"
		+ "uniform float D;" 
		+ "" 
		+ "void main (void) {" 
		+ "	gl_FragColor = normalize(("
		+ "		texture2D(tex1, gl_TexCoord[0].st+vec2(+s.x,0)) +"
		+ "		texture2D(tex1, gl_TexCoord[0].st+vec2(-s.x,0)) +"
		+ "		texture2D(tex1, gl_TexCoord[0].st+vec2(0,+s.y)) +"
		+ "		texture2D(tex1, gl_TexCoord[0].st+vec2(0,-s.y))"
		+ "		) * 0.5 - texture2D(tex2, gl_TexCoord[0].st)) * D; " 
		+ "}";
	
	public static String fragShader_caustic = "" 
		+ "#version 130\n"
		+ ""
		+ "uniform sampler2D tex1;" 
		+ "uniform sampler2D tex3;" 
		+ "uniform vec2 s;" 
		+ "uniform float scale;" 
		+ "" 
		+ "void main (void) {"
		+ "	  vec2 myPos 			= gl_TexCoord[0].st;"
		+ "	  float g_x 			= (texture2D(tex1, myPos + vec2(-s.x, 0)) - texture2D(tex1, myPos + vec2(+s.x, 0))).x;"
		+ "	  float g_y 			= (texture2D(tex1, myPos + vec2(0, -s.y)) - texture2D(tex1, myPos + vec2(0, +s.y))).y;"
		+ "" 
		+ "	  vec3 n_surface 		= normalize(cross(vec3(1, 0, g_x), vec3(0, 1, g_y))); "
		+ "	  vec3 n_view 			= vec3(0, 0, 1);"
		+ "	  vec3 refractVector	= refract(n_view, n_surface, 1.33);" 
		+ "	  vec4 caustic			= texture2D(tex3, 10.0 * refractVector.xy + vec2(0.5, 0.5));" 
		+ ""
		+ "	  gl_FragColor 			= caustic;" 
		+ "}";

	public static String fragShaderWaterDynamicVisualisation = "" 
		+ "#version 130\n"
		+ ""
		+ "uniform sampler2D tex1;" 
		+ "uniform sampler2D tex2;"
		+ "uniform sampler2D tex3;" 
		+ "uniform sampler2D tex4;" 
		+ "uniform sampler2D tex5;" 
		+ ""
		+ "uniform bool withRefraction;" 
		+ "uniform vec4 w;" 
		+ "uniform vec2 s;" 
		+ "uniform float scale;"
		+ "uniform float offSetCloud;" 
		+ "" 
		+ "void main (void) {" 
		+ "	vec2 myPos 			= gl_TexCoord[0].st;"
		+ "	float g_x 			= (texture2D(tex1, myPos + vec2(-s.x, 0)) - texture2D(tex1, myPos + vec2(+s.x, 0))).x;"
		+ "	float g_y 			= (texture2D(tex1, myPos + vec2(0, -s.y)) - texture2D(tex1, myPos + vec2(0, +s.y))).y;"
		+ "" 
		+ "	vec3 n_surface 		= normalize(cross(vec3(1, 0, g_x), vec3(0, 1, g_y))); "
		+ "	vec3 n_view 		= vec3(0, 0, 1);"
		+ "	vec3 refractVector	= refract(n_view, n_surface, 1.33);"
		+ "	vec3 reflectVector	= reflect(n_view, n_surface);" 
		+ ""
		+ "	float shadow   		= 1 + 10.0 * (g_x-g_y);"
		+ "	vec4 shadowCol 		= (1 - w.w) * vec4(1 , 1, 1, 1) + w.w * vec4(shadow, shadow, shadow, 1);"
		+ "" 
		+ "	float maxL			= 0.025;"
		+ "	float weight		= (maxL - length(vec2(g_x, g_y))) / maxL;"
		+ "	vec4 wolkenRefl		= vec4((weight * (texture2D(tex5, myPos + vec2(offSetCloud * s.x, 0)  + reflectVector.xy))).rgb, 1);"
		+ "" 
		+ "	vec4 mondRefl		= texture2D(tex3, 5 * reflectVector.xy + vec2(0.5, 0.5));" 
		+ ""
//		+ "	if (withRefraction) {"
//		+ "		gl_FragColor 		= w.x * texture2D(tex2, myPos + s * scale * refractVector.xy)* shadowCol + "
//		+ "							  w.y * texture2D(tex4, myPos + refractVector.xy * 0.25) + "
//		+ "							  w.w * mondRefl + " 
//		+ "							  w.z * wolkenRefl;"
//		+ "	} else {" 
//		+ "		gl_FragColor 		= w.x * texture2D(tex2, myPos) * shadowCol + "
//		+ "							  w.y * texture2D(tex4, myPos + refractVector.xy * 0.25) + "
//		+ "							  w.w * mondRefl + " 
//		+ "							  w.z * wolkenRefl;"
//		+ "	}" 
		+ " gl_FragColor       = vec4(0.5*(n_surface+1), 1.0);"
		+ "}";
		
	final int WB = WIDTH, HB = HEIGHT;
	
	private int waterTexture1, waterTexture1FB;
	private int waterTexture2, waterTexture2FB;
	private int waterTexture3, waterTexture3FB; 
	private int waterTexture4, waterTexture4FB; 
	private int gaussTexture;
	private int programWaterdynamic, programShaderCaustic, programWaterVisualisation;
	private int uniform_fragShader_waterDynamic_s, uniform_fragShader_waterDynamic_tex1, uniform_fragShader_waterDynamic_tex2;
	private int uniform_fragShader_waterDynamic_D;
	private int uniform_programWaterVisualisation_s, uniform_programWaterVisualisation_scale;
	private int uniform_programWaterVisualisation_tex1, uniform_programWaterVisualisation_tex2, uniform_programWaterVisualisation_tex3;
	private int uniform_programWaterVisualisation_tex4, uniform_programWaterVisualisation_tex5, uniform_programWaterVisualisation_withRefraction;
	private int uniform_programWaterVisualisation_w, uniform_programWaterVisualisation_offSetCloud;
	private int uniform_programShaderCaustic_s, uniform_programShaderCaustic_scale, uniform_programShaderCaustic_tex1;
	private int uniform_programShaderCaustic_tex2, uniform_programShaderCaustic_tex3;
	
	private int texGround1, texGround2, texGround3, texGround4, texGround5, texGround6, texGround7, texSun, texMoon, texCloud;
	
	// *** Sound ***
	private static MusicPlayer myMusicPlayer = null;
	// *** Sound ***
	
	public static JButton butRefraktion, butSound, but1, but2, but3, but4, but5, but6, but7;
	public static JSlider slider1, slider2, slider3, slider4, slider5;
	private final static int sliderMAXI = 100;
	
	public static int 		backGround = 1;
	public static boolean 	refEnable  = true;
	public static boolean 	souEnable  = true;
	
	public InteraktiveWassersimulationNormalMapping(String title, int width, int height) {
        super(title, width, height);
		
		// *** Sound ***
		try {
			myMusicPlayer		= new MusicPlayer();
			System.out.println("sound successfull loaded");
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// *** Sound ***
		
		JFrame f = new JFrame();
		Canvas c = new Canvas();

	    slider1 = new JSlider();		
	    slider1.setBorder(BorderFactory.createTitledBorder("CAUSTIC INPUT"));
	    slider1.setMaximum(sliderMAXI);
	    slider1.setValue(50);
	    slider1.setMajorTickSpacing(sliderMAXI/5);
	    slider1.setMinorTickSpacing(sliderMAXI/20);
	    slider1.setPaintTicks(true);
	    slider1.setPaintLabels(true);		    

	    slider2 = new JSlider();		
	    slider2.setBorder(BorderFactory.createTitledBorder("REF"));
	    slider2.setMaximum(sliderMAXI);
	    slider2.setValue(40);
	    slider2.setMajorTickSpacing(sliderMAXI/5);
	    slider2.setMinorTickSpacing(sliderMAXI/20);
	    slider2.setPaintTicks(true);
	    slider2.setPaintLabels(true);
	    slider2.setOrientation(SwingConstants.VERTICAL);

	    slider3 = new JSlider();		
	    slider3.setBorder(BorderFactory.createTitledBorder("SHA"));
	    slider3.setMaximum(sliderMAXI);
	    slider3.setValue(45);
	    slider3.setMajorTickSpacing(sliderMAXI/5);
	    slider3.setMinorTickSpacing(sliderMAXI/20);
	    slider3.setPaintTicks(true);
	    slider3.setPaintLabels(true);
	    slider3.setOrientation(SwingConstants.VERTICAL);
	    
	    ActionListener aktion = new Knopfdruck();
	    JPanel iconBar = new JPanel();
	    butRefraktion = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/0.jpg"))); 
	    iconBar.add(butRefraktion);
	    butRefraktion.addActionListener(aktion);
	    butRefraktion.setActionCommand("ref");
	    
	    slider4 = new JSlider();		
	    slider4.setBorder(BorderFactory.createTitledBorder("SCALE"));
	    slider4.setMaximum(500);
	    slider4.setValue(200);
	    slider4.setMajorTickSpacing(100);
	    slider4.setMinorTickSpacing(25);
	    slider4.setPaintTicks(true);
	    slider4.setPaintLabels(true);
	    slider4.setOrientation(SwingConstants.HORIZONTAL);
	    iconBar.add(slider4);
	    
	    slider5 = new JSlider();		
	    slider5.setBorder(BorderFactory.createTitledBorder("DAEMPFUNG"));
	    slider5.setMaximum(100);
	    slider5.setValue(98);
	    slider5.setMajorTickSpacing(25);
	    slider5.setMinorTickSpacing(10);
	    slider5.setPaintTicks(true);
	    slider5.setPaintLabels(true);
	    slider5.setOrientation(SwingConstants.HORIZONTAL);
	    iconBar.add(slider5);
	    
	    butSound = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/S.jpg")));
	    iconBar.add(butSound);
	    butSound.addActionListener(aktion);
	    butSound.setActionCommand("sou");
	    
	    but1 = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/1.jpg"))); 
	    iconBar.add(but1);
	    but1.addActionListener(aktion);
	    but1.setActionCommand("1");
	    
	    but2 = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/2.jpg"))); 
	    iconBar.add(but2);
	    but2.addActionListener(aktion);
	    but2.setActionCommand("2");
	    
	    but3 = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/3.jpg"))); 
	    iconBar.add(but3);
	    but3.addActionListener(aktion);
	    but3.setActionCommand("3");
	    
	    but4 = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/4.jpg"))); 
	    iconBar.add(but4);
	    but4.addActionListener(aktion);
	    but4.setActionCommand("4");
	    
	    but5 = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/5.jpg"))); 
	    iconBar.add(but5);
	    but5.addActionListener(aktion);
	    but5.setActionCommand("5");
	    
	    but6 = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/6.jpg"))); 
	    iconBar.add(but6);
	    but6.addActionListener(aktion);
	    but6.setActionCommand("6");
	    
	    but7 = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/B.jpg"))); 
	    iconBar.add(but7);
	    but7.addActionListener(aktion);
	    but7.setActionCommand("7");

	    f.getContentPane().add(c);
	    f.getContentPane().add(iconBar, BorderLayout.NORTH);
	    f.getContentPane().add(slider1, BorderLayout.SOUTH);
	    f.getContentPane().add(slider2, BorderLayout.EAST);
	    f.getContentPane().add(slider3, BorderLayout.WEST);	
		
	    f.setBounds(0, 0, WIDTH, HEIGHT);
	    f.setTitle(title);
	    f.setLocationRelativeTo(null);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		initDisplay(c);
	}
	
	public void prepareVierRotierendeFrameBuffer() {
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
		
		waterTexture4 		= glGenTextures();
		waterTexture4FB 	= glGenFramebuffers();
		glBindTexture(GL_TEXTURE_2D, waterTexture4);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, WB, HB, 0, GL_BGRA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);			
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glBindFramebuffer(GL_FRAMEBUFFER, waterTexture4FB);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, waterTexture4, 0);
		
		// wir wollen die drei FrameBuffer (die in drei Texturen schreiben) allerdings
		// "unsichtbar" im Hintergrund füllen und erst im letzten Schritt ausgewählte Inhalte
		// anzeigen
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glBindTexture(GL_TEXTURE_2D, 0); // steht nur hier, damit die Blöcke austauschbar bleiben
	}
	
	public int loadAndPrepareTexture(String fileName, int W, int H) {
		File file = new File(fileName); 
		int textureNumber = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureNumber);			
		try {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, W, H, 0, GL_BGRA, GL_UNSIGNED_BYTE,
				(IntBuffer)ByteBuffer.allocateDirect(W*H*4).order(ByteOrder.nativeOrder()).asIntBuffer().put(
				ImageIO.read(file).getRGB(0,0, W, H, new int[W*H], 0, W)).rewind());
		} catch (IOException e) {
			e.printStackTrace();
		}
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);					
		glBindTexture(GL_TEXTURE_2D, 0);  		// steht nur hier, damit die Blöcke austauschbar bleiben
		
		return textureNumber;
	}
	
	public void prepareUsedTextures() {
		texGround1 = loadAndPrepareTexture("images/steine_kaustik.jpg", 640, 480); 
		texGround2 = loadAndPrepareTexture("images/steine_rot.jpg", 640, 480); 
		texGround3 = loadAndPrepareTexture("images/sand.jpg", 1280, 1024); 
		texGround4 = loadAndPrepareTexture("images/steine_rund.jpg", 721, 480); 
		texGround5 = loadAndPrepareTexture("images/grüner_kies.jpg", 800, 480); 
		texGround6 = loadAndPrepareTexture("images/korallen.jpg", 800, 532); 
		texGround7 = loadAndPrepareTexture("images/schwarzerUntergrund.png", 640, 480);
		texSun	   = loadAndPrepareTexture("images/sonne.jpg", 322, 297);
		texMoon	   = loadAndPrepareTexture("images/mond.jpg", 322, 297);
		texCloud   = loadAndPrepareTexture("images/wolke.jpg", 200, 200);
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
		uniform_fragShader_waterDynamic_D = glGetUniformLocation(programWaterdynamic, "D");
		glUseProgram(programWaterdynamic);
		
		glUniform2f(uniform_fragShader_waterDynamic_s, 1.0f / WB, 1.0f / HB);
		glUniform1i(uniform_fragShader_waterDynamic_tex1, 1);
		glUniform1i(uniform_fragShader_waterDynamic_tex2, 0);
		glUniform1f(uniform_fragShader_waterDynamic_D, 0.95f + (slider5.getValue()*0.05f)/100.0f);
		
		ShaderUtilities.testShaderProgram(programWaterdynamic);
	}
	
	public void prepareShaderCaustic() {
		programShaderCaustic = glCreateProgram();

		int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragShader, fragShader_caustic);
		glCompileShader(fragShader);
		System.out.println(glGetShaderInfoLog(fragShader, 1024));
		glAttachShader(programShaderCaustic, fragShader);

		glLinkProgram(programShaderCaustic);
		uniform_programShaderCaustic_s 		= glGetUniformLocation(programShaderCaustic, "s");
		uniform_programShaderCaustic_scale 	= glGetUniformLocation(programShaderCaustic, "scale");
		uniform_programShaderCaustic_tex1 	= glGetUniformLocation(programShaderCaustic, "tex1");
		uniform_programShaderCaustic_tex2 	= glGetUniformLocation(programShaderCaustic, "tex2");
		uniform_programShaderCaustic_tex3 	= glGetUniformLocation(programShaderCaustic, "tex3");		
		glUseProgram(programShaderCaustic);
		
		glUniform2f(uniform_programShaderCaustic_s, 1.0f/WB, 1.0f/HB);			
		glUniform1f(uniform_programShaderCaustic_scale, -100.0f);
		glUniform1i(uniform_programShaderCaustic_tex1, 0);
		glUniform1i(uniform_programShaderCaustic_tex2, 1);
		glUniform1i(uniform_programShaderCaustic_tex3, 2);
				
		ShaderUtilities.testShaderProgram(programShaderCaustic);
	}
	
	public void prepareShaderVisualisierung() {
		programWaterVisualisation = glCreateProgram();

		int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragShader, fragShaderWaterDynamicVisualisation);
		glCompileShader(fragShader);
		System.out.println(glGetShaderInfoLog(fragShader, 1024));
		glAttachShader(programWaterVisualisation, fragShader);

		glLinkProgram(programWaterVisualisation);
		uniform_programWaterVisualisation_s 				= glGetUniformLocation(programWaterVisualisation, "s");
		uniform_programWaterVisualisation_scale				= glGetUniformLocation(programWaterVisualisation, "scale");
		uniform_programWaterVisualisation_tex1 				= glGetUniformLocation(programWaterVisualisation, "tex1");
		uniform_programWaterVisualisation_tex2 				= glGetUniformLocation(programWaterVisualisation, "tex2");
		uniform_programWaterVisualisation_tex3				= glGetUniformLocation(programWaterVisualisation, "tex3");
		uniform_programWaterVisualisation_tex4 				= glGetUniformLocation(programWaterVisualisation, "tex4");
		uniform_programWaterVisualisation_tex5 				= glGetUniformLocation(programWaterVisualisation, "tex5");
		uniform_programWaterVisualisation_withRefraction 	= glGetUniformLocation(programWaterVisualisation, "withRefraction");
		uniform_programWaterVisualisation_w 				= glGetUniformLocation(programWaterVisualisation, "w");
		uniform_programWaterVisualisation_offSetCloud 		= glGetUniformLocation(programWaterVisualisation, "offSetCloud");
		
		glUseProgram(programWaterVisualisation);
		
		glUniform2f(uniform_programWaterVisualisation_s, 1.0f/WB, 1.0f/HB);			
		glUniform1f(uniform_programWaterVisualisation_scale, -100.0f);
		glUniform1i(uniform_programWaterVisualisation_tex1, 0);
		glUniform1i(uniform_programWaterVisualisation_tex2, 1);
		glUniform1i(uniform_programWaterVisualisation_tex3, 2);
		glUniform1i(uniform_programWaterVisualisation_tex4, 3);
		glUniform1i(uniform_programWaterVisualisation_tex5, 4);
		glUniform1i(uniform_programWaterVisualisation_withRefraction, 0);
		glUniform4f(uniform_programWaterVisualisation_w, 1.0f, 0.7f, 1.0f, 1.0f);
		glUniform1f(uniform_programWaterVisualisation_offSetCloud, 0.0f);
				
		ShaderUtilities.testShaderProgram(programWaterVisualisation);
	}
	
	@Override
	public void renderLoop() {
		prepareVierRotierendeFrameBuffer();
		gaussTexture = ShaderUtilities.prepareGaussTexture();
		prepareUsedTextures();
		
		prepareShaderWaterEffect();
		prepareShaderCaustic();
		prepareShaderVisualisierung();

		long startTime = 0;
		double diffTime;
		int frame = 0;
		while (!Display.isCloseRequested()) {
			glUseProgram(programWaterVisualisation);
			if (refEnable) {
				glUniform1i(uniform_programWaterVisualisation_withRefraction, 1);
				glUniform4f(uniform_programWaterVisualisation_w, 1.0f, slider1.getValue()/100.0f, slider2.getValue()/100.0f, slider3.getValue()/100.0f);
			} else {
				glUniform1i(uniform_programWaterVisualisation_withRefraction, 0);
				glUniform4f(uniform_programWaterVisualisation_w, 	1.0f, slider1.getValue()/100.0f, slider2.getValue()/100.0f, slider3.getValue()/100.0f);
			}
			glUseProgram(0); 
			
			glUseProgram(programWaterdynamic);
			glUniform1f(uniform_fragShader_waterDynamic_D, 0.97f + (slider5.getValue()*0.03f)/100.0f);
			System.out.println();
			glUseProgram(0); 

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
				// SOUND ***
				if (souEnable && startTime==0) {
					myMusicPlayer.playWater(); 
					startTime = System.nanoTime();
				} 
				// SOUND ***
				
				glBindFramebuffer(GL_FRAMEBUFFER, waterTexture2FB);
				glUseProgram(0);

				glViewport(0, 0, WB, HB);
				glLoadIdentity();
				glTranslated(Mouse.getX() * 2f / WIDTH - 1, Mouse.getY() * 2f / HEIGHT - 1, 0);
				glScaled(0.1, 0.1, 0.1); 

				// Durch das Blenden manipulieren wir die Wasserdynamik mit der Gauss-Verteilung
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_COLOR, GL_ONE_MINUS_SRC_COLOR);

				glEnable(GL_TEXTURE_2D);
				glBindTexture(GL_TEXTURE_2D, gaussTexture);
				glColor3f(1, 1, 1);
				
				POGL.renderViereckMitTexturbindung();
				
				glDisable(GL_TEXTURE_2D);
				glDisable(GL_BLEND);
			} else {
				// SOUND ***										
				// Splasheffekt:
				if (souEnable && startTime != 0) {
					diffTime = ((System.nanoTime()-startTime)/1e9);
					if (diffTime < 0.08f) 							
						myMusicPlayer.playSplash();
				}
				startTime=0;
				// SOUND ***
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
			// Kaustik wird berechnet und als Bild gespeichert
			glUseProgram(programShaderCaustic);		
				
			glUniform2f(uniform_programShaderCaustic_s,	1.0f/WB, 1.0f/HB);			
			glUniform1f(uniform_programShaderCaustic_scale,  0.0f);
			glUniform1i(uniform_programShaderCaustic_tex1, 	0);
			glUniform1i(uniform_programShaderCaustic_tex2, 	1);
			glUniform1i(uniform_programShaderCaustic_tex3, 	2);
				
			glUseProgram(0);							
				
			glBindFramebuffer(GL_FRAMEBUFFER, waterTexture4FB);				
			glUseProgram(programShaderCaustic);					
			glViewport(0, 0, WB, HB);

			glEnable(GL_TEXTURE_2D);
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, waterTexture3);
			glActiveTexture(GL_TEXTURE1);
			if (backGround==1)
				glBindTexture(GL_TEXTURE_2D, texGround1);
			else if (backGround==2)
				glBindTexture(GL_TEXTURE_2D, texGround2);
			else if (backGround==3)
				glBindTexture(GL_TEXTURE_2D, texGround3);
			else if (backGround==4)
				glBindTexture(GL_TEXTURE_2D, texGround4);
			else if (backGround==5)
				glBindTexture(GL_TEXTURE_2D, texGround5);
			else if (backGround==6)
				glBindTexture(GL_TEXTURE_2D, texGround6);
			else if (backGround==7)
				glBindTexture(GL_TEXTURE_2D, texGround7);
			glActiveTexture(GL_TEXTURE2);
			glBindTexture(GL_TEXTURE_2D, texSun);
				
			POGL.renderViereckMitTexturbindung();
				
			glDisable(GL_TEXTURE_2D);
			
			// Gebundene Texturen in der umgekehrten Reihenfolge
			// frei geben.
			glActiveTexture(GL_TEXTURE2);
			glBindTexture(GL_TEXTURE_2D, 0);
			glActiveTexture(GL_TEXTURE1);
			glBindTexture(GL_TEXTURE_2D, 0);
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, 0);
		
			glUseProgram(0);
			// **************************************

			glUseProgram(programWaterVisualisation);					
			float offSetCloud = (frame*0.15f) % WIDTH; 
			glUniform1f(uniform_programWaterVisualisation_offSetCloud, 	offSetCloud);
			glUniform1f(uniform_programWaterVisualisation_scale, -slider4.getValue()); 
			glUseProgram(0);					
			
			glBindFramebuffer(GL_FRAMEBUFFER, 0);				
			glUseProgram(programWaterVisualisation);													
			glViewport(0, 0, WIDTH, HEIGHT);

			glEnable(GL_TEXTURE_2D);
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, waterTexture3);
			glActiveTexture(GL_TEXTURE1);
			if (backGround==1)
				glBindTexture(GL_TEXTURE_2D, texGround1);
			else if (backGround==2)
				glBindTexture(GL_TEXTURE_2D, texGround2);
			else if (backGround==3)
				glBindTexture(GL_TEXTURE_2D, texGround3);
			else if (backGround==4)
				glBindTexture(GL_TEXTURE_2D, texGround4);
			else if (backGround==5)
				glBindTexture(GL_TEXTURE_2D, texGround5);
			else if (backGround==6)
				glBindTexture(GL_TEXTURE_2D, texGround6);
			else if (backGround==7)
				glBindTexture(GL_TEXTURE_2D, texGround7);
			glActiveTexture(GL_TEXTURE2);
			glBindTexture(GL_TEXTURE_2D, texMoon);
			glActiveTexture(GL_TEXTURE3);
			glBindTexture(GL_TEXTURE_2D, waterTexture4);
			glActiveTexture(GL_TEXTURE4);
			glBindTexture(GL_TEXTURE_2D, texCloud);
				
			POGL.renderViereckMitTexturbindung();
				
			glDisable(GL_TEXTURE_2D);
			
			// Gebundene Texturen in der umgekehrten Reihenfolge
			// frei geben.
			glActiveTexture(GL_TEXTURE4);
			glBindTexture(GL_TEXTURE_2D, 0);
			glActiveTexture(GL_TEXTURE3);
			glBindTexture(GL_TEXTURE_2D, 0);
			glActiveTexture(GL_TEXTURE2);
			glBindTexture(GL_TEXTURE_2D, 0);
			glActiveTexture(GL_TEXTURE1);
			glBindTexture(GL_TEXTURE_2D, 0);
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, 0);
			
			glUseProgram(0);
			// **************************************
			frame++;
			Display.update();
		}
		Display.destroy();
	}
	
	public static void main(String[] args) throws LWJGLException {
		new InteraktiveWassersimulationNormalMapping("vividus Verlag. Dino-Buch. Kapitel 8: InteraktiveWassersimulationNormalMapping.java", 1240, 650).start();
	}
}

class Knopfdruck implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("ref")) {
			InteraktiveWassersimulationNormalMapping.refEnable = !InteraktiveWassersimulationNormalMapping.refEnable;
			if (InteraktiveWassersimulationNormalMapping.refEnable)
				InteraktiveWassersimulationNormalMapping.butRefraktion.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/0.jpg")));
			else 
				InteraktiveWassersimulationNormalMapping.butRefraktion.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/02.jpg")));
		}
		else if (cmd.equals("sou")) {
			InteraktiveWassersimulationNormalMapping.souEnable = !InteraktiveWassersimulationNormalMapping.souEnable;
			if (InteraktiveWassersimulationNormalMapping.souEnable) 
				InteraktiveWassersimulationNormalMapping.butSound.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/S.jpg")));
			else 
				InteraktiveWassersimulationNormalMapping.butSound.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/S2.jpg")));
		}
		else if (cmd.equals("1")) 
			InteraktiveWassersimulationNormalMapping.backGround = 1;
		else if (cmd.equals("2")) 
			InteraktiveWassersimulationNormalMapping.backGround = 2;
		else if (cmd.equals("3")) 
			InteraktiveWassersimulationNormalMapping.backGround = 3;
		else if (cmd.equals("4")) 
			InteraktiveWassersimulationNormalMapping.backGround = 4;
		else if (cmd.equals("5")) 
			InteraktiveWassersimulationNormalMapping.backGround = 5;
		else if (cmd.equals("6")) 
			InteraktiveWassersimulationNormalMapping.backGround = 6;
		else if (cmd.equals("7")) 
			InteraktiveWassersimulationNormalMapping.backGround = 7;
	}
}
