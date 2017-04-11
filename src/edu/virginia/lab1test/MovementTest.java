package edu.virginia.lab1test;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.display.*;
import edu.virginia.engine.events.IEventListener;
import edu.virginia.engine.util.GameClock;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by Resquall on 4/9/2017.
 */
public class MovementTest extends Game implements IEventListener {

    Player boi = new Player("boi", "standing", "standing.png");
    /*
    PhysicsSprite enemy = new PhysicsSprite("enemy", "standing", "stand.png");
    PhysicsSprite enemy2 = new PhysicsSprite("enemy2", "standing", "stand.png");
    */
    PhysicsSprite jumpingEnemy = new PhysicsSprite("jumpingEnemy", "standing", "stand.png");

    ActionSprite boss = new ActionSprite("boss", "standing", "bossPlaceholder1.png");

	AttackHitbox boiAttack1 = new AttackHitbox("boiAttack1", "boiAttack2.png", 10, 0, 0 , 0);
	AttackHitbox boiAttack2 = new AttackHitbox("boiAttack2", "boiAttack2.png", 10, 0, 0 , 0);
	AttackHitbox boiAttack3 = new AttackHitbox("boiAttack3", "boiAttack2.png", 10, 0, 0 , 0);

    AttackHitbox boiAttack4 = new AttackHitbox("boiAttack4", "boiAttack2.png", 15, 0, 0 , 0);
    AttackHitbox boiAttack5 = new AttackHitbox("boiAttack5", "boiAttack2.png", 15, 0, 0 , 0);
    AttackHitbox boiAttack6 = new AttackHitbox("boiAttack6", "boiAttack2.png", 15, 0, 0 , 0);


    AttackHitbox boiAttack7 = new AttackHitbox("boiAttack7 ", "boiAttack2.png", 30, 0, 0 , 0);
    AttackHitbox boiAttack8 = new AttackHitbox("boiAttack8 ", "boiAttack2.png", 30, 0, 0 , 0);
    AttackHitbox boiAttack9 = new AttackHitbox("boiAttack9 ", "boiAttack2.png", 30, 0, 0 , 0);
    AttackHitbox boiAttack10 = new AttackHitbox("boiAttack10 ", "boiAttack2.png", 30, 0, 0 , 0);
    AttackHitbox boiAttack11 = new AttackHitbox("boiAttack11 ", "boiAttack2.png", 30, 0, 0 , 0);
    AttackHitbox boiAttack12 = new AttackHitbox("boiAttack12 ", "boiAttack2.png", 30, 0, 0 , 0);


    AttackHitbox bossAttack1 = new AttackHitbox("bossAttack1", "bossAttack1.png", 40, 0, 0, 0);
    AttackHitbox bossAttack2 = new AttackHitbox("bossAttack1", "bossAttack1.png", 40, 0, 0, 0);
    AttackHitbox bossAttack3 = new AttackHitbox("bossAttack1", "bossAttack1.png", 40, 0, 0, 0);

    AttackHitbox fireball1 = new AttackHitbox("bossAttack1", "bossAttack1.png", 40, 0, 0, 0);

    TweenJuggler juggler = new TweenJuggler();
    Tween bossStingerTween = new Tween(boss);
    Tween fireballTween = new Tween(fireball1);

    GameClock bossTimer = new GameClock();

    boolean upWasPressed;
    boolean aWasPressed;
    boolean shiftWasPressed;
    int jumpFrameCounter;
    int dashFrameCounter;

