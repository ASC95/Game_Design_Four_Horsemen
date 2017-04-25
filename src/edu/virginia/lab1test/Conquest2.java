package edu.virginia.lab1test;

import edu.virginia.engine.display.*;
import edu.virginia.engine.events.IEventListener;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.util.GameClock;
import edu.virginia.engine.util.SoundManager;
import javafx.application.Platform;

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

    private SoundManager soundManager = new SoundManager();


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
    int bulletHell1Rate = 2;
    int bulletHell1Angle = 15;
    int bossHealth = 1000;
    int maxBossHealth = 1000;

    // boss booleans
    boolean bossWasHit;
    boolean bossMovingAction;
    Point bossPosBL;
    Point bossPosBR;
    Point bossPosLL;
    Point bossPosLR;
    Point bossPosUL;
    Point bossPosUR;

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
        boi.addEventListener(soundManager, "BOI_INJURED_0");//boi listens for when he is injured
        boi.addEventListener(soundManager, "BOI_HEALED");//soundManager listens for when boi heals
        boi.addEventListener(soundManager, "BOI_DASH");

        // boss
        boss.setPivotPoint(new Point(boss.getUnscaledWidth() / 2, boss.getUnscaledHeight() / 2));
        boss.setHitBox(0, 0, boss.getUnscaledWidth(), boss.getUnscaledHeight());
        boss.setPosition(100, 1260 + boi.getUnscaledHeight() - boss.getUnscaledHeight() / 2);
        boss.addImage("bullethell1", "bossPlaceholder2.png", 1, 1);
        boss.addImage("straight", "bossPlaceholder3.png", 1, 1);
        boss.addImage("fireball", "bossPlaceHolder4.png", 1, 1);

        boss.addAttack("bullethell1", new Action(120));
        boss.addAttack("straight", new Action(120));
        boss.addEventListener(this, "ATTACK_END" + boss.getId());
        boss.addEventListener(this, "BOSS_HIT");
        boss.addEventListener(soundManager, "BOSS_HIT");//soundManager listens for when boss gets hit
        boss.addEventListener(soundManager, "BOSS_DASH");//soundManager listens for when boss dashes
        boss.addEventListener(soundManager, "BOSS_FIREBALL");//soundManager listens for fireball
        boss.addEventListener(soundManager, "BOSS_SLASH");//soundManager listens for boss's slash

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

        // boss positions
        bossPosBL = new Point(boss.getUnscaledWidth() / 2, 1260);
        bossPosBR = new Point(1280 - boss.getUnscaledWidth() / 2, 1260);
        bossPosLR = new Point(1280 - 150 - platform1.getUnscaledWidth() / 2, 1260 - 180 - boss.getUnscaledHeight() / 2);
        bossPosLL = new Point(150 + platform1.getUnscaledWidth() / 2, 1260 - 180 - boss.getUnscaledHeight() / 2);
        bossPosUR = new Point(1280 - 150 - platform1.getUnscaledWidth() / 2 , 1260 - 834 - boss.getUnscaledHeight() / 2);
        bossPosUL = new Point(150 + platform1.getUnscaledWidth() / 2, 1260 - 834 - boss.getUnscaledHeight() / 2);
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
        // boss action decision
        if (boss != null && !boss.isAttacking()) {
            // store this - multiple calls will have different values!!! duh
            double time = bossMoveTimer.getElapsedTime();
            if (time > 3000) {
                // System.out.println("timer");
                // choose spot in level and move/teleport
                // note: if player is too close, should affect logic!
                // or just don't warp to same spot
                if (((int)(time * 100000)) % 6 == 0) {
                    // replace with actual movement call/tween
                    // bottom left
                    if (boss.getPosition().equals(bossPosBL)) {
                        // warp to different spot
                        // System.out.println("warp1");
                        boss.setPosition(bossPosUL);
                    } else {
                        // System.out.println("arp1");
                        boss.setPosition(bossPosBL);
                    }

                } else if ((int)(time * 100000) % 6 == 1) {
                    // bottom right
                    if (boss.getPosition().equals(bossPosBR)) {
                        // warp to different spot
                        // System.out.println("warp2");
                        boss.setPosition(bossPosLL);
                    } else {
                        // System.out.println("arp2");
                        boss.setPosition(bossPosBR);
                    }


                } else if ((int)(time * 100000) % 6 == 2) {
                    // lower right
                    if (boss.getPosition().equals(bossPosLR)) {
                        // warp to different spot
                        // System.out.println("warp3");
                        boss.setPosition(bossPosUR);
                    } else {
                        // System.out.println("arp3");
                        boss.setPosition(bossPosLR);
                    }

                } else if ((int)(time * 100000) % 6 == 3) {
                    // lower left
                    if (boss.getPosition().equals(bossPosLL)) {
                        // warp to different spot
                        // System.out.println("warp4");
                        boss.setPosition(bossPosLR);
                    } else {
                        // System.out.println("arp4");
                        boss.setPosition(bossPosLL);
                    }

                } else if ((int)(time * 100000) % 6 == 4) {
                    // upper right
                    if (boss.getPosition().equals(bossPosUR)) {
                        // warp to different spot
                        // System.out.println("warp5");
                        boss.setPosition(bossPosBL);
                    } else {
                        // System.out.println("arp5");
                        boss.setPosition(bossPosUR);
                    }

                } else if ((int)(time * 100000) % 6 == 5) {
                    // upper left
                    if (boss.getPosition().equals(bossPosUL)) {
                        // warp to different spot
                         // System.out.println("warp6");
                         boss.setPosition(bossPosBR);
                    } else {
                         // System.out.println("arp6");
                         boss.setPosition(bossPosUL);
                    }

                } else {
                    System.out.println("disaster");
                    System.out.println(time * 100000 % 6);
                }

                // choose attack and execute - should be independent from movement
                // different modulos should fix this?
                bossFrameCounter = 0;
                // System.out.println((int)(time * 100000) % 3);
                if ((int)(time * 100000) % 3 == 0) {
                    boss.setAttack("bullethell1");
                    boss.animate("bullethell1");
                    boss.start();
                    boss.startAttack();

                } else if ((int)(time * 100000) % 3 == 1) {
                    boss.setAttack("straight");
                    boss.animate("straight");
                    boss.start();
                    boss.startAttack();
                    bossTimer.resetGameClock();

                } else if ((int)(time * 100000) % 3 == 2) {
                    boss.setAttack("bullethell1");
                    boss.animate("bullethell1");
                    boss.start();
                    boss.startAttack();

                } else {
                    System.out.println("modulo didn't work! check attack choosing");
                }
            } else {
                boss.animate("standing");
                boss.start();
            }

        }

        // bullet hell
        if (boss != null && boss.isAttacking()) {
            if (boss.getCurrentAction().equals("bullethell1")) {
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

            } else if (boss.getCurrentAction().equals("straight")) {
                if (bossTimer != null && bossTimer.getElapsedTime() > 400) {
                    boss.dispatchEvent(new Event("BOSS_FIREBALL", boss));
                    double diffx = boi.getPosition().x - boss.getPosition().x;
                    double diffy = boi.getPosition().y - boss.getPosition().y;
                    int signx = 1;
                    if (diffx < 0) signx = -1;
            /*
            int signy = 1;
            if (diffy < 0) signy = -1;
            */
                    // radians
                    double rotate = (Math.atan(diffy / diffx));
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
            }
        }

        if (boi != null && !boi.isInvincible()) {
            for (DisplayObject attack : boss.getChildren()) {
                if (attack.collidesWith(boi)) {
                    boi.dispatchEvent(new Event("GOT_HIT", attack));
                }
            }

        }

        if (boss != null && !bossWasHit) {
            for (DisplayObject hitbox : boi.getChildren()) {
                if (hitbox.collidesWith(boss)) {
                    boss.dispatchEvent(new Event("BOSS_HIT", hitbox));
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
        if (bossHealth <= 0) {
            dispatchEvent(new Event("victory", this));
            super.exitGame();
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        // camera translation
        g.translate((int)-camX, (int)-camY);
        // draw everything but GUI
        super.draw(g);

//        if (boi != null) {
//            g2d.setColor(Color.red);
//            g2d.draw(boi.getHitBox());
//            /*
//            for(DisplayObject child : boi.getChildren()) {
//                g2d.setColor(Color.red);
//                g2d.draw(child.getHitBox());
//            }
//            */
//        }

//        if (boss != null) {
//            g2d.setColor(Color.red);
//            g2d.draw(boss.getHitBox());
//            for(DisplayObject child : boss.getChildren()) {
//                if (child.isCollidable()) {
//                    g2d.setColor(Color.red);
//                    g2d.draw(child.getHitBox());
//                }
//            }
//        }
        // change back
        g.translate((int)camX, (int)camY);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        if (boi != null) g2d.drawString("Player HP: " + boi.getHealth(), 100, 100);
        if (boi != null) g2d.drawString("Player MP: " + (int)boi.getMana(), 100, 200);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Boss HP: " + bossHealth, 1100, 100);
        if (boi != null) {
            drawHealthBars(g2d);
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
                    Conquest2.super.dispatchEvent(event);
                }
            });
        } else {
            super.dispatchEvent(event);
        }
    }

    public static void main(String[] args) {
        Conquest2 game = new Conquest2();
        game.start();
    }
}
