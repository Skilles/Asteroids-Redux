package cs1302.game.content.sprites;

import javafx.geometry.Point2D;

/**
 * A PhysicSprite is a Sprite that can collide with other Sprites in a physical way. PhysicSprites
 * have a velocity and a mass responsible for calculating their movement and collision behavior.
 */
public abstract class PhysicSprite extends Sprite {

    protected double mass;

    private boolean collidable;

    /**
     * Instantiates a new Physic sprite.
     *
     * @param path the path of the image
     * @param size the size
     * @param mass the mass
     */
    public PhysicSprite(String path, Size size, double mass) {
        super(path, size);
        this.mass = mass;
        this.collidable = true;
    }

    /**
     * Sets whether the current sprite can be collided with.
     *
     * @param collidable the collidable
     */
    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    /**
     * Collide the current physical sprite with the given one. The two sprites will bounce off each
     * other and their velocities will be adjusted accordingly depending on their masses.
     *
     * @param other the other sprite to collide with
     */
    public void collide(PhysicSprite other) {
        if (!collidable && !other.collidable) {
            return;
        }

        double totalMass = mass + other.mass;

        double xDist = other.position.getX() - position.getX();
        double yDist = other.position.getY() - position.getY();

        Point2D collisionVector = new Point2D(xDist, yDist).normalize();
        Point2D relVelocity = new Point2D(velocity.getX() - other.velocity.getX(),
                velocity.getY() - other.velocity.getY());

        // Check for if the objects are moving away from each other
        double dotProduct = collisionVector.dotProduct(relVelocity);
        if (dotProduct < 0) {
            return;
        }

        double speed = relVelocity.getX() * collisionVector.getX()
                + relVelocity.getY() * collisionVector.getY();
        double impulse = 2 * speed / totalMass;

        double velocityX = velocity.getX() - (impulse * other.mass * collisionVector.getX());
        double velocityY = velocity.getY() - (impulse * other.mass * collisionVector.getY());

        setVelocity(velocityX, velocityY, velocity.getZ());
        other.setVelocity(other.velocity.getX() + (impulse * mass * collisionVector.getX()),
                other.velocity.getY() + (impulse * mass * collisionVector.getY()),
                other.velocity.getZ());
    }

    @Override
    protected void onKill() {
        explode();
    }

}
