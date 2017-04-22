package edu.virginia.engine.display;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by Resquall on 4/8/2017.
 */
public class Player extends ActionSprite implements IEventListener {
    // fields???
	// could have fields of button presses corresponding to certain actions to enable remapping
    private int health;
    private int maxHP;
    private double mana;
    private double maxMP;

    private boolean hasDJ;

	private boolean canInput = true;

	private boolean shiftWasPressed;
	private boolean aWasPressed;
	private boolean upWasPressed;
	private boolean eWasPressed;

	private int iFrames;
	private int jumpFrameCounter;

	private AttackHitbox boiAttack1 = new AttackHitbox("boiAttack1", "boiAttack2.png", 10, 0, 0 , 0);
	private AttackHitbox boiAttack2 = new AttackHitbox("boiAttack2", "boiAttack2.png", 10, 0, 0 , 0);
	private AttackHitbox boiAttack3 = new AttackHitbox("boiAttack3", "boiAttack2.png", 10, 0, 0 , 0);

    private AttackHitbox boiAttack4 = new AttackHitbox("boiAttack4", "boiAttack2.png", 15, 0, 0 , 0);
    private AttackHitbox boiAttack5 = new AttackHitbox("boiAttack5", "boiAttack2.png", 15, 0, 0 , 0);
    private AttackHitbox boiAttack6 = new AttackHitbox("boiAttack6", "boiAttack2.png", 15, 0, 0 , 0);


    private AttackHitbox boiAttack7 = new AttackHitbox("boiAttack7 ", "boiAttack2.png", 30, 0, 0 , 0);
    private AttackHitbox boiAttack8 = new AttackHitbox("boiAttack8 ", "boiAttack2.png", 30, 0, 0 , 0);
    private AttackHitbox boiAttack9 = new AttackHitbox("boiAttack9 ", "boiAttack2.png", 30, 0, 0 , 0);
    private AttackHitbox boiAttack10 = new AttackHitbox("boiAttack10 ", "boiAttack2.png", 30, 0, 0 , 0);
    private AttackHitbox boiAttack11 = new AttackHitbox("boiAttack11 ", "boiAttack2.png", 30, 0, 0 , 0);
    private AttackHitbox boiAttack12 = new AttackHitbox("boiAttack12 ", "boiAttack2.png", 30, 0, 0 , 0);

	public Player(String id, String key, String imageFileName) {
		super(id, key, imageFileName);
		// animations
		this.addImage("walking", "walk1.png", 1, 2);
		this.addImage("jumping", "jump.png", 1, 1);

		// attacks no hitboxes
		this.addAttack("dash", new Action(20, 20, 20));
		this.addAttack("got_hit", new Action(30, 30, 30));
		this.addAttack("heal", new Action(25, 50, 70));

		// attacks with hitboxes
		this.addChild(boiAttack1);
		boiAttack1.setPosition(42, -30);
		this.addChild(boiAttack2);
		boiAttack2.setPosition(58, -8);
		this.addChild(boiAttack3);
		boiAttack3.setPosition(69, 10);


		this.addChild(boiAttack4);
		boiAttack4.setPosition(59, -15);
		this.addChild(boiAttack5);
		boiAttack5.setPosition(79, -15);
        this.addChild(boiAttack6);
        boiAttack6.setPosition(99, -15);


        this.addChild(boiAttack7);
        boiAttack7.setPosition(60, -40);
        this.addChild(boiAttack8);
        boiAttack8.setPosition(80, -35);
        this.addChild(boiAttack9);
        boiAttack9.setPosition(100, -30);
        this.addChild(boiAttack10);
        boiAttack10.setPosition(120, -20);
        this.addChild(boiAttack11);
        boiAttack11.setPosition(100, -10);
        this.addChild(boiAttack12);
        boiAttack12.setPosition(80, -5);

		// actions
		// frame data from marth lol
        Action jab1 = new Action(3, 20, 27);
        jab1.addHitboxes((AttackHitbox)this.getChildAtIndex(0), 4);
        jab1.addHitboxes(boiAttack2, 5);
        jab1.addHitboxes(boiAttack3, 6);

        Action jab2 = new Action(5, 21, 28);
        jab2.addHitboxes(boiAttack4, 5);
        jab2.addHitboxes(boiAttack5, 6);
        jab2.addHitboxes(boiAttack6, 7);

        Action jab3 = new Action(40, 40, 40);
        jab3.addHitboxes(boiAttack7, 9);
        jab3.addHitboxes(boiAttack8, 9);
        jab3.addHitboxes(boiAttack8, 10);
        jab3.addHitboxes(boiAttack9, 10);
        jab3.addHitboxes(boiAttack9, 11);
        jab3.addHitboxes(boiAttack10, 11);
        jab3.addHitboxes(boiAttack11, 11);
        jab3.addHitboxes(boiAttack11, 12);
        jab3.addHitboxes(boiAttack12, 12);

        this.addAttack("jab1", jab1);
        this.addAttack("jab2", jab2);
        this.addAttack("jab3", jab3);

		this.addEventListener(this, "GOT_HIT");
	}

	public Player(String id, String key, String imageFileName, int rows, int col) {
		super(id, key, imageFileName, rows, col);
	}

