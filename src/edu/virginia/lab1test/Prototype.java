package edu.virginia.lab1test;

import edu.virginia.engine.display.*;
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
	PhysicsSprite enemy = new PhysicsSprite("enemy", "standing", "stand.png");
	PhysicsSprite projectile = new PhysicsSprite("projectile", "idk", "projectile.png");
	Sprite boiAttack1 = new Sprite("boiAttack1", "boiAttack2.png");
	Sprite boiAttack2 = new Sprite("boiAttack2", "boiAttack2.png");
	Sprite boiAttack3 = new Sprite("boiAttack3", "boiAttack2.png");
	// potential AttackSprite fields
	int frameCounter;
	boolean attack1;
	boolean canInput;

	public Prototype() throws LineUnavailableException, UnsupportedAudioFileException {
		super("Game Prototype", screenWidth, screenHeight);

		this.addChild(boi);
		this.addChild(enemy);
		this.addChild(projectile);
		boi.setPivotPoint(new Point(boi.getUnscaledWidth() / 2, boi.getUnscaledHeight() / 2));
		boi.setHitBox(10, 10, 10, 10);
		boiAttack1.setPosition(62, -30);
		boiAttack1.setVisible(false);
		boiAttack1.setHitBox(0, 0, boiAttack1.getUnscaledWidth(), boiAttack1.getUnscaledHeight());
		boi.addChild(boiAttack1);
		boiAttack2.setPosition(73, -8);
		boiAttack2.setVisible(false);
		boiAttack2.setHitBox(0, 0, boiAttack2.getUnscaledWidth(), boiAttack2.getUnscaledHeight());
		boi.addChild(boiAttack2);
		boiAttack3.setPosition(79, 10);
		boiAttack3.setVisible(false);
		boiAttack3.setHitBox(0, 0, boiAttack3.getUnscaledWidth(), boiAttack3.getUnscaledHeight());
		boi.addChild(boiAttack3);

		enemy.setPosition(this.getScenePanel().getWidth() - 100, (int) (this.getScenePanel().getHeight() - enemy.getUnscaledHeight()*enemy.getScaleX() - 100));
		projectile.setPosition((int) enemy.getPosition().getX(), (int) enemy.getPosition().getY() + 50);

		canInput = true;

		boi.setPosition(10, (int) (this.getScenePanel().getHeight() - boi.getUnscaledHeight()*boi.getScaleX() - 45));
		boi.addImage("walking", "walk1.png", 1, 2);
		boi.addImage("jumping", "jump.png", 1, 1);

	}

	@Override
	public void update(ArrayList<Integer> pressedKeys){
		super.update(pressedKeys);
		if (projectile != null) {
			projectile.getPosition().x -= 10;
			if(projectile.getPosition().x < -30) {
				projectile.getPosition().x = (int) enemy.getPosition().getX();
			}
		}

		if (boi != null) {
		    if (canInput) {
				if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
					boi.getPosition().x -= 5;
					if (!boi.isJumping() && !boi.isFalling() && !boi.getAnimate().equals("walking")) {
						boi.setSpeed(7);
						boi.animate("walking");
						boi.start();
					}
					boi.setScaleX(-1);
				}
				if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
					boi.getPosition().x += 5;
					if (!boi.isJumping() && !boi.isFalling() && !boi.getAnimate().equals("walking")) {
						boi.setSpeed(7);
						boi.animate("walking");
						boi.start();
					}
					boi.setScaleX(1);
				}
				if (pressedKeys.contains(KeyEvent.VK_UP)) {
					if (!boi.isJumping() && !boi.isFalling()) {
						boi.setVelocity(23);
						boi.setGravity(-2);
						boi.setJumping(true);
						boi.setFalling(false);
						boi.animate("jumping");
						boi.start();
					}

				}

				if (pressedKeys.contains(KeyEvent.VK_A)) {
					// this is probably not how we want to implement all of our attacks
					// but it gives us an idea of what our hypothetical Attack class should handle
					// perhaps a similar implementation to Animations
					if (!boi.isJumping() && !boi.isFalling()) {
						boi.animate("standing");
						attack1 = true;
					}
				}
			}



			if (pressedKeys.isEmpty() && !boi.isJumping() && !boi.isFalling()) {
				boi.animate("standing");
				boi.start();
			}

			if (boi.getPosition().getY() > (this.getScenePanel().getHeight() - boi.getUnscaledHeight() - 45)) {
				boi.setJumping(false);
				boi.setFalling(false);
				boi.setPosition(boi.getPosition().x, this.getScenePanel().getHeight() - boi.getUnscaledHeight() - 45);
				/*
				if(boi.getAnimate().equals("jumping")) {
					boi.setSpeed(7);
					boi.animate("walking");
				}
				*/
			}

			if (attack1) {
			    switch(frameCounter) {
					case 0:
						// would actually be collision, not visibility
                        canInput = false;
						boiAttack1.setVisible(true);
						break;
					case 1:
						boiAttack1.setVisible(false);
						boiAttack2.setVisible(true);
						break;
					case 2:
						boiAttack2.setVisible(false);
						boiAttack3.setVisible(true);
						break;
					case 3:
						boiAttack3.setVisible(false);
						break;
					case 12:
						canInput = true;
						attack1 = false;
						frameCounter = -1;
						break;

				}
				frameCounter++;
			}
		}
		
		if(boi != null && boi.collidesWith(projectile)) {
			System.out.println("hit!!");
			
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
