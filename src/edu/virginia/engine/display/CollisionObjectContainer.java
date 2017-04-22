package edu.virginia.engine.display;

import java.util.ArrayList;

/**
 * Created by austinchang on 4/18/17.
 */
public class CollisionObjectContainer extends DisplayObjectContainer {

    public CollisionObjectContainer(String id) {
        super(id);
    }

    @Override
    protected void update(ArrayList<Integer> pressedKeys) {
        super.update(pressedKeys);
        for (DisplayObject child1 : getChildren()) {
            ((PhysicsSprite) child1).setFalling(true);
            for (DisplayObject child2 : getChildren()) {
                if (areColliding((PhysicsSprite) child1, (PhysicsSprite) child2)) {
                    handleCollision((PhysicsSprite) child1, (PhysicsSprite) child2);
                    System.out.println("Collision between " + child1.getId() + " and " + child2.getId());
                }
            }
        }
    }

    public boolean areColliding(PhysicsSprite sprite1, PhysicsSprite sprite2) {
        return sprite1 != sprite2 && sprite1.getHitBox().intersects(sprite2.getHitBox().getBounds());
    }

    public void handleCollision(PhysicsSprite sprite1, PhysicsSprite sprite2) {
        PhysicsSprite player;
        PhysicsSprite obstacle;
        if (sprite1.isPlayerControlled()) {
            player = sprite1;
            obstacle = sprite2;
        } else {
            player = sprite2;
            obstacle = sprite1;
        }
        //left collision
        if (player.getHitBox().getBounds().getX() + player.getHitBox().getBounds().getWidth() - obstacle.getHitBox().getBounds().getX() < 20) {
            player.getPosition().setLocation(obstacle.getPosition().getX() - player.getHitBox().getBounds().getWidth()/2, player.getPosition().getY());
        }
        //right collision
        else if(obstacle.getHitBox().getBounds().getX() + obstacle.getHitBox().getBounds().getWidth() < player.getHitBox().getBounds().getX() + player.getHitBox().getBounds().getWidth()/2) {
            player.getPosition().setLocation(obstacle.getPosition().getX() + obstacle.getHitBox().getBounds().getWidth() + player.getHitBox().getBounds().getWidth()/2, player.getPosition().getY());
        }
        //bottom collision
        else if (obstacle.getHitBox().getBounds().getY() + obstacle.getHitBox().getBounds().getHeight() - player.getHitBox().getBounds().getY() < 30) {
            player.setVelocityY(0);
        }
        //top collision. If we are colliding, then velocityY is set to 0, hence no jump
        else {
//            player.setVelocityY(0);
            player.getPosition().setLocation(player.getPosition().getX(), obstacle.getHitBox().getBounds().getY() - player.getHitBox().getBounds().getHeight()/2);
            player.setJumping(false);
            player.setFalling(false);
            //((Player) player).setLanding();
        }
    }
}