    public MovementTest() {
        super("Movement", 1280, 720);
        // 1920 - 1280 = 640
        // 1080 - 720 = 360

        this.addChild(boss);
        this.addChild(boi);
        this.addChild(jumpingEnemy);
        jumpingEnemy.setPosition(50, 400);
        jumpingEnemy.altMove = true;
        jumpingEnemy.setAccelerationY(.09);
        jumpingEnemy.jumpToCoordwithVelocity(700, 5);
        //jumpingEnemy.jumpToCoordwithVelocity(700, 5);
        //jumpingEnemy.setAccelerationY(.09);
        /*
        this.addChild(enemy);
        this.addChild(enemy2);
        */

        boi.setPivotPoint(new Point(boi.getUnscaledWidth() / 2, boi.getUnscaledHeight() / 2));
        boi.setHitBox(0, 0, boi.getUnscaledWidth(), boi.getUnscaledHeight());

        boss.setPivotPoint(new Point(boss.getUnscaledWidth() / 2, boss.getUnscaledHeight() / 2));
        boss.setHitBox(0, 0, boss.getUnscaledWidth(), boss.getUnscaledHeight());

        // attack stuff
        boi.addChild(boiAttack1);
		boiAttack1.setPosition(42, -30);
		boi.addChild(boiAttack2);
		boiAttack2.setPosition(58, -8);
		boi.addChild(boiAttack3);
		boiAttack3.setPosition(69, 10);


		boi.addChild(boiAttack4);
		boiAttack4.setPosition(59, -15);
		boi.addChild(boiAttack5);
		boiAttack5.setPosition(79, -15);
        boi.addChild(boiAttack6);
        boiAttack6.setPosition(99, -15);


        boi.addChild(boiAttack7);
        boiAttack7.setPosition(60, -40);

        boi.addChild(boiAttack8);
        boiAttack8.setPosition(80, -35);

        boi.addChild(boiAttack9);
        boiAttack9.setPosition(100, -30);

        boi.addChild(boiAttack10);
        boiAttack10.setPosition(120, -20);

        boi.addChild(boiAttack11);
        boiAttack11.setPosition(100, -10);

        boi.addChild(boiAttack12);
        boiAttack12.setPosition(80, -5);

		// actions
		// frame data from marth lol
        Action jab1 = new Action(3, 20, 27);
        jab1.addHitboxes((AttackHitbox)boi.getChildAtIndex(0), 4);
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

        boi.addAttack("jab1", jab1);
        boi.addAttack("jab2", jab2);
        boi.addAttack("jab3", jab3);
        boi.addAttack("dash", new Action(20, 20, 20));
        boi.setGravity(2);


        // how to set things based on bottom?
        // i want everything on the floor...
        /*
        enemy.setPosition(0, 900);
        enemy2.setPosition(1820, 900);
        */
        boi.setPosition(1280 / 2, 540 + boi.getUnscaledHeight() / 2 + 1);
        boi.addImage("walking", "walk1.png", 1, 2);
        boi.addImage("jumping", "jump.png", 1, 1);

        boss.setPosition(100, 540);
        boss.addImage("stinger", "bossPlaceholder2.png", 1, 1);
        boss.addImage("slash", "bossPlaceholder3.png", 1, 1);
        boss.addImage("fireball", "bossPlaceHolder4.png", 1, 1);

        boss.addChild(bossAttack1);
        bossAttack1.setPosition(boss.getUnscaledWidth() / 2, 0);
        boss.addChild(bossAttack2);
        bossAttack2.setPosition(boss.getUnscaledWidth() / 2, -boss.getUnscaledHeight() / 2);
        boss.addChild(bossAttack3);
        bossAttack3.setPosition(boss.getUnscaledWidth(), -boss.getUnscaledHeight() / 4);
        boss.addChild(fireball1);
        fireball1.setPosition(boss.getUnscaledWidth(), -boss.getUnscaledHeight() / 4);

        Action bossStinger = new Action(60 + 20 + 30, 110, 110);
        for (int i = 61; i < 81; i++) {
            bossStinger.addHitboxes(bossAttack1, i);
            bossStinger.addHitboxes(bossAttack2, i);
            bossStinger.addHitboxes(bossAttack3, i);
        }

        Action bossSlash1 = new Action(0, 59, 60);
        bossSlash1.addHitboxes(bossAttack2, 40);
        bossSlash1.addHitboxes(bossAttack3, 40);
        bossSlash1.addHitboxes(bossAttack3, 41);
        bossSlash1.addHitboxes(bossAttack3, 42);
        bossSlash1.addHitboxes(bossAttack1, 42);

        Action bossFireball1 = new Action(40 + 30 + 30, 100, 100);
        for (int i = 41; i < 71; i++) {
            bossFireball1.addHitboxes(fireball1, i);
        }

        boss.addAttack("stinger", bossStinger);
        boss.addAttack("slash", bossSlash1);
        boss.addAttack("fireball", bossFireball1);

        boss.addEventListener(this, "ATTACK_END");

    }

