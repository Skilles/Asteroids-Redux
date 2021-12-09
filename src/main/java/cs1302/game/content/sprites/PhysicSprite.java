package cs1302.game.content.sprites;

import javafx.geometry.Point2D;

public abstract class PhysicSprite extends Sprite {

    protected double mass;

    private PhysicSprite lastCollider;

    private boolean collidable;

    public PhysicSprite(String path, Size size, double mass) {
        super(path, size);
        this.mass = mass;
        this.collidable = true;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    public void collide(PhysicSprite other) {
        if (!collidable && !other.collidable) {
            return;
        }

        double totalMass = mass + other.mass;

        double xDist = other.position.getX() - position.getX();
        double yDist = other.position.getY() - position.getY();

        Point2D collisionVector = new Point2D(xDist, yDist).normalize();
        Point2D relVelocity = new Point2D(velocity.getX() - other.velocity.getX(), velocity.getY() - other.velocity.getY());

        // Check for if the objects are moving away from each other
        double dotProduct = collisionVector.dotProduct(relVelocity);
        if (dotProduct < 0) {
            return;
        }

        double speed = relVelocity.getX() * collisionVector.getX() + relVelocity.getY() * collisionVector.getY();
        double impulse = 2 * speed / totalMass;

        double velocityX = velocity.getX() - (impulse * other.mass * collisionVector.getX());
        double velocityY = velocity.getY() - (impulse * other.mass * collisionVector.getY());
        // Calculate the new rotational velocity TODO
        double rotVelocity = (velocity.getZ() + (impulse * other.mass * (collisionVector.getX())));

        setVelocity(velocityX, velocityY, velocity.getZ());
        other.setVelocity(other.velocity.getX() + (impulse * mass * collisionVector.getX()), other.velocity.getY() + (impulse * mass * collisionVector.getY()), other.velocity.getZ());

        this.lastCollider = other;
        other.lastCollider = this;
    }

    @Override
    protected void onKill() {
        explode();
    }

    @Override
    public void update(double delta) {
        super.update(delta);
    }

}
