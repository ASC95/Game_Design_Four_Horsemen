package edu.virginia.lab1test;

import edu.virginia.engine.display.*;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;
import edu.virginia.engine.util.GameClock;
import edu.virginia.engine.util.SoundManager;
import javafx.application.Platform;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Resquall on 4/9/2017.
 */
public class Tutorial extends Game implements IEventListener {

    Player boi = new Player("boi", "standing", "standing mc.png");

    int bossHealth = 1000;
    int maxBossHealth = 600;
    int loadingFrames = 0;

    Sprite background = new Sprite("background", "warBackground.png");
//    PhysicsSprite enemy = new PhysicsSprite("enemy", "standing", "stand.png");
//    PhysicsSprite enemy2 = new PhysicsSprite("enemy2", "standing", "stand.png");
//    PhysicsSprite platform1 = new PhysicsSprite("platform1", "standing", "platform.png");
//    PhysicsSprite platform2 = new PhysicsSprite("platform2", "standing", "platform.png");

    ActionSprite boss = new ActionSprite("boss", "standing", "dummy.png");

    AttackHitbox bossAttack1 = new AttackHitbox("bossAttack1", "bossAttack1.png", 5, 0, 0, 0);
    AttackHitbox bossAttack2 = new AttackHitbox("bossAttack1", "bossAttack1.png", 40, 0, 0, 0);
    AttackHitbox bossAttack3 = new AttackHitbox("bossAttack1", "bossAttack1.png", 40, 0, 0, 0);

    AttackHitbox fireball1 = new AttackHitbox("bossAttack1", "bossAttack1.png", 30, 0, 0, 0);

    TweenJuggler juggler = new TweenJuggler();

    GameClock bossTimer = new GameClock();
    GameClock bossMoveTimer = new GameClock();

    private SoundManager soundManager = new SoundManager();

    Sprite platform1 = new Sprite("plat1", "platform2.png");
    ArrayList<Sprite> platforms = new ArrayList<>();


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

    private HashMap<String, String> instructions = new HashMap<>();
    private String inst1a = "Press the \"a\" key to attack the training dummy.";
    private String inst1b = "Press \"a\" three times in a row to do a combo.";
    private String inst2a = "Press the \"up\" arrow key once to jump, twice to double jump.";
    private String inst3a = "You can jump through platforms to land on them.";
    private String inst3b = "You can fall through platforms by pressing the \"down\" arrow key.";
    private String inst4a = "Press the \"shift\" key to perform a dash";
    private String inst4b = "You are invincible while dashing, so use this to your advantage to dodge attacks.";
    private String inst5a = "For example, if you walk through the dummy you will take damage.";
    private String inst5b = "But if you dash through the dummy, you don't take any damage! Remember this technique.";
    private String inst6a = "Your health is displayed in the top left corner.";
    private String inst6b = "The boss's health is displayed in the top right corner.";
    private String inst7a = "You win if you reduce the boss's health to 0.";
    private String inst7b = "You lose if your health goes to 0.";
    private String inst8a = "You can heal yourself using mana by pressing the \"e\" key.";
    private String inst8b = "But be careful, mana takes time to recharge and every heal costs 80 mana.";
    private String inst9a = "Now you are ready to fight. Defeat the practice dummy!";
    private int instructionCounter = 1;
    private int strikeCount = 0;
    private double runningTime = 0;


