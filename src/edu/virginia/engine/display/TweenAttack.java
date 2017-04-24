package edu.virginia.engine.display;

/**
 * Created by Resquall on 4/24/2017.
 */
public class TweenAttack extends Tween{

    public TweenAttack(AttackHitbox attack) {
        super(attack);
        this.tween.setCollidable(true);
        this.tween.setVisible(true);
    }

    @Override
    public void update() {
        // can take this out and put it in constructor if projectiles are dynamically created
        /*
        if (!this.tween.isCollidable()) {
            this.tween.setCollidable(true);
            this.tween.setVisible(true);
        }
        */
        super.update();
        if (this.isComplete()) {
            this.tween.setCollidable(false);
            this.tween.setVisible(false);
            // NOTE: ASSUMES DYNAMICALLY CREATED HITBOX
            // also requires unique id. this doesn't work?
            this.tween.getParent().removeChild(this.tween.getId());
        }
    }
}
