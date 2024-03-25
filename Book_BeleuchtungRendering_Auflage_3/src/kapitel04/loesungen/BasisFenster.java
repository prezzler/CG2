package kapitel04.loesungen;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BasisFenster extends Frame {
	public BasisFenster(String titel, int breite, int hoehe) {
		setTitle(titel);
		setSize(breite, hoehe);
		setLocationRelativeTo(null);
		addWindowListener(new WindowAdapter() {
			@Override
         public void windowClosing(WindowEvent event) {
				event.getWindow().dispose();
				System.exit(0);
			}
		});
	}
}