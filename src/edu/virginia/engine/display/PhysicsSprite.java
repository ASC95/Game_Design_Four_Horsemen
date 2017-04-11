package edu.virginia.engine.display;

import java.util.ArrayList;

public class PhysicsSprite extends AnimatedSprite {
	// boolean to check if sprite is on solid ground and not jumping or sth
	boolean jumping = false;
	boolean falling = false;
	double accelerationX = 0;
	double accelerationY = 0;
	double velocityX = 0;
	double velocityY = 0;
	double gravity = 10;
	
	public PhysicsSprite(String id) {
		super(id);
	}
	
	public PhysicsSprite(String id, String key, String imageFileName) {
		super(id, key, imageFileName);
	}
	
	public PhysicsSprite(String id, String key, String imageFileName, int rows, int col) {
		super(id, key, imageFileName, rows, col);
	}

	public boolean isJumping() {
		return jumping;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public boolean isFalling() {
		return falling;
	}

	public void setFalling(boolean falling) {
		this.falling = falling;
	}
	
	public double getXAcceleration() {
		return accelerationX;
	}

	public double getYAcceleration() {
		return accelerationY;
	}
	public void setXAcceleration(double acceleration) {
		this.accelerationX = acceleration;
	}

	public void setYAcceleration(double acceleration) {
		this.accelerationY = acceleration;
	}

	public double getAccelerationX() {
		return accelerationX;
	}

	public void setAccelerationX(double accelerationX) {
		this.accelerationX = accelerationX;
	}

	public double getAccelerationY() {
		return accelerationY;
	}

	public void setAccelerationY(double accelerationY) {
		this.accelerationY = accelerationY;
	}

	public double getVelocityX() {
		return velocityX;
	}

	public void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
	}

	public double getVelocityY() {
		return velocityY;
	}

	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}

	public double getGravity() {
		return gravity;
	}

	public void setGravity(double gravity) {
		this.gravity = gravity;
	}
	
	@Override
	// TODO: this
	public void update(ArrayList<Integer> pressedKeys) {
		if(jumping || falling) {
			velocityY = velocityY + gravity;

		}
		if(!jumping && !falling) {
			velocityY = 0;
		}

		this.setPosition((int)(this.getPosition().x + velocityX), (int)(this.getPosition().y + velocityY));
		if(velocityY > 0) {
			falling = true;
			jumping = false;
		}
		super.update(pressedKeys);
	}

	public void jumpToCoordwithVelocity(double xCoord, double vx) {
		double displacement = xCoord - getPosition().getX();
		double frames = displacement/velocityX/2; //how many frames it will take
		double initialVelocityY = -(accelerationY * frames);
		velocityX = vx;
		velocityY = initialVelocityY;
		//return initialVelocityY;
	}
}