    public void handleEvent(Event e) {
        // TODO: collision throws events
		if (e.getEventType().equals("GOT_HIT")) {
			// get damage of hitbox and apply
			AttackHitbox x = (AttackHitbox) e.getSource();
			this.damage(x.getDamage());

		    this.fullInterrupt();
			this.iFrames = 120;
			this.velocityX = 0;
			// if velocityY > 0?
			this.velocityY = 0;
			if (this.jumping) {
				this.jumping = false;
				this.falling = true;
			}
			// replace with actual flinch anim
			this.animate("standing");
			this.start();
			this.setAttack("got_hit");
			this.startAttack();
		}
    }

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}

	public void damage(int damage) {
		this.health -= damage;
	}

	public double getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public double getMaxMP() {
		return maxMP;
	}

	public void setMaxMP(int maxMP) {
		this.maxMP = maxMP;
	}

	public boolean canInput() {
		return canInput;
	}

	public void setCanInput(boolean canInput) {
		this.canInput = canInput;
	}

	public boolean canDJ() {
		return hasDJ;
	}

	public void setDJ(boolean canDJ) {
	    this.hasDJ = canDJ;
	}

	public void setiFrames (int iFrames) {
	    this.iFrames = iFrames;
    }

    public int getiFrames () {
		return iFrames;
	}

	public void setLanding() {
		this.jumping = false;
		this.falling = false;
		this.hasDJ = true;
		this.velocityY = 0;
	}

	@Override
    public void update(ArrayList<Integer> pressedKeys) {
		super.update(pressedKeys);
		if (iFrames > 0) {
			this.setCollidable(false);
			this.setAlpha(.5f);
			this.iFrames--;
		} else if (!this.isCollidable()){
			this.setCollidable(true);
			this.setAlpha(1f);
		}

		// mana regen
		if (mana < maxMP) mana += .08;

		// controls
		if (this.canMove()) {
			if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
				this.setVelocityX(-10);
				if (!this.isJumping() && !this.isFalling() && !this.getAnimate().equals("walking")) {
					this.setSpeed(6);
					this.animate("walking");
					this.start();
				}
				this.setScaleX(-1);
			} else if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
				this.setVelocityX(10);
				if (!this.isJumping() && !this.isFalling() && !this.getAnimate().equals("walking")) {
					this.setSpeed(6);
					this.animate("walking");
					this.start();
				}
				this.setScaleX(1);
			} else {
				this.setVelocityX(0);
			}
			if (pressedKeys.contains(KeyEvent.VK_UP)) {
				if (!upWasPressed) {
					if (!this.isJumping() && !this.isFalling()) {
						upWasPressed = true;
						this.setVelocityY(-35);
						this.setJumping(true);
						this.setFalling(false);
						this.animate("jumping");
						this.start();
					} else if (this.canDJ()) {
						upWasPressed = true;
						this.setVelocityY(-30);
						this.setJumping(true);
						this.setFalling(false);
						this.setDJ(false);
					}
				}
				if (upWasPressed) {
					if (this.canDJ()) {
						// if he can double jump and is jumping, then he's using his grounded jump
						if (this.isJumping() && jumpFrameCounter > 5 && jumpFrameCounter < 16) {
							this.setVelocityY(-25 - 6 + jumpFrameCounter);
						}
					} else {
						// i did this because the two jumps should have different timing windows
						if (this.isJumping() && jumpFrameCounter > 5 && jumpFrameCounter < 12) {
							this.setVelocityY(-20 - 6 + jumpFrameCounter);
						}
					}
					jumpFrameCounter++;
				}
			} else {
				upWasPressed = false;
				jumpFrameCounter = 0;
			}
		}

		if (pressedKeys.contains(KeyEvent.VK_A)) {
			if (!aWasPressed && !this.isJumping() && !this.isFalling()) {
				if (this.getCurrentAction() == null) {
					this.animate("standing");
					this.setVelocityX(0);
					this.setAttack("jab1");
					this.startAttack();
					aWasPressed = true;
				} else if (!aWasPressed && this.getCurrentAction().equals("jab1")) {
					this.setAttack("jab2");
					aWasPressed = true;
				} else if (!aWasPressed && this.getCurrentAction().equals("jab2")) {
					this.setAttack("jab3");
					aWasPressed = true;
				}
			}
		} else {
			aWasPressed = false;
		}

		if (pressedKeys.contains(KeyEvent.VK_SHIFT)) {
			if (!this.isJumping() && !this.isFalling()) {
				if (!shiftWasPressed && (this.getCurrentAction() == null)) {
					shiftWasPressed = true;
					// replace with dashing animation
					this.animate("jumping");
					this.start();
					this.setAttack("dash");
					if (this.getiFrames() < 10) {
						this.setiFrames(12);
					}
					this.startAttack();
					if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
						this.setVelocityX(-17);
					} else if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
						this.setVelocityX(17);
					} else if (this.getScaleX() == 1) {
						this.setVelocityX(17);
					} else {
						this.setVelocityX(-17);
					}
				}
			}
		} else {
			shiftWasPressed = false;
		}

		if (pressedKeys.contains(KeyEvent.VK_E)) {
			if (!this.isJumping() && !this.isFalling()) {
				if (!eWasPressed && this.getCurrentAction() == null && mana >= 80) {
					eWasPressed = true;
					// heal anim
					this.animate("standing");
					this.start();
					this.setAttack("heal");
					this.startAttack();
					this.setVelocityX(0);
					health += 100;
					if (health > maxHP) health = maxHP;
					mana -= 80;
				}
			}
		} else {
			eWasPressed = false;
		}

	}

	/*
	@Override
	public void draw(Graphics g){
        super.draw(g);
	}
	*/

}
