package edu.virginia.lab1test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import edu.virginia.engine.display.DisplayObject;
import edu.virginia.engine.display.Game;
import edu.virginia.engine.display.Sprite;

public class LabThreeDemo extends Game {

	static int screenWidth = 900;
	static int screenHeight = 700;
	Sprite center = new Sprite("center");
	Sprite sun = new Sprite("sun", "sun-sol.png");
	DisplayObject moon = new DisplayObject("moon", "earth-moon-luna.png");
	Sprite mercury = new Sprite("mercury", "planet-mercury.png");
	Sprite venus = new Sprite("venus", "planet-venus.png");
	Sprite earth = new Sprite("earth", "planet-earth.png");
//	Sprite mars = new Sprite("mars", "planet-mars.png");
//	Sprite jupiter = new Sprite("jupiter", "planet-jupiter.png");
//	Sprite saturn = new Sprite("saturn", "planet-saturn.png");
//	Sprite neptune = new Sprite("neptune", "planet-neptune.png");
//	Sprite uranus = new Sprite("uranus", "planet-uranus.png");
	int sunRotation = 0;
	Sprite invisMerc = new Sprite("invisMerc");
	Sprite invisVen = new Sprite("invisVen");
	Sprite invisEarth = new Sprite("invisEarth");
	int inMercRot = 0;
	int inVenRot = 0;
	int inEarRot = 0;
	int earthRot = 0;
	int venRot = 0;
	int mercRot = 0;
	int moonRot = 0;
	boolean moveLeft = false;

	
	public LabThreeDemo() {
		super("Lab Three Test Game", screenWidth, screenHeight);
		this.getScenePanel().setBackground(Color.BLACK);
		sun.setScaleX(0.5);
		sun.setScaleY(0.5);
		Point p = new Point((screenWidth), (screenHeight));
		Point pt = new Point(-sun.getUnscaledWidth()/2, -sun.getUnscaledHeight()/2);
		sun.setPivotPoint(pt);
		
		sun.addChild(invisMerc);
		sun.addChild(invisVen);
		sun.addChild(invisEarth);
		
		for(int i = 0; i < sun.getChildren().size(); i++) {
			DisplayObject child = sun.getChildAtIndex(i);
			child.setPivotPoint(child.getUnscaledWidth(), child.getUnscaledHeight());
			sun.getChildAtIndex(i).setScaleX(0.3333333);
			sun.getChildAtIndex(i).setScaleY(0.3333333);

		}
		earth.setPivotPoint(-earth.getUnscaledWidth()/2, -earth.getUnscaledWidth()/2);
		mercury.setPivotPoint(-mercury.getUnscaledWidth()/2, -mercury.getUnscaledWidth()/2);
		venus.setPivotPoint(-venus.getUnscaledWidth()/2, -venus.getUnscaledWidth()/2);
		
		mercury.setScaleX(0.6);
		mercury.setScaleY(0.6);
		
		venus.setScaleX(0.8);
		venus.setScaleY(0.8);
		
		mercury.setPosition(500, 0);
		venus.setPosition(800,0);
		earth.setPosition(1450, 0);
		
		invisMerc.addChild(mercury);
		invisVen.addChild(venus);
		invisEarth.addChild(earth);
		
		invisMerc.setPosition(invisMerc.getPosition().x + 50, invisMerc.getPosition().y);
		invisVen.setPosition(invisVen.getPosition().x + 50, invisMerc.getPosition().y);
		invisEarth.setPosition(invisEarth.getPosition().x + 180, invisEarth.getPosition().y);
		moon.setPosition(moon.getPosition().x + 400, moon.getPosition().y);
		
		moon.setScaleX(0.3);
		moon.setScaleY(0.3);
		moon.setPivotPoint(moon.getUnscaledWidth()*2, moon.getUnscaledHeight()*2);
		
		earth.addChild(moon);
		center.addChild(sun);
		center.setPosition(screenWidth/2, screenHeight/2);
 	}
	
