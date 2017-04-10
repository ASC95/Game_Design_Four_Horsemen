package edu.virginia.engine.display;

/**
 * Created by Resquall on 4/8/2017.
 */
public class AttackHitbox extends DisplayObject {
    /** This class holds things that EventListeners can use when collision is detected in order to do stuff */
    // can a class listen to itself?

    // fields
    private int damage;
    private int knockback; // ???
    private int hitstun;
    private int hitstop;

    public AttackHitbox(String id){
        super(id);
        this.setVisible(false);
        this.setCollidable(false);
    }

    public AttackHitbox(String id, int damage, int knockback, int hitstun, int hitstop) {
        super(id);
        this.setVisible(false);
        this.setCollidable(false);
        this.damage = damage;
        this.knockback = knockback;
        this.hitstun = hitstun;
        this.hitstop = hitstop;
    }

    public AttackHitbox(String id, String fileName, int damage, int knockback, int hitstun, int hitstop) {
        super(id, fileName);
        this.setVisible(false);
        this.setCollidable(false);
        this.damage = damage;
        this.knockback = knockback;
        this.hitstun = hitstun;
        this.hitstop = hitstop;
    }
    // accessors

    public int getDamage() {
        return damage;
    }

    public int getKnockback() {
        return knockback;
    }

    public int getHitstun() {
        return hitstun;
    }

    public int getHitstop() {
        return hitstop;
    }
}
