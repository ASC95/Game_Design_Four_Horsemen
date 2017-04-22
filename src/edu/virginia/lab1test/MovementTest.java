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

    int bossHealth = 1000;
    int loadingFrames = 0;

    PhysicsSprite enemy = new PhysicsSprite("enemy", "standing", "stand.png");
    PhysicsSprite enemy2 = new PhysicsSprite("enemy2", "standing", "stand.png");
//    PhysicsSprite jumpingEnemy = new PhysicsSprite("jumpingEnemy", "standing", "stand.png");
    PhysicsSprite platform1 = new PhysicsSprite("platform1", "standing", "platform.png");
    PhysicsSprite platform2 = new PhysicsSprite("platform2", "standing", "platform.png");

    ActionSprite boss = new ActionSprite("boss", "standing", "bossPlaceholder1.png");

    private CollisionObjectContainer collisionManager = new CollisionObjectContainer("collisionManager");


    AttackHitbox bossAttack1 = new AttackHitbox("bossAttack1", "bossAttack1.png", 40, 0, 0, 0);
    AttackHitbox bossAttack2 = new AttackHitbox("bossAttack1", "bossAttack1.png", 40, 0, 0, 0);
    AttackHitbox bossAttack3 = new AttackHitbox("bossAttack1", "bossAttack1.png", 40, 0, 0, 0);

    AttackHitbox fireball1 = new AttackHitbox("bossAttack1", "bossAttack1.png", 30, 0, 0, 0);

    TweenJuggler juggler = new TweenJuggler();
    Tween bossPositionTween = new Tween(boss);
    Tween bossStingerTween = new Tween(boss);
    Tween fireballTween = new Tween(fireball1);

    GameClock bossTimer = new GameClock();
    GameClock bossMoveTimer = new GameClock();

    boolean bossWasHit;
    boolean bossMovingAction;


    // camera shit
    double camX;
    double camY;

    double SCREENSIZE_X;
    double SCREENSIZE_Y;
    double WORLDSIZE_X;
    double WORLDSIZE_Y;
    // max = worldsize - screensize
    double offsetMaxX;
    double offsetMaxY;
    double offsetMinX;
    double offsetMinY;


    public MovementTest() {
        super("Movement", 1280, 720);
        // 1920 - 1280 = 640
        // 1080 - 720 = 360
        boi.setPlayerControlled(true);
        platform1.setPosition(300, 300);
        collisionManager.addChild(boi);
        collisionManager.addChild(platform1);
        collisionManager.addChild(platform2);
        platform2.setPosition(600, 300);
        this.addChild(collisionManager);
        //this.addChild(platform1);

        this.addChild(boss);
        //this.addChild(boi);
        boi.setMaxHP(200);
        boi.setHealth(200);
        boi.setMaxMP(200);
        boi.setMana(200);

        boi.setGravity(2);
        boi.setPivotPoint(new Point(boi.getUnscaledWidth() / 2, boi.getUnscaledHeight() / 2));
        boi.setHitBox(0, 0, boi.getUnscaledWidth(), boi.getUnscaledHeight());
        boi.setPosition(1280 / 2, 540 + boi.getUnscaledHeight() / 2 + 1);

        boi.addEventListener(this, "ATTACK_END" + boi.getId());
        boi.addEventListener(this, "GOT_HIT");

//        this.addChild(jumpingEnemy);
//        jumpingEnemy.setPosition(50, 400);
//        jumpingEnemy.altMove = true;
//        jumpingEnemy.setAccelerationY(.09);
//        jumpingEnemy.jumpToCoordwithVelocity(700, 5);
//        jumpingEnemy.jumpToCoordwithVelocity(700, 5);
//        jumpingEnemy.setAccelerationY(.09);

        this.addChild(enemy);
        /*
        this.addChild(enemy2);
        */


        boss.setPivotPoint(new Point(boss.getUnscaledWidth() / 2, boss.getUnscaledHeight() / 2));
        boss.setHitBox(0, 0, boss.getUnscaledWidth(), boss.getUnscaledHeight());

        // how to set things based on bottom?
        // i want everything on the floor...

        enemy.setPosition(640, 540);
        /*
        enemy2.setPosition(1820, 900);
        */

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

        boss.addEventListener(this, "ATTACK_END" + boss.getId());
        boss.addEventListener(this, "BOSS_HIT");

        // camera initialization shit
        WORLDSIZE_X = 1280;
        WORLDSIZE_Y = 720;
        SCREENSIZE_X = 1280;
        SCREENSIZE_Y = 720;
        offsetMaxX = WORLDSIZE_X - SCREENSIZE_X;
        offsetMaxY = WORLDSIZE_Y - SCREENSIZE_Y;
        offsetMinX = 0;
        offsetMinY = 0;

    }

    @Override
    public void update(ArrayList<Integer> pressedKeys) {
        super.update(pressedKeys);
        if (juggler != null) {
            juggler.nextFrame();
        }
        if (boi != null) {
            if (boi.getVelocityX() == 0 && !boi.isJumping() && !boi.isFalling() && !boi.isAttacking()) {
                boi.animate("standing");
                boi.start();
            }

            if (boi.getPosition().getY() > 540 + boi.getUnscaledHeight() / 2) {
//                 set landing sets jumping false, falling false, velocityY 0, hasDJ true
                boi.setLanding();
                boi.setPosition(boi.getPosition().x, 540 + boi.getUnscaledHeight() / 2);
            }

//            if (jumpingEnemy.getPosition().getY() > 540 + jumpingEnemy.getUnscaledHeight() / 2) {
//                jumpingEnemy.setJumping(false);
//                jumpingEnemy.setFalling(false);
//                //jumpingEnemy.setDJ(true);
//                jumpingEnemy.setPosition(jumpingEnemy.getPosition().x, 540 + jumpingEnemy.getUnscaledHeight() / 2);
//                jumpingEnemy.setVelocityY(0);
//                jumpingEnemy.setAccelerationY(0);
//            }



        }
        if (boss != null && !boss.isAttacking() && !bossMovingAction) {
            if (bossTimer.getElapsedTime() > 3000) {
                boss.setVelocityX(0);
                if (bossTimer.getElapsedTime() * 1000000 % 3 == 0) {
                    // tween to one (closer) side of the stage

                    if (boss.getPosition().x > 640) {
                        // bossPositionTween.animate(TweenableParams.X, boss.getPosition().x + 1, 1180, Math.abs(boss.getPosition().x - 1180) / 60 * 30);
                        bossPositionTween.animate(TweenableParams.X, boss.getPosition().x, 1180, 1000 / 60 * 30);
                        juggler.add(bossPositionTween);
                        boss.setScaleX(-1);
                    } else {
                        // bossPositionTween.animate(TweenableParams.X, boss.getPosition().x + 1, 100, boss.getPosition().x / 60 * 30);
                        bossPositionTween.animate(TweenableParams.X, boss.getPosition().x, 100, 1000 / 60 * 30);
                        juggler.add(bossPositionTween);
                        boss.setScaleX(1);
                    }
                    boss.setAttack("stinger");
                    boss.animate("stinger");
                    boss.start();
                    boss.startAttack();
                } else if (bossTimer.getElapsedTime() * 1000000 % 3 == 1) {
                    /*
                    if (boss.getPosition().x < boi.getPosition().x) {
                        boss.setScaleX(1);
                    } else {
                        boss.setScaleX(-1);
                    }
                    */
                    // set animation to some "ready" stance
                    boss.setAttack("slash");
                    /*
                    boss.animate("slash");
                    boss.start();
                    */
                    bossMovingAction = true;
                    // boss.startAttack();
                } else if (Math.abs(boi.getPosition().x - boss.getPosition().x) > 500){
                    if (boss.getPosition().x < boi.getPosition().x) {
                        boss.setScaleX(1);
                    } else {
                        boss.setScaleX(-1);
                    }
                    boss.setAttack("fireball");
                    boss.animate("fireball");
                    boss.start();
                    boss.startAttack();
                } else {
                    // nothing was chosen
                    System.out.println("This should be removed!");
                    // bossTimer.resetGameClock();
                }
            } else {
                boss.animate("standing");
                boss.start();
                /*
                if (bossMoveTimer.getElapsedTime() > 600) {
                    if (bossMoveTimer.getElapsedTime() * 1000000 % 3 == 0) {
                        if (boss.getPosition().x > boi.getPosition().x) {
                            boss.setVelocityX(4); // away
                        } else {
                            boss.setVelocityX(-4); // towards
                        }
                    } else {
                        if (boss.getPosition().x > boi.getPosition().x) {
                            boss.setVelocityX(-4);
                        } else {
                            boss.setVelocityX(4);
                        }
                    }
                    bossMoveTimer.resetGameClock();
                }
                */
            }
        }

        if (boss != null && !boss.isAttacking() && bossMovingAction) {
            // if currentAction.equals("slash")
            if (boss.getVelocityX() == 0) {
                if (boi.getPosition().x > boss.getPosition().x) {
                    boss.setVelocityX(6);
                    boss.setScaleX(1);
                } else {
                    boss.setVelocityX(-6);
                    boss.setScaleX(-1);
                }
            }
            if (Math.abs(boi.getPosition().x - boss.getPosition().x) < 300) {
                boss.startAttack();
                boss.animate("slash");
                boss.start();
                boss.setVelocityX(0);
                bossMovingAction = false;
            }
        }

        // if (boss != null && boss.getVelocityX() != 0) System.out.println("boss moving");

        if (boss != null && boss.isAttacking()) {
            // frame counter checks are cheaper?
            if (boss.getFrameCounter() == 60 && boss.getCurrentAction().equals("stinger")) {
                // bossStingerTween.animate(TweenableParams.X, boss.getPosition().x, 1280 - boss.getPosition().x, 20 * 18);
                bossStingerTween.animate(TweenableParams.X, boss.getPosition().x, 1280 - boss.getPosition().x, 1000 / 60 * 20);
                juggler.add(bossStingerTween);
            }
            if (boss.getFrameCounter() == 40 && boss.getCurrentAction().equals("fireball")) {
                // fireballTween.animate(TweenableParams.X, boss.getUnscaledWidth() / 2, 1280, 40 * 21.33);
                fireballTween.animate(TweenableParams.X, boss.getUnscaledWidth() / 2, 1280, 1000 / 60 * 40);
                juggler.add(fireballTween);
            }
        }

        // bounds for boss
        if (boss != null) {
            if (boss.getPosition().x < 100) {
                // should be set lol
                // boss.getPosition().x = 100;
            }
            if (boss.getPosition().x > 1180) {
                // boss.getPosition().x = 1180;
            }
        }

        if (boi != null) {
            if (bossAttack1.collidesWith(boi)) {
                boi.dispatchEvent(new Event("GOT_HIT", bossAttack1));
            } else if (bossAttack2.collidesWith(boi)) {
                boi.dispatchEvent(new Event("GOT_HIT", bossAttack2));
            } else if (bossAttack3.collidesWith(boi)) {
                boi.dispatchEvent(new Event("GOT_HIT", bossAttack3));
            } else if (fireball1.collidesWith(boi)) {
                boi.dispatchEvent(new Event("GOT_HIT", fireball1));
            }
        }

        if (boss != null && !bossWasHit) {
            for (DisplayObject hitbox : boi.getChildren()) {
                if (hitbox.collidesWith(boss)) {
                    boss.dispatchEvent(new Event("BOSS_HIT", hitbox));
                    // this prevents hitting more than once with single attack
                    break;
                }
            }
        }
//        if (jumpingEnemy != null) {
//            if (jumpingEnemy.getPosition().getX() >= 1000) {
//                jumpingEnemy.setAccelerationY(2);
//                jumpingEnemy.jumpToCoordwithVelocity(boi.getPosition().getX(), -5);
//            } else if (jumpingEnemy.getPosition().getX() <= 50) {
//                jumpingEnemy.setAccelerationY(1);
//                jumpingEnemy.jumpToCoordwithVelocity(boi.getPosition().getX(), 20);
//            }
//        }

        // camera update
        if (boi != null) {
            camX = boi.getPosition().x - SCREENSIZE_X / 2;
            camY = boi.getPosition().y - SCREENSIZE_Y / 2;
        }
        if (camX > offsetMaxX)
            camX = offsetMaxX;
        else if (camX < offsetMinX)
            camX = offsetMinX;

        if (camY > offsetMaxY)
            camY = offsetMaxY;
        else if (camY < offsetMinY)
            camY = offsetMinY;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
//        g2d.draw(boi.getHitBox());
//        g2d.draw(platform1.getHitBox());
//        g2d.draw(platform2.getHitBox());
//        loadingFrames++;
        // camera translation
        g.translate((int)-camX, (int)-camY);
        // draw everything but GUI
        super.draw(g);
        // change back
        //g.translate((int)camX, (int)camY);

        g2d.setColor(Color.blue);
        g2d.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        if (boi != null) g2d.drawString("Player HP: " + boi.getHealth(), 100, 100);
        if (boi != null) g2d.drawString("Player MP: " + (int)boi.getMana(), 100, 200);
        g2d.setColor(Color.red);
        g2d.drawString("Boss HP: " + bossHealth, 1100, 100);
        /*
        if (boi != null) {
            g2d.setColor(Color.red);
            g2d.draw(boi.getHitBox());
            /*
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
        if (e.getEventType().equals("ATTACK_END" + boss.getId())) {
            bossTimer.resetGameClock();
            bossMoveTimer.resetGameClock();
        }
        if (e.getEventType().equals("ATTACK_END" + boi.getId())) {
            bossWasHit = false;
        }
        /*
        if (e.getEventType().equals("GOT_HIT")) {
        }
        */
        if (e.getEventType().equals("BOSS_HIT")) {
            bossWasHit = true;
            AttackHitbox x = (AttackHitbox) e.getSource();
            bossHealth -= x.getDamage();
        }
    }

}
