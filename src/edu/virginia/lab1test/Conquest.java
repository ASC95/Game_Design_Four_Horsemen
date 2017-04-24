package edu.virginia.lab1test;

import edu.virginia.engine.display.*;
import edu.virginia.engine.events.IEventListener;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.util.GameClock;
import javafx.application.Platform;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by Resquall on 4/21/2017.
 */
public class Conquest extends Game implements IEventListener {
    Player boi = new Player("boi", "standing", "standing.png");
    ActionSprite boss = new ActionSprite("boss", "standing", "bossPlaceholder1.png");
    Sprite platform1 = new Sprite("plat1", "platform2.png");

    // attack hitboxes for boss go here
    AttackHitbox bossAttack1 = new AttackHitbox("bulletHell1", "arrow.png", 30, 0, 0, 0);
    AttackHitbox bossAttack2 = new AttackHitbox("bulletHell1", "arrow.png", 30, 0, 0, 0);
    AttackHitbox bossAttack3 = new AttackHitbox("bulletHell1", "arrow.png", 30, 0, 0, 0);
    AttackHitbox bossAttack4 = new AttackHitbox("bulletHell1", "arrow.png", 30, 0, 0, 0);
    AttackHitbox bossAttack5 = new AttackHitbox("bulletHell1", "arrow.png", 30, 0, 0, 0);
    AttackHitbox bossAttack6 = new AttackHitbox("bulletHell1", "arrow.png", 30, 0, 0, 0);
    AttackHitbox bossAttack7 = new AttackHitbox("bulletHell1", "arrow.png", 30, 0, 0, 0);
    AttackHitbox bossAttack8 = new AttackHitbox("bulletHell1", "arrow.png", 30, 0, 0, 0);
    AttackHitbox bossAttack9 = new AttackHitbox("bulletHell1", "arrow.png", 30, 0, 0, 0);
    AttackHitbox bossAttack10 = new AttackHitbox("bulletHell1", "arrow.png", 30, 0, 0, 0);
    AttackHitbox bossAttack11 = new AttackHitbox("bulletHell1", "arrow.png", 30, 0, 0, 0);
    AttackHitbox bossAttack12 = new AttackHitbox("bulletHell1", "arrow.png", 30, 0, 0, 0);

    AttackHitbox bossStraightArrow = new AttackHitbox("straight", "arrow.png", 40, 0, 0, 0);

    TweenJuggler juggler = new TweenJuggler();
    // tweens
    /*
    Tween bossAtk1 = new Tween(bossAttack1);
    Tween bossAtk2 = new Tween(bossAttack2);
    Tween bossAtk3 = new Tween(bossAttack3);
    Tween bossAtk4 = new Tween(bossAttack4);
    Tween bossAtk5 = new Tween(bossAttack5);
    Tween bossAtk6 = new Tween(bossAttack6);
    Tween bossAtk7 = new Tween(bossAttack7);
    Tween bossAtk8 = new Tween(bossAttack8);
    Tween bossAtk9 = new Tween(bossAttack9);
    Tween bossAtk10 = new Tween(bossAttack10);
    Tween bossAtk11 = new Tween(bossAttack11);
    Tween bossAtk12 = new Tween(bossAttack12);
    */


    // boss timers
    GameClock bossTimer = new GameClock();
    GameClock bossMoveTimer = new GameClock();
    int bossFrameCounter;

    // boss booleans
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