	@Override
	public void update(ArrayList<String> pressedKeys){
		super.update(pressedKeys);
		if(sun != null) {
			
			inMercRot = (inMercRot+5)%720;
			invisMerc.setRotation(inMercRot/2);
			
			inVenRot = (inVenRot+4)%720;
			invisVen.setRotation(inVenRot/2);
			
			inEarRot = (inEarRot+1)%360;
			invisEarth.setRotation(inEarRot);
			
			earthRot = (earthRot+2)%360;
			earth.setRotation(earthRot);
			
			mercRot = (mercRot+1)%720;
			mercury.setRotation(mercRot/2);
			
			venRot = (venRot+2)%720;
			venus.setRotation(venRot/2);
			
			moonRot = (moonRot+1)%720;
			moon.setRotation(moonRot/2);
			
			int rotation = sun.getRotation();
			if(pressedKeys.contains("A")) { // clockwise
				rotation = (rotation+3)%360;
				if(pressedKeys.contains("S") && pressedKeys.indexOf("A") < pressedKeys.indexOf("S")) {
					rotation = (rotation+5)%360;
				}
			}
			if(pressedKeys.contains("S")) { // counterclockwise
				rotation = (rotation-5)%360;
				if(pressedKeys.contains("A") && pressedKeys.indexOf("S") < pressedKeys.indexOf("A")) {
					rotation = (rotation-5)%360;
				}
			}
			sun.setRotation(rotation);
			
			/* panning */
			if(pressedKeys.contains("Up")) {
				sun.getPosition().y += -10;
				if(pressedKeys.contains("Down") && pressedKeys.indexOf("Up") < pressedKeys.indexOf("Down")) {
					sun.getPosition().y += -10; // so that sprite will still move 
				}
			}
			if(pressedKeys.contains("Down")) {
				sun.getPosition().y += 10;
				if(pressedKeys.contains("Up") && pressedKeys.indexOf("Up") > pressedKeys.indexOf("Down")) {
					sun.getPosition().y += 10; // so that sprite will still move 
				}
			}
			if(pressedKeys.contains("Left")) {
				sun.getPosition().x += -10;
				if(pressedKeys.contains("Right") && pressedKeys.indexOf("Right") > pressedKeys.indexOf("Left")) {
					sun.getPosition().x += -10; // so that sprite will still move 
				}
			}
			if(pressedKeys.contains("Right")) {
				sun.getPosition().x += 10;
				if(pressedKeys.contains("Left") && pressedKeys.indexOf("Right") < pressedKeys.indexOf("Left")) {
					sun.getPosition().x += 10; // so that sprite will still move 
				}
			}
			
			/* scaling */
			double scale = center.getScaleX();
			if(pressedKeys.contains("Q") && scale < 3.0) { // zoom in
				scale += 0.05;
				if(pressedKeys.contains("W") && pressedKeys.indexOf("Q") < pressedKeys.indexOf("W")) {
					scale += 0.05;
				}
				
			}
			if(pressedKeys.contains("W") && scale > 0.1) { // zoom out
				scale -= 0.05;
				if(pressedKeys.contains("Q") && pressedKeys.indexOf("W") < pressedKeys.indexOf("Q")) {
					scale -= 0.05;
				}
			}
			center.setScaleX(scale);
			center.setScaleY(scale);
			
			int newX = invisMerc.getPosition().x;
			if(invisMerc.getRotation() > 0 && invisMerc.getRotation() < 180) {
					newX -= 2;
			} else {
					newX += 2;
			}
			invisMerc.setPosition(newX, invisMerc.getPosition().y);
			
			newX = invisVen.getPosition().x;
			if(invisVen.getRotation() > 0 && invisVen.getRotation() < 180) {
					newX -= 2;
			} else {
					newX += 2;
			}
			invisVen.setPosition(newX, invisVen.getPosition().y);
			
			newX = invisEarth.getPosition().x;
			if(invisEarth.getRotation() > 0 && invisEarth.getRotation() < 180) {
					newX -= 2;
			} else {
					newX += 2;
			}
			invisEarth.setPosition(newX, invisEarth.getPosition().y);
			
			newX = moon.getPosition().x;
			if(moon.getRotation() > 0 && moon.getRotation() < 180) {
					newX -= 2;
			} else {
					newX += 2;
			}
			moon.setPosition(newX, moon.getPosition().y);
		}		
		
	}
	
	@Override
	public void draw(Graphics g){
		super.draw(g);
		if(center != null) {
			center.draw(g);
		}
	}
	
	public static void main(String[] args) {
		LabThreeDemo game = new LabThreeDemo();
		game.start();

	}
}
