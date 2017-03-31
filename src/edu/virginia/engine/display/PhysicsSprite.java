package edu.virginia.engine.display;

import java.util.ArrayList;

public class PhysicsSprite extends AnimatedSprite {
	// boolean to check if sprite is on solid ground and not jumping or sth
	boolean jumping = false;
	boolean falling = false;
	double acceleration = 0;
	double velocity = 0;
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
	
	public double getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public double getGravity() {
		return gravity;
	}

	public void setGravity(double gravity) {
		this.gravity = gravity;
	}
	
	@Override
	public void update(ArrayList<Integer> pressedKeys) {
		if(jumping || falling) {
			velocity = velocity + gravity;
			this.setPosition(this.getPosition().x, (int)(this.getPosition().y - velocity));
			if(velocity < 0) {
				falling = true;
				jumping = false;
			}	
		}
		if(!jumping && !falling) {
			velocity = 0;
		}
		super.update(pressedKeys);
	}
}
