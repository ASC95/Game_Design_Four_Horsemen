package edu.virginia.lab1test;

import edu.virginia.engine.display.*;
import edu.virginia.engine.events.IEventListener;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.util.GameClock;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by Resquall on 4/21/2017.
 */
public class Conquest2 extends Game implements IEventListener {
    Player boi = new Player("boi", "standing", "standing.png");
    ActionSprite boss = new ActionSprite("boss", "standing", "bossPlaceholder1.png");
    Sprite platform1 = new Sprite("plat1", "platform2.png");
    Sprite platform2 = new Sprite("plat2", "platform2.png");
    Sprite platform3 = new Sprite("plat3", "platform2.png");
    Sprite platform4 = new Sprite("plat4", "platform2.png");
    Sprite platform5 = new Sprite("plat5", "platform2.png");
    Sprite background = new Sprite("background", "conquestBackground.png");
    ArrayList<Sprite> platforms = new ArrayList<>();

    // attack hitboxes for boss go here
    ArrayList<AttackHitbox> bulletHell1 = new ArrayList<>();
    ArrayList<AttackHitbox> straightAttack = new ArrayList<>();

    TweenJuggler juggler = new TweenJuggler();
    // tweens

    // boss timers
    GameClock bossTimer = new GameClock();
    GameClock bossMoveTimer = new GameClock();
    int bossFrameCounter;
    int arrowCounter;
    int bulletHell1Rate = 3;
    int bulletHell1Angle = 15;
    int bossHealth = 1000;

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

    public Conquest2() {
        super("Conquest2", 1280, 720);

        this.addChild(background);
        this.addChild(boss);
        this.addChild(boi);
        this.addChild(platform1);
        this.addChild(platform2);
        this.addChild(platform3);
        this.addChild(platform4);
        this.addChild(platform5);
        // boi
        boi.setMaxHP(200);
        boi.setHealth(200);
        boi.setMaxMP(200);
        boi.setMana(200);

        // call
        /*
        boi.setPlayerControlled(true);
        boss.setPlayerControlled(true);
        */

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
        platform1.setPosition(1280 - 150 - platform1.getUnscaledWidth(), 1260 - 180);
        platform2.setPosition(150, 1260 - 180);
        platform3.setPosition(640 - platform1.getUnscaledWidth() / 2, 1260 - 507);
        platform4.setPosition(1280 - 150 - platform1.getUnscaledWidth(), 1260 - 834);
        platform5.setPosition(150, 1260 - 834);
        platforms.add(platform1);
        platforms.add(platform2);
        platforms.add(platform3);
        platforms.add(platform4);
        platforms.add(platform5);
    }

    @Override
    public void update(ArrayList<Integer> pressedKeys) {
        super.update(pressedKeys);
        if (juggler != null) juggler.nextFrame();
        if (boi != null) {
            // TODO: falling flag lol
            boi.setFalling(true);

            if (boi.getPosition().getY() >= 1260 + boi.getUnscaledHeight() / 2) {
                // set landing sets jumping false, falling false, velocityY 0, hasDJ true
                boi.setLanding();
                boi.setPosition(boi.getPosition().x, 1260 + boi.getUnscaledHeight() / 2);
            }
            if (!boi.canDropDown()) {
                for (Sprite plat : platforms) {
                    if (plat.getPosition().y >= boi.getLastFramePosition().y + boi.getUnscaledHeight() / 2) {
                        if (plat.collidesWith(boi)) {
                            boi.setLanding();
                            boi.setPosition(boi.getPosition().x, plat.getPosition().y - boi.getUnscaledHeight() / 2);
                        }
                    }
                }
            }
            if (boi.getVelocityX() == 0 && !boi.isJumping() && !boi.isFalling() && !boi.isAttacking()) {
                boi.animate("standing");
                boi.start();
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

        // bullet hell
        if (boss != null) {
            if (bossFrameCounter % bulletHell1Rate == 0) {
//                AttackHitbox arrow = bulletHell1.get(bossFrameCounter / bulletHell1Rate);
                AttackHitbox arrow = new AttackHitbox("bullethell1", "arrow.png", 30, 0, 0, 0);
                boss.addChild(arrow);
                arrow.setPivotPoint(arrow.getUnscaledWidth() / 2, arrow.getUnscaledHeight() / 2);
                arrow.setRotation(90 - bossFrameCounter / bulletHell1Rate * bulletHell1Angle);
                TweenAttack arrowTween = new TweenAttack(arrow);
                arrowTween.animate(TweenableParams.X, 0, 1800 * Math.cos(Math.toRadians(arrow.getRotation())), 1000 / 60 * 270);
                arrowTween.animate(TweenableParams.Y, 0, 1800 * Math.sin(Math.toRadians(arrow.getRotation())), 1000 / 60 * 270);
                TweenJuggler.add(arrowTween);
            }
            bossFrameCounter++;
//            if (bossFrameCounter > 4 * bulletHell1.size() - 1) bossFrameCounter = 0;
            if (bossFrameCounter > bulletHell1Rate * 360 / bulletHell1Angle - 1) bossFrameCounter = 0;

        }

        if (bossTimer != null && bossTimer.getElapsedTime() > 400) {
            double diffx = boi.getPosition().x - boss.getPosition().x;
            double diffy = boi.getPosition().y - boss.getPosition().y;
            int signx = 1;
            if (diffx < 0) signx = -1;
            /*
            int signy = 1;
            if (diffy < 0) signy = -1;
            */
            // radians
            double rotate = (Math.atan(diffy/diffx));
            // straightAttack.get(arrowCounter).setRotation((int)Math.toDegrees(rotate));
            // TweenAttack straightTween = new TweenAttack(straightAttack.get(arrowCounter));
            AttackHitbox straight = new AttackHitbox("straight", "arrow.png", 30, 0, 0, 0);
            straight.setPivotPoint(straight.getUnscaledWidth() / 2, straight.getUnscaledHeight() / 2);
            boss.addChild(straight);
            if (signx > 0) {
                straight.setRotation((int) Math.toDegrees(rotate));
            } else {
                straight.setRotation(180 + (int) Math.toDegrees(rotate));
            }
            TweenAttack straightTween = new TweenAttack(straight);
            straightTween.animate(TweenableParams.X, 0, signx * Math.cos(rotate) * 1800, 1000 / 60 * 120);
            straightTween.animate(TweenableParams.Y, 0, signx * Math.sin(rotate) * 1800, 1000 / 60 * 120);
            TweenJuggler.add(straightTween);
            bossTimer.resetGameClock();
            /*
            arrowCounter++;
            if (arrowCounter == straightAttack.size()) arrowCounter = 0;
            */
        }

        if (boi != null) {
            for (DisplayObject attack : boss.getChildren()) {
                if (attack.collidesWith(boi)) {
                    //TODO: the thing
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
    }

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
                if (child.isCollidable()) {
                    g2d.setColor(Color.red);
                    g2d.draw(child.getHitBox());
                }
            }
        }

        // change back
        g.translate((int)camX, (int)camY);



    }

    @Override
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

    public static void main(String[] args) {
        Conquest2 game = new Conquest2();
        game.start();
    }
}
