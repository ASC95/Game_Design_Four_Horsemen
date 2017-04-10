package edu.virginia.engine.display;

import edu.virginia.engine.events.Event;
import edu.virginia.engine.events.IEventListener;

import java.util.ArrayList;

/**
 * Created by Resquall on 4/8/2017.
 */
public class Player extends ActionSprite implements IEventListener {
    // fields???
	// could have fields of button presses corresponding to certain actions to enable remapping
    private int health;
	private boolean canInput;
	private boolean canMove;

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

	public boolean canMove() {
		return canMove;
	}

	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}

	@Override
    public void update(ArrayList<Integer> pressedKeys) {
		super.update(pressedKeys);
	}

}
