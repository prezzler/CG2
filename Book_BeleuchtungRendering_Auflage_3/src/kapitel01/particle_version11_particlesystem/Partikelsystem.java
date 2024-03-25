package kapitel01.particle_version11_particlesystem;

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

public class Partikelsystem extends LWJGLBasisFenster {
	private ObjektManager partikelSystem;
	private double runningAverageFrameTime = 1 / 60, avgRatio = 0.75;
	private long last = System.nanoTime();

	public Partikelsystem(String title, int width, int height) {
	    super(title, width, height);
		initDisplay();
		partikelSystem = ObjektManager.getExemplar();
		erzeugePartikel(1000);
	}

	private void erzeugePartikel(int anz) {
		Random rand = ThreadLocalRandom.current();
		for (int i = 0; i < anz; i++) {
			Partikel partikel = new Partikel(new Vektor2D(rand.nextInt(WIDTH), rand.nextInt(HEIGHT)),
					new Vektor2D(0, rand.nextFloat()), rand.nextInt(3) + 1, 0.05f, 0.39f, 0.51f);
			partikel.setVerhalten(new VerhaltenPartikel(partikel));
			partikel.setObjektManager(partikelSystem);
			partikelSystem.registrierePartikel(partikel);
		}
	}

	public int getCurrFPS() {
		return (int) (1/runningAverageFrameTime);
	}

	@Override
	public void renderLoop() {
		glEnable(GL_DEPTH_TEST);

		while (!Display.isCloseRequested()) {
			long now = System.nanoTime();
			double diff = (now-last)/1e9;
			runningAverageFrameTime = avgRatio*runningAverageFrameTime+(1-avgRatio)*diff;
			last = now;

			glClearColor(0.95f, 0.95f, 0.95f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT);

			// ist ja 2d
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0, WIDTH, HEIGHT, 0, 0, 1);
			glMatrixMode(GL_MODELVIEW);
			glDisable(GL_DEPTH_TEST);

			for (int i = 1; i <= partikelSystem.getPartikelSize(); i++) {
				Partikel aktPartikel = partikelSystem.getPartikel(i);
				aktPartikel.render();
				aktPartikel.update(diff);
			}

			Display.update();
			System.out.println("fps: " + getCurrFPS() + " diff: " + diff);
		}
	}

	public static void main(String[] args) {
		new Partikelsystem("vividus Verlag. Dino-Buch. Kap. 1 (particle_version11_particlesystem): Partikelsystem.java",
              800, 450).start();
	}
}
