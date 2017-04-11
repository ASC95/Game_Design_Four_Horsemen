package edu.virginia.engine.display;

import edu.virginia.engine.events.Event;

import java.util.*;

/**
 * Created by Resquall on 4/7/2017.
 */
public class ActionSprite extends PhysicsSprite {
    /* in attack */
	private boolean attacking;
	// can be interrupted; see notes for interrupt implementation
	private boolean interruptable;
	// if true, start follow-up attack at interrupt frame
	private boolean interrupted;

	private Action currentAction;
	private Action queuedAction;

	private boolean canMove = true;

	// list of hitboxes will just be children, unless we have a reason not to do so

	/* hashmap! containing corresponding start/end frames for given attack string */
	private HashMap<String, Action> attacks = new HashMap<>();

	private int attackSpeed = 1;
	private int frameCounter; // incremented in update()

    /* methods */

	public ActionSprite(String id) {
		super(id);
	}

	public ActionSprite(String id, String key, String imageFileName) {
		super(id, key, imageFileName);
	}

	public ActionSprite(String id, String key, String imageFileName, int rows, int col) {
		super(id, key, imageFileName, rows, col);
	}

	public boolean canMove() {
		return canMove;
	}

	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}

	/* create and establish attack name with corresponding start/end; add to hashmap */
	/* SEPARATE FROM ADDING CORRESPONDING HITBOXES */
	public void addAttack(String attack, Action action) {
		if (!attacks.containsKey(attack)) {
			attacks.put(attack, action);
		}
	}

	// must call startAttack
	public void setAttack(String attack) {
		if (attacks.containsKey(attack)) {
		    if (!attacking) {
				// if not in the middle of an attack, then currentAction should switch
				this.currentAction = attacks.get(attack);
			} else if (interruptable){
				// if in the middle of an attack, then just queue the attack instead
		    	this.queuedAction = attacks.get(attack);
			}
		}
	}

	/*
	public void setInterrupt(String attack) {
		if (attacks.containsKey(attack)) {
			this.queuedAction = attacks.get(attack);
			this.interrupted = true;
		}
	}
	*/
	public void fullInterrupt() {
		for (Action.HitboxList list : this.currentAction.getHitboxes()) {
			for (AttackHitbox a : list) {
				a.setCollidable(false);
				a.setVisible(false);
			}
		}
		this.stopAttack();
		this.queuedAction = null;
	}

	public void stopAttack() {
		this.attacking = false;
		this.interruptable = true;
		this.interrupted = false;
		this.currentAction = null;
		this.canMove = true;
		this.frameCounter = 0;
		this.dispatchEvent(new Event("ATTACK_END" + this.getId(), this));
	}

	public void startAttack() {
		this.attacking = true;
		this.interruptable = false;
		this.interrupted = false;
		this.canMove = false;
		this.frameCounter = 0;
	}

	public void setAttackSpeed(int speed) {
		this.attackSpeed = speed;
	}

	public boolean isAttacking() {
		return this.attacking;
	}

	public boolean isInterruptable() {
	    return this.interruptable;
    }

    public boolean isInterrupted() {
		return this.interrupted;
	}

	public String getCurrentAction() {
		for (Map.Entry<String, Action> entry : attacks.entrySet()) {
			if (Objects.equals(currentAction, entry.getValue()))
			    return entry.getKey();
		}
		return null;
	}

	/*
	public void addImage(String filename) {
		frames.add(filename);
	}
	*/

	public int getFrameCounter() {
		return this.frameCounter;
	}

	@Override
	public void update(ArrayList<Integer> pressedKeys) {
		super.update(pressedKeys);
        if (attacking) {
            if (frameCounter == currentAction.getEnd()) {
                // stop attacking
                this.stopAttack();
            } else {
            	// might not work -- reference vs value
				// first step - disable previous hitboxes
				if (frameCounter != 0) {
					for (AttackHitbox attack : currentAction.getHitboxes()[frameCounter - 1]) {
						attack.setCollidable(false);

						// TODO: this is a debugging line
						attack.setVisible(false);
					}
				}
				// every frame, update current hitboxes
				for (AttackHitbox attack : currentAction.getHitboxes()[frameCounter]) {
					attack.setCollidable(true);

					// TODO: this is a debugging line
					attack.setVisible(true);
				}

				// after input frame passes, able to queue action
				if (frameCounter == currentAction.getInput()) {
					interruptable = true;
				}
				// after interrupt frame passes, start queued action
				if (frameCounter >= currentAction.getInterrupt() && queuedAction != null) {
				    /*
				    currentAction = queuedAction;
					queuedAction = null;
					*/
				    this.stopAttack();
				    currentAction = queuedAction;
					this.startAttack();
				}
            }
			frameCounter++;
        }

	}



}
