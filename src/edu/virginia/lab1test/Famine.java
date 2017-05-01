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
public class Famine extends Game implements IEventListener {

    /**
     * Sounds, shaking (damage indicator), tutorial, menu
     * later: animations
     */

    Player boi = new Player("boi", "standing", "standing mc.png");

    int bossHealth = 1000;
    int maxBossHealth = 1000;
    int loadingFrames = 0;

    Sprite background = new Sprite("background", "warBackground.png");

    Sprite platform1 = new Sprite("plat1", "platform2.png");
    Sprite platform2 = new Sprite("plat2", "platform2.png");
    ArrayList<Sprite> platforms = new ArrayList<>();

    ActionSprite boss = new ActionSprite("boss", "standing", "bossPlaceholder1.png");
    boolean pillarAttackActive = false;
    boolean pillarsReversed = false;
    boolean launchPillarAttack = true;
    boolean pillarAttackFinished = false;

    AnimatedSprite lightning = new AnimatedSprite("lightning", "lightningList", "lightning0.png");
    AnimatedSprite lightning2 = new AnimatedSprite("lightning2", "lightningList", "lightning0.png");
    AttackHitbox lightningColumnHitBox = new AttackHitbox("lightningColumnHitBox", "lightning0.png", 40, 0, 0, 0);
    AttackHitbox pillarLeft = new AttackHitbox("pillarLeft", "lightningSkinny.png", 40, 0, 0, 0);
    AttackHitbox pillarRight = new AttackHitbox("pillarRight", "lightningSkinny.png", 40, 0, 0, 0);
    Tween leftPillarTween = new Tween(pillarLeft);
    Tween rightPillarTween = new Tween(pillarRight);


    AttackHitbox bossAttack2 = new AttackHitbox("bossAttack1", "bossAttack1.png", 40, 0, 0, 0);
    AttackHitbox bossAttack3 = new AttackHitbox("bossAttack1", "bossAttack1.png", 40, 0, 0, 0);
//    AttackHitbox fireball1 = new AttackHitbox("bossAttack1", "bossAttack1.png", 30, 0, 0, 0);

    //    AttackHitbox columnLeftHitBox = new AttackHitbox("bossAttack1", "lightningtest.png", 30, 0, 0, 0);
    AttackHitbox columnRightHitBox = new AttackHitbox("bossAttack1", "bossAttack1.png", 30, 0, 0, 0);
    //    Tween columnLeftTween = new Tween(columnLeftHitBox);
    Tween columnRightTween = new Tween(columnRightHitBox);


    TweenJuggler juggler = new TweenJuggler();
    Tween bossPositionTween = new Tween(boss);
    Tween bossStingerTween = new Tween(boss);

    //    Tween fireballTween = new Tween(fireball1);


    GameClock bossTimer = new GameClock();
    GameClock bossMoveTimer = new GameClock();

    private SoundManager soundManager = new SoundManager();

    boolean bossWasHit;
    boolean bossMovingAction;

    // camera shit
//    double camX;
//    double camY;
//
//    double SCREENSIZE_X;
//    double SCREENSIZE_Y;
//    double WORLDSIZE_X;
//    double WORLDSIZE_Y;
//    // max = worldsize - screensize
//    double offsetMaxX;
//    double offsetMaxY;
//    double offsetMinX;
//    double offsetMinY;


