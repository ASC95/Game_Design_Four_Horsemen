package edu.virginia.lab1test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import edu.virginia.engine.display.AnimatedSprite;
import edu.virginia.engine.display.DisplayObject;
import edu.virginia.engine.display.Game;
import edu.virginia.engine.display.PhysicsSprite;
import edu.virginia.engine.display.PickedUpEvent;
import edu.virginia.engine.display.Sprite;
import edu.virginia.engine.display.Tween;
import edu.virginia.engine.display.TweenEvent;
import edu.virginia.engine.display.TweenJuggler;
import edu.virginia.engine.display.TweenableParams;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;
import edu.virginia.engine.events.QuestManager;
import edu.virginia.engine.util.SoundManager;

public class LabSixGame extends Game implements IEventListener {

	static int screenWidth = 600;
	static int screenHeight = 590;
	
	PhysicsSprite boi = new PhysicsSprite("boi", "standing", "standing.png");
	Sprite bg = new Sprite("bg", "bg0000.png");
	Sprite box = new Sprite("box", "box.png");
	Sprite plat1 = new Sprite("plat1", "platform.png");
	Sprite plat2 = new Sprite("plat2", "platform.png");
	Sprite plat3 = new Sprite("plat3", "platform.png");
	
	QuestManager questManager = new QuestManager();
	SoundManager soundManager = new SoundManager();
	
	TweenJuggler juggler = new TweenJuggler();
	Tween boiTween = new Tween(boi);
	Tween boxTween = new Tween(box, "easeInOut");
	Tween boxDisappearingTween = new Tween(box, "easeInOut");
	
	PickedUpEvent pickup = new PickedUpEvent(PickedUpEvent.BOX_PICKED_UP, box);
	TweenEvent boiEvent = new TweenEvent(TweenEvent.TWEEN_COMPLETE_EVENT, boiTween);
	TweenEvent moveEvent = new TweenEvent(TweenEvent.TWEEN_COMPLETE_EVENT, boxTween);
	TweenEvent fadeEvent = new TweenEvent(TweenEvent.TWEEN_COMPLETE_EVENT, boxDisappearingTween);
	String questComplete = "Quest completed!";
	
	Rectangle hitbox;
	
	Boolean canPlay = false;
	
	public LabSixGame() throws LineUnavailableException, UnsupportedAudioFileException {
		super("Lab Three Test Game", screenWidth, screenHeight);
		
		plat1.setPosition(100, 380);
		plat2.setPosition(270, 290);
		plat3.setPosition(450, 210);
		
		boi.setPosition(10, (int) (this.getScenePanel().getHeight() - boi.getUnscaledHeight()*boi.getScaleX() - 45));
		boi.addImage("walking", "walk1.png", 1, 2);
		boi.addImage("jumping", "jump.png", 1, 1);
		
		box.setPosition(450 + plat2.getUnscaledWidth()/2 - box.getUnscaledWidth()/2, 210 - box.getUnscaledHeight());
		box.addEventListener(questManager, PickedUpEvent.BOX_PICKED_UP);
		
		bg.setPosition(bg.getPosition().x, bg.getPosition().y - 20);
		
		soundManager.LoadMusic("bg", "Phantom_Brave_OST_-_My_Little_Garden.wav");
		soundManager.LoadSoundEffect("item GET", "GET_AN_ITEM_Wind_Waker_Sound_effect.wav");
		soundManager.PlayMusic("bg");
		
		boiTween.animate(TweenableParams.SCALE_X, 0, 1, 2);
		boiTween.animate(TweenableParams.ALPHA, 0, 1, 2);
		boiTween.addEventListener(this, TweenEvent.TWEEN_COMPLETE_EVENT);
		
		juggler.add(boiTween);
	}
	
