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

    /**
     * https://courses.lumenlearning.com/boundless-physics/chapter/collisions/
     * https://code.tutsplus.com/tutorials/playing-around-with-elastic-collisions--active-7472
     * https://spicyyoghurt.com/tutorials/html5-javascript-game-development/collision-detection-physics
     */
    public void collide(PhysicSprite other) {
        if (!collidable && !other.collidable) {
            return;
        }

        double totalMass = mass + other.mass;

        double xDist = other.positionX - positionX;
        double yDist = other.positionY - positionY;

        Point2D collisionVector = new Point2D(xDist, yDist).normalize();
        Point2D relVelocity = new Point2D(velocityX - other.velocityX, velocityY - other.velocityY);

        // Check for if the objects are moving away from each other
        double dotProduct = collisionVector.dotProduct(relVelocity);
        if (dotProduct < 0) {
            return;
        }

        double speed = relVelocity.getX() * collisionVector.getX() + relVelocity.getY() * collisionVector.getY();
        double impulse = 2 * speed / totalMass;

        velocityX -= (impulse * other.mass * collisionVector.getX());
        velocityY -= (impulse * other.mass * collisionVector.getY());
        other.velocityX += (impulse * mass * collisionVector.getX());
        other.velocityY += (impulse * mass * collisionVector.getY());

        this.lastCollider = other;
        other.lastCollider = this;
    }

    @Override
    public void update(double delta) {
        super.update(delta);
    }

}
