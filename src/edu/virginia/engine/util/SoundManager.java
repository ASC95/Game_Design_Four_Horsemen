// music loop: http://stackoverflow.com/questions/4875080/music-loop-in-java
// how to play sound: http://stackoverflow.com/questions/26305/how-can-i-play-sound-in-java

package edu.virginia.engine.util;

import edu.virginia.engine.display.AttackHitbox;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.print.DocFlavor.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundManager implements IEventListener {

    HashMap<String, Clip> fxList = new HashMap<String, Clip>();
    HashMap<String, Clip> musicList = new HashMap<String, Clip>();

    //load some fx by default
    public SoundManager() {
        try {
            loadSoundEffect("boiStrike0", "boiStrike0.wav");
            loadSoundEffect("boiStrike1", "boiStrike1.wav");
            loadSoundEffect("boiStrike2", "boiStrike2.wav");
            loadSoundEffect("boiStrike3", "boiStrike3.wav");
            loadSoundEffect("boiInjured0", "boiInjured0.wav");
            loadSoundEffect("bossDash", "bossDash.wav");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getSource() instanceof AttackHitbox) {
            AttackHitbox x = (AttackHitbox) event.getSource();
            switch (x.getId()) {
                case "boiAttack1":
                    playSoundEffect("boiStrike0");
                    break;
                case "boiAttack2":
                    playSoundEffect("boiStrike2");
                    break;
                case "boiAttack3":
                    playSoundEffect("boiStrike3");
                    break;
            }
        }
//        if (event.getEventType().equals("BOSS_HIT")) {
//            playSoundEffect("boiStrike1");
//        }
        else if (event.getEventType().equals("BOSS_DASH")) {
            playSoundEffect("bossDash");
        } else if (event.getEventType().equals("BOI_INJURED_0")) {
            playSoundEffect("boiInjured0");
        }
    }

    public void loadSoundEffect(String id, String filename) throws LineUnavailableException, UnsupportedAudioFileException {
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

    public void playSoundEffect(String id) {
        Clip soundFile = fxList.get(id);
        if (soundFile != null) {
            soundFile.setFramePosition(0);
            soundFile.start();
        }
    }

    public void loadMusic(String id, String filename) throws LineUnavailableException, UnsupportedAudioFileException {
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

    public void playMusic(String id) {
        Clip soundFile = musicList.get(id);
        if (soundFile != null) {
            soundFile.loop(soundFile.LOOP_CONTINUOUSLY);
            soundFile.start();
        }
    }

    public void stopMusic(String id) {
        Clip soundFile = musicList.get(id);
        soundFile.stop();
    }

    public HashMap getMusicList() {
        return musicList;
    }

    public HashMap getFXList() {
        return fxList;
    }

    public void stopAllMusic() {
        for (Map.Entry<String, Clip> entry : musicList.entrySet()) {
            String key = entry.getKey();
//			Clip value = entry.getValue();
            stopMusic(key);
        }
    }
}
