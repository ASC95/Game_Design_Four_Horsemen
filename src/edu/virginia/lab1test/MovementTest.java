package edu.virginia.lab1test;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.display.*;
import edu.virginia.engine.events.IEventListener;
import edu.virginia.engine.util.GameClock;
import edu.virginia.engine.util.SoundManager;
import javafx.application.Platform;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by Resquall on 4/9/2017.
 */
public class MovementTest extends Game implements IEventListener {

    Player boi = new Player("boi", "standing", "standing mc.png");

    int bossHealth = 1000;
    int maxBossHealth = 1000;
    int loadingFrames = 0;

    Sprite background = new Sprite("background", "warBackground.png");
    PhysicsSprite enemy = new PhysicsSprite("enemy", "standing", "stand.png");
    PhysicsSprite enemy2 = new PhysicsSprite("enemy2", "standing", "stand.png");
//    PhysicsSprite platform1 = new PhysicsSprite("platform1", "standing", "platform.png");
//    PhysicsSprite platform2 = new PhysicsSprite("platform2", "standing", "platform.png");

    Rectangle boiHealth = new Rectangle(100, 100, 400, 10);

    ActionSprite boss = new ActionSprite("boss", "standing", "war.png");

    private CollisionObjectContainer collisionManager = new CollisionObjectContainer("collisionManager");

    AttackHitbox bossAttack1 = new AttackHitbox("bossAttack1", "bossAttack1.png", 40, 0, 0, 0);
    AttackHitbox bossAttack2 = new AttackHitbox("bossAttack1", "bossAttack1.png", 40, 0, 0, 0);
    AttackHitbox bossAttack3 = new AttackHitbox("bossAttack1", "bossAttack1.png", 40, 0, 0, 0);

    AttackHitbox fireball1 = new AttackHitbox("bossAttack1", "fireball.png", 30, 0, 0, 0);

    TweenJuggler juggler = new TweenJuggler();
    Tween bossPositionTween = new Tween(boss);
    Tween bossStingerTween = new Tween(boss);
    Tween fireballTween = new Tween(fireball1);

    GameClock bossTimer = new GameClock();
    GameClock bossMoveTimer = new GameClock();

    private SoundManager soundManager = new SoundManager();

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
        super("War", 1280, 720);
        // 1920 - 1280 = 640
        // 1080 - 720 = 360
//         see physics sprite notes.
//        platform1.setPosition(300, 300);
//        collisionManager.addChild(boi);
//        collisionManager.addChild(platform1);
//        collisionManager.addChild(platform2);
//        platform2.setPosition(600, 300);
//        this.addChild(collisionManager);
//        this.addChild(platform1)

        this.addChild(background);
        this.addChild(boss);
        this.addChild(boi);

        boi.setMaxHP(200);
        boi.setHealth(200);
        boi.setMaxMP(200);
        boi.setMana(200);

        boi.setGravity(2);
        /*
        boi.setPivotPoint(new Point(boi.getUnscaledWidth() / 2, boi.getUnscaledHeight() / 2));
        boi.setHitBox(0, 0, boi.getUnscaledWidth(), boi.getUnscaledHeight());
        */
        boi.setPosition(1280 / 2, 540 + boi.getUnscaledHeight() / 2 + 1);

        boi.addEventListener(this, "ATTACK_END" + boi.getId());
        boi.addEventListener(this, "GOT_HIT");
        boi.addEventListener(soundManager, "BOI_INJURED_0");//boi listens for when he is injured
        boi.addEventListener(soundManager, "BOI_HEALED");//soundManager listens for when boi heals
        boi.addEventListener(soundManager, "BOI_DASH");

//        this.addChild(enemy);
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

        Action bossStinger = new Action(40 + 20 + 30, 90, 90);
        for (int i = 41; i < 61; i++) {
            bossStinger.addHitboxes(bossAttack1, i);
            bossStinger.addHitboxes(bossAttack2, i);
            bossStinger.addHitboxes(bossAttack3, i);
        }

