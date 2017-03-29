package edu.virginia.lab1test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayList;

import edu.virginia.engine.display.AnimatedSprite;
import edu.virginia.engine.display.Game;
import edu.virginia.engine.display.Sprite;

/**
 * Example game that utilizes our engine. We can create a simple prototype game with just a couple lines of code
 * although, for now, it won't be a very fun game :)
 * */

/* 
 * image from https://www.youtube.com/watch?v=kV-lVlcKuC8 around 2:05
 * found the image with google I didn't actually watch the vid
 */
public class LabTwoGame extends Game implements MouseListener{

	/* Create a sprite object for our game. We'll use mario */
	AnimatedSprite mario = new AnimatedSprite("Mario", "still", "Mario.png");
	Sprite bg = new Sprite("bg", "mariobg.jpg");
	/* position displacement*/
	int inc = 20;
	/* stores x and y pos of mario */
//	int xPos = 0;
//	int yPos = 0;
	int health = 15;
	/* time in sec */
	int time = 60;
	/* frames */
	int timer = 3600;
	String winner = "";
	/* mouse pos */
	int mouseX = -1;
	int mouseY = -1;
	boolean gameOver = false;
	/* variables for screen width & height to make changing it easier later */
	static int screenWidth = 570;
	static int screenHeight = 500;
	boolean goLeft = false;
	Rectangle hitbox = new Rectangle((int)(mario.getPosition().getX()*mario.getScaleX()), (int)(mario.getPosition().getY()*mario.getScaleY()), (int)(mario.getUnscaledWidth()*mario.getScaleX()), (int)((mario.getUnscaledHeight())*mario.getScaleY()));
	
	/**
	 * Constructor. See constructor in Game.java for details on the parameters given
	 * */
	public LabTwoGame() {
		super("Lab One Test Game", screenWidth, screenHeight);
		super.getScenePanel().addMouseListener(this);
		mario.addImage("running", "MarioRun.png", 1, 2);
 	}
	
	/**
	 * Engine will automatically call this update method once per frame and pass to us
	 * the set of keys (as strings) that are currently being pressed down
	 * */
	@Override
	public void update(ArrayList<String> pressedKeys){
		super.update(pressedKeys);		
		
		/* check if game is over, if not advance the clock */
		if((timer != 0) && !gameOver) {
			timer -= 1;
			if(timer%60 == 0) {
				time -= 1;
				/* makes mario speed up over time */
				inc+=1;
			}
		}
		
		/* check for win conditions */
		if((timer == 0) && !(health == 0)) {
			gameOver = true;
			winner = "P1 WIN!";
		}
		if(health == 0) {
			gameOver = true;
			winner = "P2 WIN!";
		}
		
		/* Make sure mario is not null. Sometimes Swing can auto cause an extra frame to go before everything is initialized */
		if(mario != null)  {
			if(pressedKeys.contains("Up") || pressedKeys.contains("Down") || pressedKeys.contains("Left") || pressedKeys.contains("Right")) {
					mario.animate("running");
					mario.start();
			} else {

					mario.animate("still");
					mario.stop();
			}
			
			mario.update(pressedKeys);
		}
		
		if(mario != null) {
			int marioX = (int)(mario.getUnscaledWidth()*mario.getScaleX());
			int marioY = (int)(mario.getUnscaledHeight()*mario.getScaleY());
			
			Rectangle hitbox = new Rectangle(0, 0, (int)((screenWidth - 7 - marioX)*(1/mario.getScaleX())), (int)((screenHeight - 30  - marioY)*(1/mario.getScaleY())));

			if(!hitbox.contains(mario.getPosition())) {
				int x;  
				int y;
				if(mario.getPosition().y > hitbox.height) {
					y = hitbox.height;
				} else if(mario.getPosition().y < hitbox.y) {
					y = hitbox.y;
				} else {
					y = mario.getPosition().y;
				}
				if(mario.getPosition().x > hitbox.width) {
					x = hitbox.width;
				} else if(mario.getPosition().x < hitbox.x) {
					x = hitbox.x;
				} else {
					x = mario.getPosition().x;
				}
				Point p = new Point(x, y);
				mario.setPosition(p);
			}
		}
	}
	
	/**
	 * Engine automatically invokes draw() every frame as well. If we want to make sure mario gets drawn to
	 * the screen, we need to make sure to override this method and call mario's draw method.
	 * */
	@Override
	public void draw(Graphics g){
		super.draw(g);
		if(bg != null) bg.draw(g);
		/* Same, just check for null in case a frame gets thrown in before Mario is initialized */
		if(mario != null) {
				mario.draw(g);
		}
		/* draws all text needed */
		g.setFont(new Font("Helvetica", Font.PLAIN, 20));
		g.setColor(Color.red);
		g.drawString("Health: " + health, screenWidth/2 -50, 20);
		g.setFont(new Font("Helvetica", Font.PLAIN, 20));
		g.setColor(Color.black);
		g.drawString("Time: " + time, screenWidth - 90, 20);
		g.setFont(new Font("Helvetica", Font.PLAIN, 125));
		g.setColor(Color.green);
		g.drawString(winner, screenWidth/2 - 240, screenHeight/2 + 20);
		
	}

	/**
	 * Quick main class that simply creates an instance of our game and starts the timer
	 * that calls update() and draw() every frame
	 * */
	public static void main(String[] args) {
		LabTwoGame game = new LabTwoGame();
		game.start();

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		/* gets mouse x and y point clicked */
		mouseX = arg0.getX();
		mouseY = arg0.getY();
				
		/* draws rectangle around mario when mouse is clicked and checks if the
		 * mouse click is inside the rectangle
		 * (check if mouse has clicked on mario)
		 */

		hitbox.setBounds((int)(mario.getPosition().getX()*mario.getScaleX()), (int)(mario.getPosition().getY()*mario.getScaleY()), (int)(mario.getUnscaledWidth()*mario.getScaleX()), (int)((mario.getUnscaledHeight())*mario.getScaleY()));
		AffineTransform transform = new AffineTransform();
		transform.rotate(Math.toRadians(mario.getRotation()), (int)(mario.getPosition().getX()*mario.getScaleX()), (int)(mario.getPosition().getY()*mario.getScaleY()));
		Path2D pathBox = (Path2D) transform.createTransformedShape(hitbox);
		
		if(pathBox.contains(mouseX, mouseY) && (health > 0)) {
			System.out.println("click");
			if(!gameOver) {
				health -= 1;
			}
		}
	}
	

	@Override
	public void mouseReleased(MouseEvent arg0) {
		mouseX = -1;
		mouseY = -1;
	}
}
