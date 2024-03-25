package kapitel02;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicPlayer {
	Clip water=null, splash=null;

	public MusicPlayer() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
	    splash = AudioSystem.getClip();
	    AudioInputStream ais = AudioSystem.getAudioInputStream( new File("sounds/splash.wav") );
	    splash.open(ais);

		water = AudioSystem.getClip();
	    AudioInputStream ais2 = AudioSystem.getAudioInputStream( new File("sounds/water.wav") );
	    water.open(ais2);
	}
	
	public void playSplash() {
		if (splash!=null) {
			if (!splash.isActive())
				splash.loop(1);
		}
	}
	
	public void playWater() {
		if (water!=null) {
			if (!water.isActive())
				water.loop(1);
		}
	}
}
