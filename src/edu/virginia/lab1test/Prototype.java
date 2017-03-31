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
	boolean boiCollidable = true;

	int frameCounter2;
	boolean gotHit;

	public Prototype() throws LineUnavailableException, UnsupportedAudioFileException {
		super("Game Prototype", screenWidth, screenHeight);

		this.addChild(boi);
		this.addChild(enemy);
		this.addChild(projectile);
		boi.setPivotPoint(new Point(boi.getUnscaledWidth() / 2, boi.getUnscaledHeight() / 2));
		boi.setHitBox(0, 0, boi.getUnscaledWidth(), boi.getUnscaledHeight());
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

		projectile.setHitBox(0, 0, projectile.getUnscaledWidth(), projectile.getUnscaledHeight());

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

			if (boi != null && boiCollidable && boi.collidesWith(projectile)) {
		    	gotHit = true;
				System.out.println("hit!!");
			}

			if (gotHit) {
		        // need some way to interrupt ALL other possible player states ie. attack1, attack2, etc.
				// if hit during an attack, hitboxes should disappear and immediately stop appearing
				// maybe put these special hitbox children in some arraylist and make sure that they're all off?
				// they're all children...

                // should be an if so i can do things on even frames ie. flash when invincible
		    	switch(frameCounter2) {
					case 0:
						attack1 = false;
						canInput = false;
						boiCollidable = false;
						// TODO: affect knockback by which direction boi gets hit from!!!
						boi.getPosition().x--;
						break;
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
					case 8:
					case 9:
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
					case 16:
						boi.getPosition().x--;
						break;
					case 17:
						canInput = true;
						frameCounter = 0;
						break;
					case 59:
						boiCollidable = true;
						gotHit = false;
						frameCounter2 = -1;
						break;
				}
				frameCounter2++;
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
					case 7:
					case 8:
					case 9:
					case 10:
					case 11:
						// attack2 interrupt here
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