    public Famine() {
        super("Famine", 1280, 720);
        // 1920 - 1280 = 640
        // 1080 - 720 = 360

        this.addChild(background);
        this.addChild(lightning);
        this.addChild(lightning2);
        this.addChild(boss);
        this.addChild(boi);

        lightning.setPosition(0, 0);
        lightning.addImageWithoutSheet("lightningList", "lightning1.png");
        lightning.addImageWithoutSheet("lightningList", "lightning2.png");
        lightning.addImageWithoutSheet("lightningList", "lightning3.png");
        lightning.addImageWithoutSheet("lightningList", "lightning4.png");
        lightning.addImageWithoutSheet("lightningList", "lightning5.png");
        lightning.addImageWithoutSheet("lightningList", "lightning6.png");
        lightning.setSpeed(2);
        lightning.setCollidable(false);
        lightning.setVisible(false);

        lightning2.setPosition(1280, 0);
        lightning2.addImageWithoutSheet("lightningList", "lightning1.png");
        lightning2.addImageWithoutSheet("lightningList", "lightning2.png");
        lightning2.addImageWithoutSheet("lightningList", "lightning3.png");
        lightning2.addImageWithoutSheet("lightningList", "lightning4.png");
        lightning2.addImageWithoutSheet("lightningList", "lightning5.png");
        lightning2.addImageWithoutSheet("lightningList", "lightning6.png");
        lightning2.setSpeed(2);
        lightning2.setCollidable(false);
        lightning2.setVisible(false);

        boi.setMaxHP(200);
        boi.setHealth(200);
        boi.setMaxMP(200);
        boi.setMana(200);

        boi.setGravity(2);
//        boi.setPivotPoint(new Point(boi.getUnscaledWidth() / 2, boi.getUnscaledHeight() / 2));
//        boi.setHitBox(60, 0, boi.getUnscaledWidth() - 120, boi.getUnscaledHeight());
        boi.setPosition(1280 / 2, 540 + boi.getUnscaledHeight() / 2 + 1);

        boi.addEventListener(this, "ATTACK_END" + boi.getId());
        boi.addEventListener(this, "GOT_HIT");
        boi.addEventListener(soundManager, "BOI_INJURED_0");//boi listens for when he is injured
        boi.addEventListener(soundManager, "BOI_HEALED");//soundManager listens for when boi heals
        boi.addEventListener(soundManager, "BOI_DASH");

        boss.setPivotPoint(new Point(boss.getUnscaledWidth() / 2, boss.getUnscaledHeight() / 2));
        boss.setHitBox(0, 0, boss.getUnscaledWidth(), boss.getUnscaledHeight());
        boss.setPosition(100, 540);

        Action lightningColumnAction = new Action(30, 30, 30);
        lightningColumnAction.addHitboxes(lightningColumnHitBox, 15);
        boss.addAttack("lightningColumnAttack", lightningColumnAction);

        boss.addEventListener(soundManager, "LIGHTNING_STRIKE");
        boss.addEventListener(soundManager, "LIGHTNING_ARC");
        boss.addEventListener(soundManager, "STOP_LIGHTNING_ARC");

//        boss.addImage("stinger", "bossPlaceholder2.png", 1, 1);
//        boss.addImage("slash", "bossPlaceholder3.png", 1, 1);
//        boss.addImage("fireball", "bossPlaceHolder4.png", 1, 1);
//        boss.addImage("columnAttackAction", "bossPlaceholder2.png", 1, 1);

//        boss.addChild(bossAttack1);
//        bossAttack1.setPosition(boss.getUnscaledWidth() / 2, 0);
//
//        boss.addChild(bossAttack2);
//        bossAttack2.setPosition(boss.getUnscaledWidth() / 2, -boss.getUnscaledHeight() / 2);
//        boss.addChild(bossAttack3);
//        bossAttack3.setPosition(boss.getUnscaledWidth(), -boss.getUnscaledHeight() / 4);

//        boss.addChild(fireball1);
//        fireball1.setPosition(boss.getUnscaledWidth(), -boss.getUnscaledHeight() / 4);
//        boss.addChild(columnHitbox);
//        columnHitbox.setPosition(boss.getUnscaledWidth(), -boss.getUnscaledHeight() / 4);

//        Action bossStinger = new Action(60 + 20 + 30, 110, 110);
//        for (int i = 61; i < 81; i++) {
//            bossStinger.addHitboxes(bossAttack1, i);
//            bossStinger.addHitboxes(bossAttack2, i);
//            bossStinger.addHitboxes(bossAttack3, i);
//        }

//        Action bossSlash1 = new Action(0, 59, 60);
//        bossSlash1.addHitboxes(bossAttack2, 40);
//        bossSlash1.addHitboxes(bossAttack3, 40);
//        bossSlash1.addHitboxes(bossAttack3, 41);
//        bossSlash1.addHitboxes(bossAttack3, 42);
//        bossSlash1.addHitboxes(bossAttack1, 42);

//        Action bossFireball1 = new Action(40 + 30 + 30, 100, 100);
//        for (int i = 41; i < 71; i++) {
//            bossFireball1.addHitboxes(fireball1, i);
//        }
//        Action columnAction = new Action(100, 100, 40);
//        for (int i = 10; i < 40; i++) {
//            columnAction.addHitboxes(columnHitbox, i);
//        }

//        boss.addAttack("stinger", bossStinger);
//        boss.addAttack("slash", bossSlash1);
//        boss.addAttack("fireball", bossFireball1);


        boss.addEventListener(this, "ATTACK_END" + boss.getId());
        boss.addEventListener(this, "BOSS_HIT");
        boss.addEventListener(soundManager, "BOSS_HIT");//soundManager listens for when boss gets hit
//        boss.addEventListener(soundManager, "BOSS_DASH");//soundManager listens for when boss dashes
        boss.addEventListener(soundManager, "BOSS_FIREBALL");//soundManager listens for fireball
//        boss.addEventListener(soundManager, "BOSS_SLASH");//soundManager listens for boss's slash

    }

