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

public class WasserdynamikCPU {
	private final float D = 0.99f; 						// Dämpfung
	private final static int WIDTH = 400, HEIGHT = 225; // Bildbreite und -höhe

	private JFrame f;
	private Point mouseCoords;
	private float[] surfaceA, surfaceB, surfaceC;
	private int[] pixels;
	private MemoryImageSource mis;

	public WasserdynamikCPU() {
		initialize();
	}

	private void initialize() {
		surfaceA = new float[WIDTH * HEIGHT];
		surfaceB = new float[WIDTH * HEIGHT];
		surfaceC = new float[WIDTH * HEIGHT];
		pixels = new int[WIDTH * HEIGHT];
		mis = new MemoryImageSource(WIDTH, HEIGHT, pixels, 0, WIDTH);
		mis.setAnimated(true);

		f = new JFrame();
		f.setTitle("vividus Verlag. Dino-Buch. Kapitel 2: WasserdynamikCPU.java");
		final JComponent viewer = new JComponent() {
			private static final long serialVersionUID = 1L;
			Image im = createImage(mis);

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(im, 0, 0, getWidth(), getHeight(), 0, 0, WasserdynamikCPU.WIDTH, WasserdynamikCPU.HEIGHT,
						this);
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
	public static int colorForAmplitude(float a) {
		int green = (int) ((a < 0 ? 0 : a) * 128);
		int yellow = (int) ((-a < 0 ? 0 : -a) * 128);
		green = green > 255 ? 255 : green;
		yellow = yellow > 255 ? 255 : yellow;
		return 0xFF000000 | (yellow << 16) | (green << 8) | (yellow << 8);
	}

	public void renderLoop() throws InterruptedException {
		while (f.isVisible()) {
			float[] newC = surfaceA;
			float[] newB = surfaceC;
			float[] newA = surfaceB;
			surfaceA = newA;
			surfaceB = newB;
			surfaceC = newC;

			// water effect
			for (int y = 1, o = WIDTH + 1; y < HEIGHT - 1; y++, o += 2)
				for (int x = 1; x < WIDTH - 1; x++, o++)
					surfaceC[o] = Math.min(1.0f, Math.max(-1.0f,
							D * ((surfaceB[o - 1] + surfaceB[o + 1] + surfaceB[o - WIDTH] + surfaceB[o + WIDTH]) * 0.5f
									- surfaceA[o])));

			// Quaderobjekt
			int Q_SIZE = WIDTH / 20;
			int posX = Math.min(Math.max(mouseCoords.x - Q_SIZE / 2, 1), WIDTH - Q_SIZE);
			int posY = Math.min(Math.max(mouseCoords.y - Q_SIZE / 2, 1), HEIGHT - Q_SIZE);

			// Alle Pixel des Quaderobjekts vollständig setzen
			for (int y = 0, o = posX + posY * WIDTH, r = WIDTH - Q_SIZE; y < Q_SIZE; y++, o += r)
				for (int x = 0; x < Q_SIZE; x++, o++)
					surfaceB[o] = 1;

			for (int i = 0, l = pixels.length; i < l; i++)
				pixels[i] = colorForAmplitude(surfaceC[i]);

			mis.newPixels();
			Thread.sleep(10);
		}
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		new WasserdynamikCPU().renderLoop();
	}
}
