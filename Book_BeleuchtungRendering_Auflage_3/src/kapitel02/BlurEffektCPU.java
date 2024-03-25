package kapitel02;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.MemoryImageSource;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class BlurEffektCPU {
	private final float D = 0.99f; 						// Dämpfung
	private final static int WIDTH = 400, HEIGHT = 225; // Bildbreite und -höhe

	private JFrame f;
	private Point mouseCoords;
	private float[] surfaceA, surfaceB;
	private int[] pixels;
	private MemoryImageSource mis;

	public BlurEffektCPU() {
		initialize();
	}

	private void initialize() {
		surfaceA = new float[WIDTH * HEIGHT];
		surfaceB = new float[WIDTH * HEIGHT];
		pixels = new int[WIDTH * HEIGHT];
		mis = new MemoryImageSource(WIDTH, HEIGHT, pixels, 0, WIDTH);
		mis.setAnimated(true);

		f = new JFrame();
		f.setTitle("vividus Verlag. Dino-Buch. Kapitel 2: BlurEffektCPU.java");
		final JComponent viewer = new JComponent() {
			private static final long serialVersionUID = 1L;
			Image im = createImage(mis);

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(im, 0, 0, getWidth(), getHeight(), 0, 0, BlurEffektCPU.WIDTH, BlurEffektCPU.HEIGHT, this);
			}
		};
		f.getContentPane().add(viewer);
		f.setBounds(100, 100, 2 * WIDTH, 2 * HEIGHT);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mouseCoords = new Point();
		viewer.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				mouseCoords.setLocation(e.getX() * WIDTH / viewer.getWidth(), e.getY() * HEIGHT / viewer.getHeight());
			}
		});
	}

	// Bestimmung des Farbwertes
	public static int colorForAmplitudeGray(float a) {
		int gray = (int) ((a < 0 ? 0 : a) * 128);	// Werte kleiner als 0 auf 0
		gray = gray > 128 ? 128 : gray;				// Werte größer 128 auf 128
		gray += 127;								// mittlerer Grauwert
		return 0xFF000000 | (gray << 16) | (gray << 8) | gray;
	}

	public void renderLoop() throws InterruptedException {
		while (f.isVisible()) {
			float[] newB = surfaceA;
			float[] newA = surfaceB;
			surfaceA = newA;
			surfaceB = newB;

			// blur effect
			for (int y = 1, o = WIDTH + 1; y < HEIGHT - 1; y++, o += 2)
				for (int x = 1; x < WIDTH - 1; x++, o++)
					surfaceB[o] = ((surfaceA[o] + surfaceA[o - 1] + surfaceA[o + 1] + surfaceA[o - WIDTH]
							+ surfaceA[o + WIDTH]) * 0.2f) * D;

			// Quaderobjekt
			int Q_SIZE = WIDTH / 20;
			int posX = Math.min(Math.max(mouseCoords.x - Q_SIZE / 2, 1), WIDTH - Q_SIZE);
			int posY = Math.min(Math.max(mouseCoords.y - Q_SIZE / 2, 1), HEIGHT - Q_SIZE);

			// Alle Pixel des Quaderobjekts vollständig setzen
			for (int y = 0, o = posX + posY * WIDTH, r = WIDTH - Q_SIZE; y < Q_SIZE; y++, o += r)
				for (int x = 0; x < Q_SIZE; x++, o++)
					surfaceB[o] = 1;

			for (int i = 0, l = pixels.length; i < l; i++)
				pixels[i] = colorForAmplitudeGray(surfaceB[i]);

			mis.newPixels();
			Thread.sleep(10);
		}
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		new BlurEffektCPU().renderLoop();
	}
}