        Action bossSlash1 = new Action(0, 59, 60);
        bossSlash1.addHitboxes(bossAttack2, 10);
        bossSlash1.addHitboxes(bossAttack3, 10);
        bossSlash1.addHitboxes(bossAttack3, 11);
        bossSlash1.addHitboxes(bossAttack3, 12);
        bossSlash1.addHitboxes(bossAttack1, 12);

        Action bossFireball1 = new Action(20 + 30 + 30, 80, 80);
        for (int i = 21; i < 51; i++) {
            bossFireball1.addHitboxes(fireball1, i);
        }

        boss.addAttack("stinger", bossStinger);
        boss.addAttack("slash", bossSlash1);
        boss.addAttack("fireball", bossFireball1);

        boss.addEventListener(this, "ATTACK_END" + boss.getId());
        boss.addEventListener(this, "BOSS_HIT");
        boss.addEventListener(soundManager, "BOSS_HIT");//soundManager listens for when boss gets hit
        boss.addEventListener(soundManager, "BOSS_DASH");//soundManager listens for when boss dashes
        boss.addEventListener(soundManager, "BOSS_FIREBALL");//soundManager listens for fireball
        boss.addEventListener(soundManager, "BOSS_SLASH");//soundManager listens for boss's slash

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
            if (boi.getPosition().getX() < 0) {
                boi.setPosition(0, (int) boi.getPosition().getY());
            }
            if (boi.getPosition().getX() > 1280) {
                boi.setPosition(1280, (int) boi.getPosition().getY());
            }
        }

        if (boi != null) {
//            if (boi.getVelocityX() == 0 && !boi.isJumping() && !boi.isFalling() && !boi.isAttacking()) {
            if (!boi.isFalling() && !boi.isJumping() && !boi.isAttacking() && !pressedKeys.contains(KeyEvent.VK_LEFT) && !pressedKeys.contains(KeyEvent.VK_RIGHT)) {
                boi.animate("standing");
                boi.start();
            }
            if (boi.isJumping()) {
                boi.animate("jumping");
            }

            if (boi.getPosition().getY() > 540 + 137 / 2) {
//                 set landing sets jumping false, falling false, velocityY 0, hasDJ true
                boi.setLanding();
                boi.setPosition(boi.getPosition().x, 540 + 137 / 2);
            }
            // bounds checking
            if (boi.getPosition().x < 62) boi.setPosition(62, boi.getPosition().y);
            if (boi.getPosition().x > 1218) boi.setPosition(1218, boi.getPosition().y);

        }
        if (boss != null && !boss.isAttacking() && !bossMovingAction) {
            if (bossTimer.getElapsedTime() > 2000) {
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
                    boss.animate("slash");
                    boss.start();
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
                    boss.setVelocityX(10);
                    boss.setScaleX(1);
                } else {
                    boss.setVelocityX(-10);
                    boss.setScaleX(-1);
                }
            }
            if (Math.abs(boi.getPosition().x - boss.getPosition().x) < 300) {
                boss.startAttack();
                boss.animate("slash");
                boss.dispatchEvent(new Event("BOSS_SLASH", boss));
                boss.start();
                boss.setVelocityX(0);
                bossMovingAction = false;
            }
        }

        // if (boss != null && boss.getVelocityX() != 0) System.out.println("boss moving");

        if (boss != null && boss.isAttacking()) {
            // frame counter checks are cheaper?
            if (boss.getFrameCounter() == 40 && boss.getCurrentAction().equals("stinger")) {
                // bossStingerTween.animate(TweenableParams.X, boss.getPosition().x, 1280 - boss.getPosition().x, 20 * 18);
                bossStingerTween.animate(TweenableParams.X, boss.getPosition().x, 1280 - boss.getPosition().x, 1000 / 60 * 20);
                juggler.add(bossStingerTween);
                boss.dispatchEvent(new Event("BOSS_DASH", boss));
            }
            if (boss.getFrameCounter() == 20 && boss.getCurrentAction().equals("fireball")) {
                // fireballTween.animate(TweenableParams.X, boss.getUnscaledWidth() / 2, 1280, 40 * 21.33);
                fireballTween.animate(TweenableParams.X, boss.getUnscaledWidth() / 2, 1280, 1000 / 60 * 40);
                boss.dispatchEvent(new Event("BOSS_FIREBALL", boss));
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
            if (!boi.isInvincible()) {
                if (bossAttack1.collidesWith(boi)) {
                    boi.dispatchEvent(new Event("GOT_HIT", bossAttack1));
                    boi.dispatchEvent(new Event("BOI_INJURED_0", bossAttack1));
                } else if (bossAttack2.collidesWith(boi)) {
                    boi.dispatchEvent(new Event("GOT_HIT", bossAttack2));
                    boi.dispatchEvent(new Event("BOI_INJURED_0", bossAttack1));
                } else if (bossAttack3.collidesWith(boi)) {
                    boi.dispatchEvent(new Event("GOT_HIT", bossAttack3));
                    boi.dispatchEvent(new Event("BOI_INJURED_0", bossAttack1));
                } else if (fireball1.collidesWith(boi)) {
                    boi.dispatchEvent(new Event("GOT_HIT", fireball1));
                    boi.dispatchEvent(new Event("BOI_INJURED_0", bossAttack1));
                }
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

        if (boi != null) {
            if (boi.getHealth() <= 0) {
                dispatchEvent(new Event("gameOver", this));
                super.exitGame();
            }
        }
        if (boss != null) {
            if (bossHealth <= 0) {
                dispatchEvent(new Event("victory", this));
                super.exitGame();
            }
        }
    }

    /**
     * This is needed because you need to switch to the JavaFX thread before you can change data in the
     * JavaFX application.
     * @param event
     */
    @Override
    public void dispatchEvent(Event event) {
        if (event.getEventType().equals("gameOver") || event.getEventType().equals("victory")) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    MovementTest.super.dispatchEvent(event);
                }
            });
        } else {
            super.dispatchEvent(event);
        }
    }


    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
