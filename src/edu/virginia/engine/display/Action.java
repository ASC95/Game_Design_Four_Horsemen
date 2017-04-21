package edu.virginia.engine.display;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Resquall on 4/8/2017.
 */
public class Action {
    /** Just a holder for various info, to be handled by an ActionSprite */
    private int input;
    private int interrupt;
    private int end;
    private HitboxList[] hitboxes;

    class HitboxList extends ArrayList<AttackHitbox> {

    }

    public Action(int input, int interrupt, int end) {
        this.input = input;
        this.interrupt = interrupt;
        this.end = end;
        // TODO: check bounds?
        this.hitboxes = new HitboxList[end];
        // preventing null pointer
        for (int i = 0; i < end; i++) {
            this.hitboxes[i] = new HitboxList();
        }
    }

    // adds hitbox to specified frame of the action
    public void addHitboxes(AttackHitbox hitbox, int index) {
        this.hitboxes[index].add(hitbox);
    }

    public int getInput() {
        return input;
    }

    public int getInterrupt() {
        return interrupt;
    }

    public int getEnd() {
        return end;
    }

    public HitboxList[] getHitboxes() {
        return hitboxes;
    }
}
