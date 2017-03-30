package edu.virginia.lab1test;

import edu.virginia.engine.display.Game;
import edu.virginia.engine.display.PhysicsSprite;
import edu.virginia.engine.display.TweenEvent;
import edu.virginia.engine.display.TweenableParams;
import edu.virginia.engine.events.IEventListener;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.security.Key;
import java.util.ArrayList;
import java.awt.event.KeyEvent;

/**
 * Created by Resquall on 3/30/2017.
 */
public class Prototype extends Game {

	static int screenWidth = 800;
	static int screenHeight = 600;

	PhysicsSprite boi = new PhysicsSprite("boi", "standing", "standing.png");

	public Prototype() throws LineUnavailableException, UnsupportedAudioFileException {

		super("Game Prototype", screenWidth, screenHeight);

		this.addChild(boi);

		boi.setPosition(10, (int) (this.getScenePanel().getHeight() - boi.getUnscaledHeight()*boi.getScaleX() - 45));
		boi.addImage("walking", "walk1.png", 1, 2);
		boi.addImage("jumping", "jump.png", 1, 1);

	}

	@Override
	public void update(ArrayList<Integer> pressedKeys){
		super.update(pressedKeys);
		if (boi != null) {
			if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
				boi.getPosition().x -= 5;
			}
			if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
				boi.getPosition().x += 5;
			}
			if (pressedKeys.contains(KeyEvent.VK_UP)) {
				if(!boi.isJumping() && !boi.isFalling()) {
					boi.setVelocity(23);
					boi.setGravity(-2);
					boi.setJumping(true);
					boi.setFalling(false);
					boi.animate("jumping");
					boi.start();
				}

			}

		}
	}

	@Override
	public void draw(Graphics g){
		super.draw(g);
	}

	public static void main(String[] args) throws LineUnavailableException, UnsupportedAudioFileException {
		Prototype game = new Prototype();
		game.start();
	}

}
