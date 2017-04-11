package edu.virginia.engine.display;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Resquall on 4/8/2017.
 */
public class Player extends ActionSprite implements IEventListener {
    // fields???
	// could have fields of button presses corresponding to certain actions to enable remapping
    private int health;

    private boolean hasDJ;

	private boolean canInput = true;
	private boolean dashing = true;

	private int iFrames;

	public Player(String id, String key, String imageFileName) {
		super(id, key, imageFileName);
	}

	public Player(String id, String key, String imageFileName, int rows, int col) {
		super(id, key, imageFileName, rows, col);
	}

    public void handleEvent(Event e) {
        // TODO: collision throws events
    }

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public boolean canInput() {
		return canInput;
	}

	public void setCanInput(boolean canInput) {
		this.canInput = canInput;
	}

	public boolean canDJ() {
		return hasDJ;
	}

	public void setDJ(boolean canDJ) {
	    this.hasDJ = canDJ;
	}

	public void setiFrames (int iFrames) {
	    this.iFrames = iFrames;
    }

	@Override
    public void update(ArrayList<Integer> pressedKeys) {
		super.update(pressedKeys);

	}

	/*
	@Override
	public void draw(Graphics g){
        super.draw(g);
	}
	*/

}
