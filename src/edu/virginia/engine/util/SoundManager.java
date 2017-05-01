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
import javax.sound.sampled.*;

public class SoundManager implements IEventListener {

    HashMap<String, Clip> fxList = new HashMap<String, Clip>();
    HashMap<String, Clip> musicList = new HashMap<String, Clip>();

    //load some fx by default
    public SoundManager() {
        try {
            loadSoundEffect("boiStrike1", "boiStrike1.wav");
            loadSoundEffect("boiStrike2", "boiStrike2.wav");
            loadSoundEffect("boiStrike3", "boiStrike3.wav");
            loadSoundEffect("boiInjured0", "boiInjured0.wav");
            loadSoundEffect("boiHealed", "boiHealed.wav");
            loadSoundEffect("bossDash", "bossDash.wav");
            loadSoundEffect("bossFireball", "bossFireball2.wav");
            loadSoundEffect("bossSlash", "bossSlash.wav");
            loadSoundEffect("bossArrow", "arrow.wav");
            loadSoundEffect("boiDash", "boiDash.wav");
            loadSoundEffect("lightningStrike", "lightning_short_quiet.wav");
//            loadSoundEffect("footstep", "footstep.wav");
            loadMusic("footstep", "footsteps2.wav");
            loadSoundEffect("jump1", "jump1.wav");
            loadSoundEffect("jump2", "jump2.wav");
            loadSoundEffect("boiWhiff1", "boiWhiff1.wav");
            loadSoundEffect("boiWhiff2", "boiWhiff2.wav");
            loadSoundEffect("boiWhiff3", "boiWhiff3.wav");
            loadMusic("lightningArc", "lightningArc.wav");
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
                    playSoundEffect("boiStrike1");
                    break;
                case "boiAttack2":
                    playSoundEffect("boiStrike2");
                    break;
                case "boiAttack3":
                    playSoundEffect("boiStrike3");
                    break;
            }
        } else if (event.getEventType().equals("BOSS_DASH")) {
            playSoundEffect("bossDash");
        } else if (event.getEventType().equals("BOI_INJURED_0")) {
            playSoundEffect("boiInjured0");
        } else if (event.getEventType().equals("BOSS_FIREBALL")) {
            playSoundEffect("bossFireball");
        } else if (event.getEventType().equals("BOSS_SLASH")) {
            playSoundEffect("bossSlash");
        } else if (event.getEventType().equals("BOSS_ARROW")) {
            playSoundEffect("bossArrow");
        } else if (event.getEventType().equals("BOI_HEALED")) {
            playSoundEffect("boiHealed");
        } else if (event.getEventType().equals("BOI_DASH")) {
            playSoundEffect("boiDash");
        } else if (event.getEventType().equals("LIGHTNING_STRIKE")) {
            playSoundEffect("lightningStrike");
        } else if (event.getEventType().equals("BOI_WALKING")) {
            playMusic("footstep");
        } else if (event.getEventType().equals("BOI_STOPPED_WALKING")) {
            stopMusic("footstep");
        } else if (event.getEventType().equals("BOI_JUMP_1")) {
            playSoundEffect("jump1");
        } else if (event.getEventType().equals("BOI_JUMP_2")) {
            playSoundEffect("jump2");
        } else if (event.getEventType().equals("BOI_WHIFF_1")) {
            playSoundEffect("boiWhiff1");
        } else if (event.getEventType().equals("BOI_WHIFF_2")) {
            playSoundEffect("boiWhiff2");
        } else if (event.getEventType().equals("BOI_WHIFF_3")) {
            playSoundEffect("boiWhiff3");
        } else if (event.getEventType().equals("LIGHTNING_ARC")) {
            playMusic("lightningArc");
        } else if (event.getEventType().equals("STOP_LIGHTNING_ARC")) {
            stopMusic("lightningArc");
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
        soundFile.setFramePosition(0); //I added this
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
