package edu.virginia.lab1test;

import edu.virginia.engine.display.*;
import edu.virginia.engine.events.IEventListener;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.awt.event.KeyEvent;

/**
 * Created by Resquall on 3/30/2017.
 */
public class CollisionTest extends Game {

	static int screenWidth = 800;
	static int screenHeight = 600;

	CollisionGrid collisionDetector = new CollisionGrid(screenWidth, screenHeight, 8, 16);
	
	PhysicsSprite boi = new PhysicsSprite("boi", "standing", "standing.png");
	PhysicsSprite enemy = new PhysicsSprite("enemy", "standing", "stand.png");
	PhysicsSprite enemy2 = new PhysicsSprite("enemy2", "standing", "stand.png");
	PhysicsSprite projectile = new PhysicsSprite("projectile", "idk", "projectile.png");
	Sprite boiAttack1 = new Sprite("boiAttack1", "boiAttack2.png");
	Sprite boiAttack2 = new Sprite("boiAttack2", "boiAttack2.png");
	Sprite boiAttack3 = new Sprite("boiAttack3", "boiAttack2.png");
	private int bossHealth = 100;
	private int boiHealth = 100;
	
	// arraylist of things that can damage player on contact like all projectiles
	ArrayList<PhysicsSprite> damageObjects = new ArrayList<PhysicsSprite>();
	
	Sprite test = new Sprite("testing", "projectile.png");

	// potential AttackSprite fields
	int frameCounter;
	int frameCounter2;
	boolean attack1;
	boolean attack1Hit = false;
	boolean canInput;

	int frameC2;
	boolean gotHit;

	public CollisionTest() throws LineUnavailableException, UnsupportedAudioFileException {
		super("Game Prototype Collision Test", screenWidth, screenHeight);		
		
		this.addChild(boi);
		this.addChild(enemy);
		this.addChild(enemy2);
		this.addChild(projectile);
		
		damageObjects.add(enemy);
		damageObjects.add(enemy2);
		damageObjects.add(projectile);
		
		boi.setPivotPoint(new Point(boi.getUnscaledWidth() / 2, boi.getUnscaledHeight() / 2));
		boi.setHitBox(0, 0, boi.getUnscaledWidth(), boi.getUnscaledHeight());

		boi.addChild(boiAttack1);
		boiAttack1.setPosition(62, -30);
		boiAttack1.setVisible(false);
		boiAttack1.setCollidable(false);

		/*
		boiAttack1.addChild(test);
		test.setHitBox(100, 50, 50, 50);
		boiAttack1.setCollidable(false);
		*/
		
		boi.addChild(boiAttack2);
		boiAttack2.setPosition(78, -8);
		boiAttack2.setVisible(false);
		boiAttack2.setCollidable(false);

		boi.addChild(boiAttack3);
		boiAttack3.setPosition(89, 10);
		boiAttack3.setVisible(false);
		boiAttack3.setCollidable(false);

		enemy.setPosition(this.getScenePanel().getWidth() - 100, (int) (this.getScenePanel().getHeight() - enemy.getUnscaledHeight()*enemy.getScaleX() - 100));
		enemy2.setPosition(10, (int) (this.getScenePanel().getHeight() - enemy.getUnscaledHeight()*enemy.getScaleX() - 100));
		projectile.setPosition((int) enemy.getPosition().getX(), (int) enemy.getPosition().getY() + 50);

		canInput = true;

		boi.setPosition(400, (int) (this.getScenePanel().getHeight() - boi.getUnscaledHeight()*boi.getScaleX() - 45));
		boi.addImage("walking", "walk1.png", 1, 2);
		boi.addImage("jumping", "jump.png", 1, 1);

		collisionDetector.addSprite(boi);
		collisionDetector.addSprites(damageObjects);
	}