	@Override
	public void update(ArrayList<String> pressedKeys){
		super.update(pressedKeys);
		
		if(box != null) box.update(pressedKeys);
		if(plat1 != null) plat1.update(pressedKeys);
		if(plat2 != null) plat2.update(pressedKeys);
		if(plat3 != null) plat3.update(pressedKeys);
		if(juggler != null) juggler.nextFrame();

		if(boi != null)  {
			boi.update(pressedKeys);
			
			if(boiTween.isComplete()) {
				boiTween.dispatchEvent(boiEvent);
			}
			
			if(boxTween.isComplete()) {
				boxTween.dispatchEvent(moveEvent);
			}
			
			if(boxDisappearingTween.isComplete()) {
				boxDisappearingTween.dispatchEvent(fadeEvent);
			}
			
			if(canPlay) {
				if(pressedKeys.contains("Left") || pressedKeys.contains("Right") || pressedKeys.contains("Up")) {
					
					if(pressedKeys.contains("Left")) {
						boi.setPosition(boi.getPosition().x += -5, boi.getPosition().y);
						if(pressedKeys.contains("Right") && pressedKeys.indexOf("Right") > pressedKeys.indexOf("Left")) {
							boi.setPosition(boi.getPosition().x - 5, boi.getPosition().y); // so that sprite will still move 
						}
						if(boi.getScaleX() > 0) {
							boi.setScaleX(-1);
							boi.setPosition(boi.getPosition().x + boi.getUnscaledWidth(), boi.getPosition().y);
						}
						if((boi.getPosition().x < plat3.getPosition().x&& boi.getPosition().x > plat2.getPosition().x + plat2.getUnscaledWidth()) || (boi.getPosition().x < plat2.getPosition().x && boi.getPosition().x > plat1.getPosition().x + plat1.getUnscaledWidth()) || (boi.getPosition().x < plat1.getPosition().x)) {
							boi.setGravity(-4);
							boi.setFalling(true);
						}
					}
					if(pressedKeys.contains("Right")) {
						boi.setPosition(boi.getPosition().x += 5, boi.getPosition().y);
						if(pressedKeys.contains("Left") && pressedKeys.indexOf("Right") < pressedKeys.indexOf("Left")) {
							boi.setPosition(boi.getPosition().x + 5, boi.getPosition().y); // so that sprite will still move 
						}
						if(boi.getScaleX() < 0) {
							boi.setScaleX(1);
							boi.setPosition(boi.getPosition().x - boi.getUnscaledWidth(), boi.getPosition().y);
						}
						
						if((boi.getPosition().x > plat1.getPosition().x + plat1.getUnscaledWidth() && boi.getPosition().x < plat2.getPosition().x) || (boi.getPosition().x < plat3.getPosition().x && boi.getPosition().x > plat2.getPosition().x + plat1.getUnscaledWidth())) {
							boi.setGravity(-4);
							boi.setFalling(true);
						}
						
					}
					
					if(!boi.isPlaying()) {
						boi.setSpeed(7);
						boi.animate("walking");
					}
					
					if (pressedKeys.contains("Up")) {
						if(!boi.isJumping() && !boi.isFalling()) {
							try {
								soundManager.LoadSoundEffect("jump", "Mario_Jumping.wav");
							} catch (LineUnavailableException | UnsupportedAudioFileException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							soundManager.PlaySoundEffect("jump");
							boi.setVelocity(38);
							boi.setGravity(-5);
							boi.setJumping(true);
							boi.setFalling(false);
							boi.animate("jumping");
							boi.start();
						}	
	
					}
				} else {
					boi.animate("standing");
					boi.stop();
				}
	
				if(boi.getPosition().getY() > (this.getScenePanel().getHeight() - boi.getUnscaledHeight() - 45)) {
					boi.setJumping(false);
					boi.setFalling(false);
					boi.setPosition(boi.getPosition().x, this.getScenePanel().getHeight() - boi.getUnscaledHeight() - 45);
					if(boi.getAnimate().equals("jumping")) {
						boi.setSpeed(7);
						boi.animate("walking");
					}
				}
				if(boi.getScaleX() == -1) {
					hitbox = new Rectangle(boi.getUnscaledWidth(), 0, (int)((this.getScenePanel().getWidth())), (int)((this.getScenePanel().getHeight() - boi.getUnscaledHeight() - 45)));
				} else {
					hitbox = new Rectangle(0, 0, (int)((this.getScenePanel().getWidth() - boi.getUnscaledWidth())), (int)((this.getScenePanel().getHeight() - boi.getUnscaledHeight() - 45)));
				}
				if(!hitbox.contains(boi.getPosition())) {
					int x;  
					if(boi.getPosition().x > hitbox.width) {
						x = hitbox.width;
					} else if(boi.getPosition().x < hitbox.x) {
						x = hitbox.x;
					} else {
						x = boi.getPosition().x;
					}
					Point p = new Point(x, boi.getPosition().y);
					boi.setPosition(p);
				}
	
				if(box.collidesWith(boi) && box.hasEventListener(questManager, PickedUpEvent.BOX_PICKED_UP)) {
					soundManager.PlaySoundEffect("item GET");
					box.dispatchEvent(pickup);
					box.removeEventListener(questManager, PickedUpEvent.BOX_PICKED_UP);
					
					boxTween.animate(TweenableParams.SCALE_X, 1, 4, 3.5);
					boxTween.animate(TweenableParams.SCALE_Y, 1, 4, 3.5);
					boxTween.animate(TweenableParams.X, box.getPosition().x, this.getScenePanel().getWidth()/2 - this.box.getUnscaledWidth()*2, 370);
					boxTween.animate(TweenableParams.Y, box.getPosition().y, this.getScenePanel().getHeight()/2 - this.box.getUnscaledHeight()*2, 14);
					boxTween.addEventListener(this, TweenEvent.TWEEN_COMPLETE_EVENT);
					
					juggler.add(boxTween);
				}
				
				if(boi.isFalling() && plat1.collidesWith(boi) && boi.getPosition().getY() + boi.getUnscaledHeight() + boi.getVelocity() < plat1.getPosition().getY() + plat1.getUnscaledHeight()) {
					boi.setJumping(false);
					boi.setFalling(false);
					boi.setPosition(boi.getPosition().x, plat1.getPosition().y - boi.getUnscaledHeight());
					if(boi.getAnimate().equals("jumping")) {
						boi.setSpeed(7);
						boi.animate("walking");
					}
				}
	
				if(boi.isFalling() && plat2.collidesWith(boi) && boi.getPosition().getY() + boi.getUnscaledHeight() + boi.getVelocity() < plat2.getPosition().getY() + plat2.getUnscaledHeight()) {
					boi.setJumping(false);
					boi.setFalling(false);
					boi.setPosition(boi.getPosition().x, plat2.getPosition().y - boi.getUnscaledHeight());
					if(boi.getAnimate().equals("jumping")) {
						boi.setSpeed(7);
						boi.animate("walking");
					}
				}
				
				if(boi.isFalling() && plat3.collidesWith(boi) && boi.getPosition().getY() + boi.getUnscaledHeight() + boi.getVelocity() < plat3.getPosition().getY() + plat3.getUnscaledHeight()) {
					boi.setJumping(false);
					boi.setFalling(false);
					boi.setPosition(boi.getPosition().x, plat3.getPosition().y - boi.getUnscaledHeight());
					if(boi.getAnimate().equals("jumping")) {
						boi.setSpeed(7);
						boi.animate("walking");
					}
				}
			}
		}
		
	}
	
	@Override
	public void draw(Graphics g){
		super.draw(g);
		
		if(bg != null) {
			bg.draw(g);
		}
		
		if(plat1 != null) {
			plat1.draw(g);
		}
		if(plat2 != null) {
			plat2.draw(g);
		}
		if(plat3 != null) {
			plat3.draw(g);
		}
		
		if(boi != null) {
			boi.draw(g);
		}
		
		if(box != null) {
			box.draw(g);
		}
		
		if(questComplete != null && questManager.questCompleted) {
			g.setColor(Color.BLACK);
			g.setFont(new Font("Times New Roman", Font.PLAIN, 50));
			g.drawString(questComplete, 120, 90);
		}
		
	}
	
	public static void main(String[] args) throws LineUnavailableException, UnsupportedAudioFileException {
		LabSixGame game = new LabSixGame();
		game.start();

	}

	@Override
	public void handleEvent(Event event) {
		if(event.getSource().equals(this.boiTween)) {
			boiTween.removeEventListener(this, TweenEvent.TWEEN_COMPLETE_EVENT);
			canPlay = true;
		}
		if(event.getSource().equals(this.boxTween)) {
			boxTween.removeEventListener(this, TweenEvent.TWEEN_COMPLETE_EVENT);
			
			boxDisappearingTween.animate(TweenableParams.ALPHA, 1, 0, 3);
			boxDisappearingTween.addEventListener(this, TweenEvent.TWEEN_COMPLETE_EVENT);
			
			juggler.add(boxDisappearingTween);
		}
		if(event.getSource().equals(this.boxDisappearingTween)) {
			boxDisappearingTween.removeEventListener(this, TweenEvent.TWEEN_COMPLETE_EVENT);
			box.setVisible(false);
		}
		
	}
}