//        g2d.draw(boi.getHitBox());
//        g2d.draw(platform1.getHitBox());
//        g2d.draw(platform2.getHitBox());
        loadingFrames++;
        // camera translation
        g.translate((int)-camX, (int)-camY);
        // draw everything but GUI
        super.draw(g);
        // change back
        g.translate((int)camX, (int)camY);
        if (boi != null) {
            drawHealthBars(g2d);
        }

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
//        if (boi != null) {
//            g2d.setColor(Color.red);
//            g2d.draw(boi.getHitBox());
//        }
    }

    private void drawHealthBars(Graphics2D g2d) {
        if (boi.getHealth() > 100) {
            g2d.setColor(Color.GREEN);
        } else if (boi.getHealth() < 100 && boi.getHealth() > 50) {
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.RED);
        }
        g2d.fillRect(100, 100, boi.getHealth(), 20);
        g2d.setColor(Color.blue);
        g2d.fillRect(100, 150, (int)boi.getMana(), 20);
        if (bossHealth > 500) {
            g2d.setColor(Color.GREEN);
        } else if (bossHealth < 500 && bossHealth > 250) {
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.RED);
        }
        int bossHealthPosition = 735 + (maxBossHealth - bossHealth)/2;
        g2d.fillRect(bossHealthPosition, 100, bossHealth/2, 20);

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
            // TODO: game over condition goes here
        }
        */
        if (e.getEventType().equals("BOSS_HIT")) {
            bossWasHit = true;
            AttackHitbox x = (AttackHitbox) e.getSource();
            bossHealth -= x.getDamage();
            // TODO: victory condition goes here
        }
    }

}