    public Conquest() {
        super("Conquest", 1280, 720);

        this.addChild(boss);
        this.addChild(boi);
        this.addChild(platform1);
        // boi
        boi.setMaxHP(200);
        boi.setHealth(200);
        boi.setMaxMP(200);
        boi.setMana(200);

        boi.setGravity(2);
        boi.setPivotPoint(new Point(boi.getUnscaledWidth() / 2, boi.getUnscaledHeight() / 2));
        boi.setHitBox(0, 0, boi.getUnscaledWidth(), boi.getUnscaledHeight());
        boi.setPosition(1280 / 2, 1260 + boi.getUnscaledHeight() / 2 + 1);

        boi.addEventListener(this, "ATTACK_END" + boi.getId());
        boi.addEventListener(this, "GOT_HIT");

        // boss
        boss.setPivotPoint(new Point(boss.getUnscaledWidth() / 2, boss.getUnscaledHeight() / 2));
        boss.setHitBox(0, 0, boss.getUnscaledWidth(), boss.getUnscaledHeight());
        boss.setPosition(100, 1260);
        boss.addImage("stinger", "bossPlaceholder2.png", 1, 1);
        boss.addImage("slash", "bossPlaceholder3.png", 1, 1);
        boss.addImage("fireball", "bossPlaceHolder4.png", 1, 1);



        /*
        bossAttack1.setPivotPoint(bossAttack1.getUnscaledWidth() / 2, bossAttack1.getUnscaledHeight() / 2);
        bossAttack2.setPivotPoint(bossAttack1.getUnscaledWidth() / 2, bossAttack1.getUnscaledHeight() / 2);
        bossAttack3.setPivotPoint(bossAttack1.getUnscaledWidth() / 2, bossAttack1.getUnscaledHeight() / 2);
        bossAttack4.setPivotPoint(bossAttack1.getUnscaledWidth() / 2, bossAttack1.getUnscaledHeight() / 2);
        bossAttack5.setPivotPoint(bossAttack1.getUnscaledWidth() / 2, bossAttack1.getUnscaledHeight() / 2);
        bossAttack6.setPivotPoint(bossAttack1.getUnscaledWidth() / 2, bossAttack1.getUnscaledHeight() / 2);
        bossAttack7.setPivotPoint(bossAttack1.getUnscaledWidth() / 2, bossAttack1.getUnscaledHeight() / 2);
        bossAttack8.setPivotPoint(bossAttack1.getUnscaledWidth() / 2, bossAttack1.getUnscaledHeight() / 2);
        bossAttack9.setPivotPoint(bossAttack1.getUnscaledWidth() / 2, bossAttack1.getUnscaledHeight() / 2);
        bossAttack10.setPivotPoint(bossAttack1.getUnscaledWidth() / 2, bossAttack1.getUnscaledHeight() / 2);
        bossAttack11.setPivotPoint(bossAttack1.getUnscaledWidth() / 2, bossAttack1.getUnscaledHeight() / 2);
        bossAttack12.setPivotPoint(bossAttack1.getUnscaledWidth() / 2, bossAttack1.getUnscaledHeight() / 2);
        */

        boss.addChild(bossAttack1);
        boss.addChild(bossAttack2);
        boss.addChild(bossAttack3);
        boss.addChild(bossAttack4);
        boss.addChild(bossAttack5);
        boss.addChild(bossAttack6);
        boss.addChild(bossAttack7);
        boss.addChild(bossAttack8);
        boss.addChild(bossAttack9);
        boss.addChild(bossAttack10);
        boss.addChild(bossAttack11);
        boss.addChild(bossAttack12);



        // should just use normal for loop but whatever
        int counter = 0;
        for (DisplayObject child : boss.getChildren()) {
            child.setPivotPoint(bossAttack1.getUnscaledWidth() / 2, bossAttack1.getUnscaledHeight() / 2);
            child.setRotation(90 - counter * 10);
            // shouldn't be here actually
            child.setCollidable(true);
            child.setVisible(true);
            counter++;
        }
        boss.addChild(bossStraightArrow);
        bossStraightArrow.setPivotPoint(bossStraightArrow.getUnscaledWidth() / 2, bossStraightArrow.getUnscaledHeight() / 2);
        bossStraightArrow.setCollidable(true);
        bossStraightArrow.setVisible(true);


        // camera initialization shit
        WORLDSIZE_X = 1280;
        WORLDSIZE_Y = 720 * 2;
        SCREENSIZE_X = 1280;
        SCREENSIZE_Y = 720;
        offsetMaxX = WORLDSIZE_X - SCREENSIZE_X;
        offsetMaxY = WORLDSIZE_Y - SCREENSIZE_Y;
        offsetMinX = 0;
        offsetMinY = 0;

        // platforms
        platform1.setPosition(640, 1260 - 300);
    }

