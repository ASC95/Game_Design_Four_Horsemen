// music loop: http://stackoverflow.com/questions/4875080/music-loop-in-java
// how to play sound: http://stackoverflow.com/questions/26305/how-can-i-play-sound-in-java

package edu.virginia.engine.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.print.DocFlavor.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundManager {
	
	HashMap<String, Clip> fxList = new HashMap<String, Clip>();
	HashMap<String, Clip> musicList = new HashMap<String, Clip>();
	
	public SoundManager() {
		
	}
	
	public void LoadSoundEffect(String id, String filename) throws LineUnavailableException, UnsupportedAudioFileException {
		try {
			String file = ("resources" + File.separator + filename);
			File f = new File(file);
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			fxList.put(id, clip);
			
		} catch (IOException e) {
			System.out.println("[Error in DisplayObject.java:LoadSoundEffect] Could not read sound " + filename);
			e.printStackTrace();
		}
	}
	
	public void PlaySoundEffect(String id) {
		Clip soundFile = fxList.get(id);
		if(soundFile != null) {
			soundFile.start();
		}
	}
	
	public void LoadMusic(String id, String filename) throws LineUnavailableException, UnsupportedAudioFileException {
		try {
			String file = ("resources" + File.separator + filename);
			File f = new File(file);
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			musicList.put(id, clip);
			
		} catch (IOException e) {
			System.out.println("[Error in DisplayObject.java:LoadSoundEffect] Could not read sound " + filename);
			e.printStackTrace();
		}
	}
	
	public void PlayMusic(String id) {
		Clip soundFile = musicList.get(id);
		if(soundFile != null) {
			soundFile.loop(soundFile.LOOP_CONTINUOUSLY);
			soundFile.start();
		}
	}
	
	public void StopMusic(String id) {
		Clip soundFile = musicList.get(id);
		soundFile.stop();
	}
}
