package edu.virginia.lab1test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import edu.virginia.engine.display.AnimatedSprite;
import edu.virginia.engine.display.DisplayObject;
import edu.virginia.engine.display.Game;
import edu.virginia.engine.display.Sprite;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.QuestManager;

public class LabFourGame extends Game {

	static int screenWidth = 600;
	static int screenHeight = 300;
	AnimatedSprite boi = new AnimatedSprite("boi", "standing", "standing.png");
	Sprite bg = new Sprite("bg", "bg0000.png");
	Sprite box = new Sprite("box", "box.png");
	QuestManager questManager = new QuestManager();
	Event pickup = new Event("PickedUpEvent.COIN_PICKED_UP", box);
	String questComplete = "Quest completed!";
	
	public LabFourGame() {
		super("Lab Three Test Game", screenWidth, screenHeight);
		boi.setPosition(50, (int) (this.getScenePanel().getHeight() - boi.getUnscaledHeight()*boi.getScaleX() - 40));
		this.getScenePanel().setBackground(Color.LIGHT_GRAY);
		boi.addImage("walking", "walk1.png", 1, 2);
		box.setPosition(this.getScenePanel().getWidth() - box.getUnscaledWidth() - 50, this.getScenePanel().getHeight() - box.getUnscaledHeight() - 40);
		box.addEventListener(questManager, "PickedUpEvent.COIN_PICKED_UP");
		bg.setPosition(bg.getPosition().x, bg.getPosition().y - 20);
	}
	
	@Override
	public void update(ArrayList<String> pressedKeys){
		super.update(pressedKeys);
		
		if(box != null) box.update(pressedKeys);
		
		if(boi != null)  {
			if(pressedKeys.contains("Left") || pressedKeys.contains("Right")) {
				
				if(pressedKeys.contains("Left")) {
					boi.setPosition(boi.getPosition().x += -5, boi.getPosition().y);
					if(pressedKeys.contains("Right") && pressedKeys.indexOf("Right") > pressedKeys.indexOf("Left")) {
						boi.setPosition(boi.getPosition().x - 5, boi.getPosition().y); // so that sprite will still move 
					}
					if(boi.getScaleX() > 0) {
						boi.setScaleX(-1);
						boi.setPosition(boi.getPosition().x + boi.getUnscaledWidth(), boi.getPosition().y);
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
				}
				
				if(!boi.isPlaying()) {
					boi.setSpeed(7);
					boi.animate("walking");
				}
			} else {

				boi.animate("standing");
				boi.stop();
			}
			Rectangle hitbox;
			if(boi.getScaleX() == -1) {
				hitbox = new Rectangle(boi.getUnscaledWidth(), 0, (int)((this.getScenePanel().getWidth())), (int)((this.getScenePanel().getHeight() - boi.getUnscaledHeight())));
			} else {
				hitbox = new Rectangle(0, 0, (int)((this.getScenePanel().getWidth() - boi.getUnscaledWidth())), (int)((this.getScenePanel().getHeight() - boi.getUnscaledHeight())));

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
			
			Rectangle rect = new Rectangle(boi.getPosition().x, boi.getPosition().y, (int)(boi.getUnscaledWidth()*boi.getScaleX()), (int)(boi.getUnscaledHeight()*boi.getScaleY()));
			Rectangle boxRect = new Rectangle(box.getPosition().x, box.getPosition().y, box.getUnscaledWidth(), box.getUnscaledHeight());
			if(rect.contains(boxRect)) {
				box.setVisible(false);
				box.dispatchEvent(pickup);
				box.removeEventListener(questManager, "PickedUpEvent.COIN_PICKED_UP");
			}
			boi.update(pressedKeys);
		}
		
	}
	
	@Override
	public void draw(Graphics g){
		super.draw(g);
		if(bg != null) {
			bg.draw(g);
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
	
	public static void main(String[] args) {
		LabFourGame game = new LabFourGame();
		game.start();

	}
}