    @Override
    public void update(ArrayList<Integer> pressedKeys) {
        super.update(pressedKeys);
        if (juggler != null) juggler.nextFrame();
        if (boi != null) {
            if (boi.getVelocityX() == 0 && !boi.isJumping() && !boi.isFalling() && !boi.isAttacking()) {
                boi.animate("standing");
                boi.start();
            }
            if (boi.getPosition().getY() > 1260 + boi.getUnscaledHeight() / 2) {
                // set landing sets jumping false, falling false, velocityY 0, hasDJ true
                boi.setLanding();
                boi.setPosition(boi.getPosition().x, 1260 + boi.getUnscaledHeight() / 2);
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

        if (boss != null && bossFrameCounter < 48) {
            // boss.getChildren().atIndex(frameCounter / 2 or whatever)
            if (bossFrameCounter % 4 == 0) {
                DisplayObject arrow = boss.getChildren().get(bossFrameCounter / 4);
                Tween arrowTween = new Tween(arrow);
                arrowTween.animate(TweenableParams.X, arrow.getPosition().x, arrow.getPosition().x + 1800 * Math.cos(Math.toRadians(arrow.getRotation())), 1000 / 60 * 300);
                arrowTween.animate(TweenableParams.Y, arrow.getPosition().y, arrow.getPosition().y + 1800 * Math.sin(Math.toRadians(arrow.getRotation())), 1000 / 60 * 300);
                juggler.add(arrowTween);
            }
            bossFrameCounter++;
            /*
            for (DisplayObject arrow : boss.getChildren()) {
                Tween arrowTween = new Tween(arrow);
                arrowTween.animate(TweenableParams.X, arrow.getPosition().x, arrow.getPosition().x + 1800 * Math.cos(Math.toRadians(arrow.getRotation())), 1000 / 60 * 300);
                arrowTween.animate(TweenableParams.Y, arrow.getPosition().y, arrow.getPosition().y + 1800 * Math.sin(Math.toRadians(arrow.getRotation())), 1000 / 60 * 300);
                juggler.add(arrowTween);
            }
            bossMovingAction = true;
            */
        }

        if (bossTimer != null && bossTimer.getElapsedTime() > 3000) {
            double diffx = boi.getPosition().x - boss.getPosition().x;
            double diffy = boi.getPosition().y - boss.getPosition().y;
            // radians
            double rotate = (Math.atan(diffy/diffx));
            bossStraightArrow.setRotation((int)Math.toDegrees(rotate));
            Tween straightTween = new Tween(bossStraightArrow);
            straightTween.animate(TweenableParams.X, 0, Math.cos(rotate) * 1800, 1000 / 60 * 120);
            straightTween.animate(TweenableParams.Y, 0, Math.sin(rotate) * 1800, 1000 / 60 * 120);
            TweenJuggler.add(straightTween);
            bossTimer.resetGameClock();
        }

        if (boi != null) {
            for (DisplayObject attack : boss.getChildren()) {
                if (attack.collidesWith(boi)) {
                    System.out.println("wow");
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

//        if (boi != null) {
//            if (boi.getHealth() <= 0) {
//                dispatchEvent(new Event("gameOver", this));
//                super.exitGame();
//            }
//        }
//        if (bossHealth <= 0) {
//            dispatchEvent(new Event("victory", this));
//        }
    }

//    /**
//     * This is needed because you need to switch to the JavaFX thread before you can change data in the
//     * JavaFX application.
//     * @param event
//     */
//    @Override
//    public void dispatchEvent(Event event) {
//        if (event.getEventType().equals("gameOver") || event.getEventType().equals("victory")) {
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    Conquest.super.dispatchEvent(event);
//                }
//            });
//        } else {
//            super.dispatchEvent(event);
//        }
//    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        // camera translation
        g.translate((int)-camX, (int)-camY);
        // draw everything but GUI
        super.draw(g);

        if (boi != null) {
            g2d.setColor(Color.red);
            g2d.draw(boi.getHitBox());
            /*
            for(DisplayObject child : boi.getChildren()) {
                g2d.setColor(Color.red);
                g2d.draw(child.getHitBox());
            }
            */
        }

        if (boss != null) {
            g2d.setColor(Color.red);
            g2d.draw(boss.getHitBox());
            for(DisplayObject child : boss.getChildren()) {
                g2d.setColor(Color.red);
                g2d.draw(child.getHitBox());
            }
        }

        // change back
        g.translate((int)camX, (int)camY);



    }

    @Override
    public void handleEvent(Event event) {

    }

    public static void main(String[] args) {
        Conquest game = new Conquest();
        game.start();
    }
}
