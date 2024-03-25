package kapitel01.loesungen;

import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glFrustum;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTranslated;

import org.lwjgl.opengl.Display;
import kapitel01.LWJGLBasisFensterEigenesCanvas;

public class Stern extends LWJGLBasisFensterEigenesCanvas {
	public Stern() {
		super("Stern dreht sich", 600, 600);
	}

	@Override
	public void renderLoop() {
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		long start = System.nanoTime();

		while (!Display.isCloseRequested()) {
			double t = (System.nanoTime() - start) / 1e9;
			POGL.clearBackgroundWithColor(1, 1, 1, 1);

			glLoadIdentity();
			glFrustum(-1, 1, -1, 1, 4, 10);
			glTranslated(0, 0, -5);
			glRotatef((float) t * 100, 1, 0, 1);
			glScaled(.5, .5, .5);

			glColor3d(0, 0, 0);
			POGL.renderStern2(8); // Lösung von Dustin Heyer

			Display.update();
		}
	}

	public static void main(String[] args) {
		new Stern().start();
	}
}