    @Override
    public void update(ArrayList<Integer> pressedKeys) {
        super.update(pressedKeys);
        if (juggler != null) {
            juggler.nextFrame();
        }

        if (bossTimer != null && !boss.isAttacking() && !pillarAttackActive) {
            if (bossTimer.getElapsedTime() > 1200 && Math.abs(boi.getPosition().getX() - boss.getPosition().getX()) > 300) {
                boss.setAttack("lightningColumnAttack");
                boss.startAttack();
            } else if (bossTimer.getElapsedTime() > 2000 && Math.abs(boi.getPosition().getX() - boss.getPosition().getX()) < 300) {
                boss.setCollidable(false);
                pillarAttackActive = true;
                Tween bossDisappearTween = new Tween(boss);
                bossDisappearTween.animate(TweenableParams.ALPHA, 1.0, 0, 1000);
                juggler.add(bossDisappearTween);
            }
        }

        if (boss != null) {
            if (boss.isAttacking()) {
                if (boss.getCurrentAction().equals("lightningColumnAttack")) {
                    if (boss.getFrameCounter() == 1) {
                        lightningColumnHitBox.setPosition((int) boi.getPosition().getX() - lightningColumnHitBox.getUnscaledWidth() / 2, 0);
                        lightning.setPosition((int) boi.getPosition().getX() - lightning.getUnscaledWidth() / 2, 0);
                    }
                    if (boss.getFrameCounter() == 15) {
                        lightning.setVisible(true);
                        lightning.start();
                        boss.dispatchEvent(new Event("LIGHTNING_STRIKE", boss));
                    }
                    if (boss.getFrameCounter() > 15 + lightning.getNumFrames("lightningList") * lightning.getSpeed()) {
                        lightning.stop();
                        lightning.setCurrentFrame(0);
                        lightning.setVisible(false);
                    }
                }
            }
            if (pillarAttackActive && !boss.isAttacking()) {//don't do two attacks at once
                if (launchPillarAttack) {
                    pillarLeft.setCollidable(true);
                    pillarRight.setCollidable(true);
                    launchPillarAttack = false;
                    lightning.setPosition(0, 0);
                    lightning.setVisible(true);
                    lightning.start();
                    lightning2.setPosition(1000, 0);
                    lightning2.setVisible(true);
                    lightning2.start();
                    pillarLeft.setPosition(0, 0);
                    pillarRight.setPosition(1280, 0);
                    leftPillarTween.animate(TweenableParams.X, 0, 640 - pillarLeft.getUnscaledWidth() / 2, 2400);
                    rightPillarTween.animate(TweenableParams.X, 1280 - pillarRight.getUnscaledWidth(), 640 - pillarRight.getUnscaledWidth() / 2, 2400);
                    juggler.add(leftPillarTween);
                    juggler.add(rightPillarTween);
                    boss.dispatchEvent(new Event("LIGHTNING_ARC", boss));
                } else {
                    lightning.setPosition((int) pillarLeft.getPosition().getX() + pillarLeft.getUnscaledWidth() / 2 - lightning.getUnscaledWidth() / 2, 0);
                    lightning2.setPosition((int) pillarRight.getPosition().getX() + pillarRight.getUnscaledWidth() / 2 - lightning2.getUnscaledWidth() / 2, 0);
                    if (leftPillarTween.isComplete() && !pillarsReversed) {
                        leftPillarTween.animate(TweenableParams.X, pillarLeft.getPosition().getX(), 0, 1200);
                        rightPillarTween.animate(TweenableParams.X, pillarRight.getPosition().getX(), 1280 - pillarRight.getUnscaledWidth(), 1200);
                        juggler.add(rightPillarTween);
                        juggler.add(leftPillarTween);
                        leftPillarTween.setComplete(false);
                        rightPillarTween.setComplete(false);
                        pillarsReversed = true;
                    }
                    if (leftPillarTween.isComplete() && pillarsReversed) {
                        pillarAttackActive = false;
                        pillarAttackFinished = true;
                    }
                }
            }
            if (pillarAttackFinished) {
                pillarAttackFinished = false;
                pillarLeft.setCollidable(false);
                pillarRight.setCollidable(false);
                lightning.setVisible(false);
                lightning.stop();
                lightning.setCurrentFrame(0);
                lightning2.setVisible(false);
                lightning2.stop();
                lightning2.setCurrentFrame(0);
                pillarsReversed = false;
                launchPillarAttack = true;
                boss.dispatchEvent(new Event("STOP_LIGHTNING_ARC", boss));
                if (boi.getPosition().getX() < 640) {
                    boss.setPosition(1180, (int) boss.getPosition().getY());
                } else {
                    boss.setPosition(100, (int) boss.getPosition().getY());
                }
                Tween bossReappearTween = new Tween(boss);
                bossReappearTween.animate(TweenableParams.ALPHA, 0, 1.0, 1000);
                juggler.add(bossReappearTween);
                boss.setCollidable(true);
            }
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
            if (!boi.isJumping() && !boi.isAttacking() && !pressedKeys.contains(KeyEvent.VK_LEFT) && !pressedKeys.contains(KeyEvent.VK_RIGHT)) {
                boi.animate("standing");
                boi.start();
            }
            if (boi.isJumping()) {
                boi.animate("jumping");
            }

            if (boi.getPosition().getY() > 500 + boi.getUnscaledHeight() / 2) {
                boi.setLanding();
                boi.setPosition(boi.getPosition().x, 500 + boi.getUnscaledHeight() / 2);
            }
        }

        if (boi != null) {
            if (!boi.isInvincible()) {
                if (lightningColumnHitBox.collidesWith(boi)) {
                    boi.dispatchEvent(new Event("GOT_HIT", lightningColumnHitBox));
                    boi.dispatchEvent(new Event("BOI_INJURED_0", lightningColumnHitBox));
                } else if (pillarLeft.collidesWith(boi)) {
                    boi.dispatchEvent(new Event("GOT_HIT", pillarLeft));
                    boi.dispatchEvent(new Event("BOI_INJURED_0", pillarLeft));
                } else if (pillarRight.collidesWith(boi)) {
                    boi.dispatchEvent(new Event("GOT_HIT", pillarRight));
                    boi.dispatchEvent(new Event("BOI_INJURED_0", pillarRight));
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
     *
     * @param event
     */
    @Override
    public void dispatchEvent(Event event) {
        if (event.getEventType().equals("gameOver") || event.getEventType().equals("victory")) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Famine.super.dispatchEvent(event);
                }
            });
        } else {
            super.dispatchEvent(event);
        }
    }


    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        // camera translation
//        g.translate((int) -camX, (int) -camY);
        // draw everything but GUI
        super.draw(g);
        // change back
//        g.translate((int) camX, (int) camY);
        if (boi != null) {
            drawHealthBars(g2d);
        }

        g2d.setColor(Color.blue);
        g2d.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        if (boi != null) g2d.drawString("Player HP: " + boi.getHealth(), 100, 100);
        if (boi != null) g2d.drawString("Player MP: " + (int) boi.getMana(), 100, 200);
        g2d.setColor(Color.red);
        g2d.drawString("Boss HP: " + bossHealth, 1100, 100);

        if (boi != null) {
            g2d.setColor(Color.red);
            g2d.draw(boi.getHitBox());
        }
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
        g2d.fillRect(100, 150, (int) boi.getMana(), 20);
        if (bossHealth > 500) {
            g2d.setColor(Color.GREEN);
        } else if (bossHealth < 500 && bossHealth > 250) {
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.RED);
        }
        int bossHealthPosition = 735 + (maxBossHealth - bossHealth) / 2;
        g2d.fillRect(bossHealthPosition, 100, bossHealth / 2, 20);

    }

    public static void main(String[] args) {
        Famine game = new Famine();
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