    public Tutorial() {
        super("Movement", 1280, 720);
        // 1920 - 1280 = 640
        // 1080 - 720 = 360
        instructions.put("1a", inst1a);
        instructions.put("1b", inst1b);
        instructions.put("2a", inst2a);
        instructions.put("3a", inst3a);
        instructions.put("3b", inst3b);
        instructions.put("4a", inst4a);
        instructions.put("4b", inst4b);
        instructions.put("5a", inst5a);
        instructions.put("5b", inst5b);
        instructions.put("6a", inst6a);
        instructions.put("6b", inst6b);
        instructions.put("7a", inst7a);
        instructions.put("7b", inst7b);
        instructions.put("8a", inst8a);
        instructions.put("8b", inst8b);
        instructions.put("9a", inst9a);

        this.addChild(background);
        this.addChild(boss);
        this.addChild(boi);
//        this.addChild(platform1);

        platforms.add(platform1);
        platform1.setPosition(640, 200);

        boi.setMaxHP(200);
        boi.setHealth(200);
        boi.setMaxMP(200);
        boi.setMana(200);

        boi.setGravity(2);
//        boi.setPivotPoint(new Point(boi.getUnscaledWidth() / 2, boi.getUnscaledHeight() / 2));
//        boi.setHitBox(0, 0, boi.getUnscaledWidth(), boi.getUnscaledHeight());
//        boi.setHitBox(90, 0, boi.getUnscaledWidth() - 180, boi.getUnscaledHeight());
        boi.setPosition(400, 520 + boi.getUnscaledHeight() / 2 + 1);

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
        boss.getPosition().setLocation(700, 600);

        // how to set things based on bottom?
        // i want everything on the floor...

//        enemy.setPosition(640, 540);
        /*
        enemy2.setPosition(1820, 900);
        */

        boss.addImage("stinger", "bossPlaceholder2.png", 1, 1);
//        boss.addImage("slash", "bossPlaceholder3.png", 1, 1);
//        boss.addImage("fireball", "bossPlaceHolder4.png", 1, 1);

//        boss.addChild(bossAttack1);
//        bossAttack1.setPosition(boss.getUnscaledWidth() / 2, 0);
//        boss.addChild(bossAttack2);
//        bossAttack2.setPosition(boss.getUnscaledWidth() / 2, -boss.getUnscaledHeight() / 2);
//        boss.addChild(bossAttack3);
//        bossAttack3.setPosition(boss.getUnscaledWidth(), -boss.getUnscaledHeight() / 4);
//        boss.addChild(fireball1);
//        fireball1.setPosition(boss.getUnscaledWidth(), -boss.getUnscaledHeight() / 4);

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

//        boss.addAttack("stinger", bossStinger);
//        boss.addAttack("slash", bossSlash1);
//        boss.addAttack("fireball", bossFireball1);

        boss.addEventListener(this, "ATTACK_END" + boss.getId());
        boss.addEventListener(this, "BOSS_HIT");
        boss.addEventListener(soundManager, "BOSS_HIT");//soundManager listens for when boss gets hit
        boss.addEventListener(soundManager, "BOSS_DASH");//soundManager listens for when boss dashes
//        boss.addEventListener(soundManager, "BOSS_FIREBALL");//soundManager listens for fireball
//        boss.addEventListener(soundManager, "BOSS_SLASH");//soundManager listens for boss's slash

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
        if (pressedKeys.contains(KeyEvent.VK_UP) && instructionCounter == 2) {
            instructionCounter += 2;
        }
        if (pressedKeys.contains(KeyEvent.VK_SHIFT)) {
            if (instructionCounter == 4) {
                instructionCounter++;
            }
        }
        if (bossTimer != null) {
            if (instructionCounter > 4) runningTime += 1;
            if (runningTime >= 400 && instructionCounter >= 5) {
                instructionCounter++;
                runningTime = 0;
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
//            boi.setFalling(true);
            if (!boi.isJumping() && !boi.isAttacking() && !pressedKeys.contains(KeyEvent.VK_LEFT) && !pressedKeys.contains(KeyEvent.VK_RIGHT)) {
                boi.animate("standing");
                boi.start();
            }
            if (boi.isJumping()) {
                boi.animate("jumping");
            }
            if (boi.getPosition().getY() > 520 + boi.getUnscaledHeight() / 2) {
//                 set landing sets jumping false, falling false, velocityY 0, hasDJ true
                boi.setLanding();
                boi.setPosition(boi.getPosition().x, 520 + boi.getUnscaledHeight() / 2);
            }
//            if (!boi.canDropDown()) {
//                for (Sprite plat : platforms) {
//                    if (plat.getPosition().y >= boi.getLastFramePosition().y + boi.getUnscaledHeight() / 2) {
//                        if (plat.collidesWith(boi)) {
//                            boi.setLanding();
//                            boi.setPosition(boi.getPosition().x, plat.getPosition().y - boi.getUnscaledHeight() / 2);
//                        }
//                    }
//                }
//            }
        }
//        if (boss != null) {
//            if (bossTimer.getElapsedTime() > 3000) {
//
//            }
//        }

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
                } else if (boss.collidesWith(boi)) {
                    boi.dispatchEvent(new Event("GOT_HIT", bossAttack1));
                    boi.dispatchEvent(new Event("BOI_INJURED_0", bossAttack1));
                }
            }
        }

        if (boss != null && !bossWasHit) {
            for (DisplayObject hitbox : boi.getChildren()) {
                if (hitbox.collidesWith(boss)) {
                    strikeCount++;
                    if (strikeCount == 4) {
                        instructionCounter++;
                    }
                    boss.dispatchEvent(new Event("BOSS_HIT", hitbox));
                    Tween shakeTween = new Tween(boss);
                    if (boi.getPosition().getX() < boss.getPosition().getX()) {
                        shakeTween.animate(TweenableParams.X, boss.getPosition().x, boss.getPosition().x - 10, 50);
                        shakeTween.animate(TweenableParams.X, boss.getPosition().x, boss.getPosition().x + 10, 50);
                    } else {
                        shakeTween.animate(TweenableParams.X, boss.getPosition().x, boss.getPosition().x + 10, 50);
                        shakeTween.animate(TweenableParams.X, boss.getPosition().x, boss.getPosition().x - 10, 50);
                    }
                    juggler.add(shakeTween);
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
        if (boss != null) {
            if (boss.getPosition().x < 100) {
                boss.getPosition().x = 100;
            }
            if (boss.getPosition().x > 1180) {
                boss.getPosition().x = 1180;
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
                    Tutorial.super.dispatchEvent(event);
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
        g.translate((int) -camX, (int) -camY);
        // draw everything but GUI
        super.draw(g);
        // change back
        g.translate((int) camX, (int) camY);
        if (boi != null) {
            drawHealthBars(g2d);
        }
        g2d.setColor(Color.blue);
        g2d.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        if (boi != null) g2d.drawString("Player HP: " + boi.getHealth(), 100, 100);
        if (boi != null) g2d.drawString("Player MP: " + (int) boi.getMana(), 100, 200);
        g2d.setColor(Color.red);
        g2d.drawString("Boss HP: " + bossHealth, 1100, 100);

        g2d.setColor(Color.black);
        if (instructions.get(instructionCounter + "a") != null) {
            drawInstruction(g2d, instructions.get(instructionCounter + "a"), 350, 30);
        }
        if (instructions.get(instructionCounter + "b") != null) {
            drawInstruction(g2d, instructions.get(instructionCounter + "b"), 350, 60);
        }
//        if (boi != null) {
//            g2d.setColor(Color.red);
//            g2d.draw(boi.getHitBox());
//        }
    }

    private void drawInstruction(Graphics2D g2d, String inst, int x, int y) {
        g2d.drawString(inst, x, y);
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
        Tutorial game = new Tutorial();
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
