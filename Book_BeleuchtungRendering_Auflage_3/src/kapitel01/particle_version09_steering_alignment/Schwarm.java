package kapitel01.particle_version09_steering_alignment;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import kapitel04.Vektor2D;

import org.lwjgl.opengl.Display;

import kapitel01.LWJGLBasisFenster;

public class Schwarm extends LWJGLBasisFenster {
	private ObjektManager flummis;

	public Schwarm(String title, int width, int height) {
	    super(title, width, height);
		initDisplay();
		flummis = ObjektManager.getExemplar();
		erzeugeFlummies(500);
	}

	private void erzeugeFlummies(int anz) {
		Random rand = ThreadLocalRandom.current();
		for (int i = 0; i < anz; i++) {
			Flummi flummi = new Flummi(new Vektor2D(rand.nextInt(640), rand.nextInt(480)),
					new Vektor2D(0, rand.nextFloat() + 1), rand.nextInt(3) + 1, rand.nextFloat(), rand.nextFloat(),
					rand.nextFloat());
			flummi.setVerhalten(new VerhaltenSchwarm(flummi));
			flummi.setObjektManager(flummis);
			flummis.registriereFlummi(flummi);
		}
	}

	@Override
	public void renderLoop() {
		glEnable(GL_DEPTH_TEST);

		while (!Display.isCloseRequested()) {
			glClearColor(0.95f, 0.95f, 0.95f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT);

			// ist ja 2d
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0, WIDTH, HEIGHT, 0, 0, 1);
			glMatrixMode(GL_MODELVIEW);
			glDisable(GL_DEPTH_TEST);

			for (int i = 1; i <= flummis.getFlummiSize(); i++) {
				Flummi aktFlummi = flummis.getFlummi(i);
				aktFlummi.render();
				aktFlummi.update();
			}

			Display.update();
		}
	}

	public static void main(String[] args) {
		new Schwarm("vividus Verlag. Dino-Buch. Kap. 1 (particle_version09_steering_alignment): Schwarm.java",
              800, 450).start();
	}
}