    @Override
    public void update(ArrayList<Integer> pressedKeys) {
        super.update(pressedKeys);
        if (juggler != null) {
            juggler.nextFrame();
        }
        if (boi != null) {
            if (boi.canMove()) {
                if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
                    boi.setVelocityX(-10);
                    if (!boi.isJumping() && !boi.isFalling() && !boi.getAnimate().equals("walking")) {
                        boi.setSpeed(6);
                        boi.animate("walking");
                        boi.start();
                    }
                    boi.setScaleX(-1);
                } else if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
                    boi.setVelocityX(10);
                    if (!boi.isJumping() && !boi.isFalling() && !boi.getAnimate().equals("walking")) {
                        boi.setSpeed(6);
                        boi.animate("walking");
                        boi.start();
                    }
                    boi.setScaleX(1);
                } else {
                    boi.setVelocityX(0);
                }
                if (pressedKeys.contains(KeyEvent.VK_UP)) {
                    if (!upWasPressed) {
                        if (!boi.isJumping() && !boi.isFalling()) {
                            upWasPressed = true;
                            boi.setVelocityY(-35);
                            boi.setJumping(true);
                            boi.setFalling(false);
                            boi.animate("jumping");
                            boi.start();
                        } else if (boi.canDJ()) {
                            upWasPressed = true;
                            boi.setVelocityY(-30);
                            boi.setJumping(true);
                            boi.setFalling(false);
                            boi.setDJ(false);
                        }
                    }
                    if (upWasPressed) {
                        if (boi.canDJ()) {
                            // if he can double jump and is jumping, then he's using his grounded jump
                            if (boi.isJumping() && jumpFrameCounter > 5 && jumpFrameCounter < 16) {
                                boi.setVelocityY(-25 - 6 + jumpFrameCounter);
                            }
                        } else {
                            // i did this because the two jumps should have different timing windows
                            if (boi.isJumping() && jumpFrameCounter > 5 && jumpFrameCounter < 12) {
                                boi.setVelocityY(-20 - 6 + jumpFrameCounter);
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
                if (!boi.isJumping() && !boi.isFalling()) {
                    boi.animate("standing");
                    boi.setVelocityX(0);
                    if (boi.getCurrentAction() == null) {
						boi.setAttack("jab1");
						boi.startAttack();
						aWasPressed = true;
					} else if (!aWasPressed && boi.getCurrentAction().equals("jab1")) {
                    	boi.setAttack("jab2");
                    	aWasPressed = true;
					} else if (!aWasPressed && boi.getCurrentAction().equals("jab2")) {
                        boi.setAttack("jab3");
                        aWasPressed = true;
                    }
                }
            } else {
		    	aWasPressed = false;
			}

			if (pressedKeys.contains(KeyEvent.VK_SHIFT)) {
                if (!boi.isJumping() && !boi.isFalling()) {
                    if (!shiftWasPressed && (boi.getCurrentAction() == null)) {
                        shiftWasPressed = true;
                        // replace with dashing animation
                        boi.animate("jumping");
                        boi.start();
                        boi.setAttack("dash");
                        boi.setiFrames(10);
                        boi.startAttack();
                        if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
                            boi.setVelocityX(-17);
                        } else if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
                            boi.setVelocityX(17);
                        } else if (boi.getScaleX() == 1) {
                            boi.setVelocityX(17);
                        } else {
                            boi.setVelocityX(-17);
                        }
                    }
                }
            } else {
                shiftWasPressed = false;
            }

            if (boi.getVelocityX() == 0 && !boi.isJumping() && !boi.isFalling() && !boi.isAttacking()) {
                boi.animate("standing");
                boi.start();
            }

            if (boi.getPosition().getY() > 540 + boi.getUnscaledHeight() / 2) {
                boi.setJumping(false);
                boi.setFalling(false);
                boi.setDJ(true);
                boi.setPosition(boi.getPosition().x, 540 + boi.getUnscaledHeight() / 2);
                boi.setVelocityY(0);
            }
            if (jumpingEnemy.getPosition().getY() > 540 + jumpingEnemy.getUnscaledHeight() / 2) {
                jumpingEnemy.setJumping(false);
                jumpingEnemy.setFalling(false);
                //jumpingEnemy.setDJ(true);
                jumpingEnemy.setPosition(jumpingEnemy.getPosition().x, 500 + jumpingEnemy.getUnscaledHeight() / 2);
                jumpingEnemy.setVelocityY(0);
                jumpingEnemy.setAccelerationY(0);
            }



        }
        if (boss != null && !boss.isAttacking()) {
            if (bossTimer.getElapsedTime() > 3000) {
                if (bossTimer.getElapsedTime() * 1000000 % 3 == 0) {
                    // tween to one side of the stage
                    // handleEvent of tween should start attack
                    if (boss.getPosition().x > 500) {
                        boss.setScaleX(-1);
                    } else {
                        boss.setScaleX(1);
                    }
                    boss.setAttack("stinger");
                    boss.animate("stinger");
                    boss.start();
                    boss.startAttack();
                } else if (bossTimer.getElapsedTime() * 1000000 % 3 == 1) {
                    if (boss.getPosition().x < boi.getPosition().x) {
                        boss.setScaleX(1);
                    } else {
                        boss.setScaleX(-1);
                    }
                    boss.setAttack("slash");
                    boss.animate("slash");
                    boss.start();
                    boss.startAttack();
                } else {
                    if (boss.getPosition().x < boi.getPosition().x) {
                        boss.setScaleX(1);
                    } else {
                        boss.setScaleX(-1);
                    }
                    boss.setAttack("fireball");
                    boss.animate("fireball");
                    boss.start();
                    boss.startAttack();
                }
            } else {
                boss.animate("standing");
                boss.start();
            }
        }
        if (boss != null && boss.isAttacking()) {
            if (boss.getCurrentAction().equals("stinger") && boss.getFrameCounter() == 60) {
                bossStingerTween.animate(TweenableParams.X, boss.getPosition().x, 1280 - boss.getPosition().x, 20 * 21.33);
                juggler.add(bossStingerTween);
            }
            if (boss.getCurrentAction().equals("fireball") && boss.getFrameCounter() == 40) {
                fireballTween.animate(TweenableParams.X, boss.getUnscaledWidth() / 2, 1280, 30 * 21.33);
                juggler.add(fireballTween);
            }
        }

        if (jumpingEnemy.getPosition().getX() >= 1000) {
            jumpingEnemy.setAccelerationY(2);
            jumpingEnemy.jumpToCoordwithVelocity(boi.getPosition().getX(), -5);
        } else if (jumpingEnemy.getPosition().getX() <= 50) {
            jumpingEnemy.setAccelerationY(1);
            jumpingEnemy.jumpToCoordwithVelocity(boi.getPosition().getX(), 20);
        }

    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);

        Graphics2D g2d = (Graphics2D) g;
        /*
        if (boi != null) {
            g2d.setColor(Color.red);
            g2d.draw(boi.getHitBox());
            for(DisplayObject child : boi.getChildren()) {
                g2d.setColor(Color.red);
                g2d.draw(child.getHitBox());
            }
        }
        */
    }

    public static void main(String[] args) {
        MovementTest game = new MovementTest();
        game.start();
    }

    public void handleEvent(Event e) {
        if (e.getEventType().equals("ATTACK_END")) {
            bossTimer.resetGameClock();
        }
    }

}