	@Override
	public void update(ArrayList<Integer> pressedKeys){
		super.update(pressedKeys);

		if (projectile != null) {
			projectile.move(-1, 0);
			if(projectile.getPosition().x < -30) {
				projectile.getPosition().x = (int) enemy.getPosition().getX();
			}			
		}

		if(collisionDetector != null) {
			collisionDetector.update(pressedKeys);
		}
		
		if (boi != null) {

		    if (canInput) {
				if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
					boi.move(-5, 0);
					if (!boi.isJumping() && !boi.isFalling() && !boi.getAnimate().equals("walking")) {
						boi.setSpeed(7);
						boi.animate("walking");
						boi.start();
					}
					boi.setScaleX(-1);
				}
				if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
					boi.move(5, 0);
					if (!boi.isJumping() && !boi.isFalling() && !boi.getAnimate().equals("walking")) {
						boi.setSpeed(7);
						boi.animate("walking");
						boi.start();
					}
					boi.setScaleX(1);
				}
				if (pressedKeys.contains(KeyEvent.VK_UP)) {
					if (!boi.isJumping() && !boi.isFalling()) {
						boi.setVelocityY(-23);
						boi.setGravity(2);
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
						attack1Hit = false;
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

//			checkPlayerCollision();
//			
			List<PhysicsSprite> booop = new ArrayList<PhysicsSprite>();
			booop = collisionDetector.retrieve(boi);
			
			for(int i = 0; i < booop.size(); i++) {
				PhysicsSprite asdf = booop.get(i);
				if(boi.collidesWith(asdf) && damageObjects.contains(asdf)) {
					gotHit = true;
				}
			}
//			for(int i = 0; i < booop.size(); i++) {
//				PhysicsSprite s = booop.get(i);
//				ArrayList<int[]> occupiedCells = collisionDetector.getAllCollidables().get(s);
//				System.out.println(s.getId());
//				for(int k = 0; k < occupiedCells.size(); k++) {
//					int[] p = occupiedCells.get(k);
//					System.out.print("[" + p[0] + ", " + p[1] + "]");
//				}
//				System.out.println("");
//			}
//			
//			if(list.contains(enemy)) {
//				System.out.println("enemy");
//			} else if(list.contains(enemy2)) {
//				System.out.println("enemy2");
//			} else if(list.contains(projectile)) {
//				System.out.println("projectile");
//			}
			if (gotHit) {
		        // need some way to interrupt ALL other possible player states ie. attack1, attack2, etc.
				// if hit during an attack, hitboxes should disappear and immediately stop appearing
				// maybe put these special hitbox children in some arraylist and make sure that they're all off?
				// they're all children...
				
				for (DisplayObject attack : boi.getChildren()) {
					attack.setCollidable(false);
					attack.setVisible(false);
				}
                // should be an if so i can do things on even frames ie. flash when invincible
                if (frameCounter2 < 1) {
					boi.animate("standing");
					attack1 = false;
					canInput = false;
					boi.setCollidable(false);
					boiHealth -= 10;
					// TODO: affect knockback by which direction boi gets hit from!!!
//					boi.getPosition().x--;
					boi.move(-1, 0);
				} else if (frameCounter2 < 17) {
					boi.move(-1,0);
				} else if (frameCounter2 == 17) {
                	canInput = true;
                	frameCounter = 0;
				} else if (frameCounter2 == 59) {
                	boi.setCollidable(true);
                	gotHit = false;
                	frameCounter2 = -1;
                	boi.setVisible(true);
				}
				if (frameCounter2 % 2 == 1) {
					// every even frame, flash
					boi.setVisible(!boi.isVisible());
				}
				/*
		    	switch(frameCounter2) {
					case 0:
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
						boi.setCollidable(true);
						gotHit = false;
						frameCounter2 = -1;
						break;
				}
				*/
				frameCounter2++;
			}

			if (attack1) {
			    switch(frameCounter) {
					case 0:
						// would actually be collision, not visibility
                        canInput = false;
						boiAttack1.setVisible(true);
						boiAttack1.setCollidable(true);
						break;
					case 1:
						boiAttack1.setVisible(false);
						boiAttack2.setVisible(true);
						boiAttack1.setCollidable(false);
						boiAttack2.setCollidable(true);
						break;
					case 2:
						boiAttack2.setVisible(false);
						boiAttack3.setVisible(true);
						boiAttack2.setCollidable(false);
						boiAttack3.setCollidable(true);
						break;
					case 3:
						boiAttack3.setVisible(false);
						boiAttack3.setCollidable(false);
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
		
		/*
		if (boiAttack3 != null && boiAttack3.collidesWith(enemy)) {
			bossHealth -= 10;
		}
		*/
		
		if(enemy != null && enemy2 != null) {
//			checkBossCollision();
		}
		
		/**
		 * if (!attack1Hit) {
			if (boiAttack1 != null && boiAttack1.collidesWith(enemy)) {
				bossHealth -= 10;
				attack1Hit = true;
			}
			if (boiAttack1 != null && boiAttack1.collidesWith(enemy2)) {
				bossHealth -= 10;
				attack1Hit = true;
			}
			if (boiAttack2 != null && boiAttack2.collidesWith(enemy)) {
				bossHealth -= 10;
				attack1Hit = true;
			}
			if (boiAttack2 != null && boiAttack2.collidesWith(enemy2)) {
				bossHealth -= 10;
				attack1Hit = true;
			}
			if (boiAttack3 != null && boiAttack3.collidesWith(enemy)) {
				bossHealth -= 10;
				attack1Hit = true;
			}
			if (boiAttack3 != null && boiAttack3.collidesWith(enemy2)) {
				bossHealth -= 10;
				attack1Hit = true;
			}
		}
		 */
		
		// TODO: fix this
		/**if (boi != null && boi.collidesWith(enemy)) {
			bossHealth -= 10;
		} **/
	}
	// method to check collision for boss
	private void checkBossCollision() {
		double halfway = this.getScenePanel().WIDTH/2;

		if (!attack1Hit) {
			if(boi.getPosition().x < halfway) {
				
			} else {
				
			}
			if (boiAttack1 != null && boiAttack1.collidesWith(enemy)) {
				bossHealth -= 10;
				attack1Hit = true;
			}
			if (boiAttack1 != null && boiAttack1.collidesWith(enemy2)) {
				bossHealth -= 10;
				attack1Hit = true;
			}
			if (boiAttack2 != null && boiAttack2.collidesWith(enemy)) {
				bossHealth -= 10;
				attack1Hit = true;
			}
			if (boiAttack2 != null && boiAttack2.collidesWith(enemy2)) {
				bossHealth -= 10;
				attack1Hit = true;
			}
			if (boiAttack3 != null && boiAttack3.collidesWith(enemy)) {
				bossHealth -= 10;
				attack1Hit = true;
			}
			if (boiAttack3 != null && boiAttack3.collidesWith(enemy2)) {
				bossHealth -= 10;
				attack1Hit = true;
			}
		}
	}
	
	// method to check collision of player with stuff
	// TODO: probably differentiate between player/boss/attack/platform collision eventually
	private void checkPlayerCollision() {
		// check if boi is in the left half of the screen
		double halfway = this.getScenePanel().WIDTH/2;
		
		if(boi.getPosition().x < halfway) {
			// check if enemies are also in left half
			for(int i = 0; i < damageObjects.size(); i++) {
				// currently only for objects that damage boy; can be modified to include platforms?
				PhysicsSprite cur = damageObjects.get(i);
				
				if(cur.getPosition().x < halfway) {
					if(boi.collidesWith(cur)) {
						// possibly throw an event here
						gotHit = true;
					}
				}
			}
		} else { // boi is in right half of screen
			for(int i = 0; i < damageObjects.size(); i++) {
				PhysicsSprite cur = damageObjects.get(i);
				
				if(cur.getPosition().x >= halfway) {
					if(boi.collidesWith(cur)) {
						gotHit = true;
					}
				}
			}
		}
	}

	private void drawHealthBar(int x, int y, int width, int height, Color color, Graphics g) {
		g.setColor(color);
		g.fillRect(x, y + (100 - height), width, height);
		g.setColor(Color.BLACK);
		for (int i = 0; i < 10; i++) {
			g.drawRect(x, y + (i * 10), width, 10);
		}
	}

	@Override
	public void draw(Graphics g){
		super.draw(g);
		drawHealthBar(750, 50, 25, bossHealth, Color.RED, g);
		drawHealthBar(150, 50, 25, boiHealth, Color.GREEN, g);

		Graphics2D g2d = (Graphics2D) g;

		if(boi != null) {
			for(DisplayObject child : boi.getChildren()) {
				g2d.setColor(Color.red);
				g2d.draw(child.getHitBox());
			}
		}
		if(test != null) {
			g2d.draw(test.getHitBox());
		}
		for (DisplayObject child : this.getChildren()) {
			g2d.setColor(Color.red);
			g2d.draw(child.getHitBox());
		}
		
		int xCell = this.screenWidth/16;
		int yCell = this.screenHeight/8;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 16; j++) {
				g2d.drawLine(xCell *(j), (yCell)* (i), xCell * j, yCell * (i + 1));
			}
		}
		for(int i = 0; i < 8; i++) {
			g2d.drawLine(0, (yCell *i), screenWidth, (yCell*i));
		}

		//Draw the boss's health bar
		//g.setColor(Color.RED);
		//g.fillRect(750, 50 + (100 - bossHealth), 25, bossHealth);

		//g.setColor(Color.BLACK);
		//for (int i = 0; i < 10; i++) {
		//	g.drawRect(750, 50 + (i * 10), 25, 10);
		//}
		//Draw
	}

	public static void main(String[] args) throws LineUnavailableException, UnsupportedAudioFileException {
		CollisionTest game = new CollisionTest();
		game.start();
	}

}
