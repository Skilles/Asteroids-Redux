package cs1302.game.content.sprites;

public abstract class PhysicSprite extends Sprite {

    double mass;

    private PhysicSprite lastCollider;

    private boolean collidable;

    public PhysicSprite(String path, Size size, double mass) {
        super(path, size);
        this.mass = mass;
        this.collidable = true;
    }

    double momentumX() {
        return mass * velocityX;
    }

    double momentumY() {
        return mass * velocityY;
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
     */
    public void collide(PhysicSprite other) {
        if (!collidable && !other.collidable) {
            return;
        }

        double totalMass = mass + other.mass;

        double xDist = positionX - other.positionX;
        double yDist = positionY - other.positionY;

        double collisionAngle = Math.atan(yDist / xDist);
        double magnitude = Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2));
        double otherMagnitude = Math.sqrt(Math.pow(other.velocityX, 2) + Math.pow(other.velocityY, 2));
        double angle = Math.atan(velocityY / velocityX);
        double otherAngle = Math.atan(other.velocityY / other.velocityX);

        double velocityX = magnitude * Math.cos(angle - collisionAngle);
        final double velocityY = magnitude * Math.sin(angle - collisionAngle);
        double otherVelocityX = otherMagnitude * Math.cos(otherAngle - collisionAngle);
        final double otherVelocityY = otherMagnitude * Math.sin(otherAngle - collisionAngle);

        final double finalVelocityX = ((mass - other.mass) * velocityX + (2 * other.mass) * otherVelocityX) / (totalMass);
        final double finalOtherVelocityX = ((2 * mass) * velocityX + (other.mass - mass) * otherVelocityX) / (totalMass);

        final double newVelocityX = Math.cos(collisionAngle) * finalVelocityX + Math.cos(collisionAngle + Math.PI / 2) * velocityY;
        final double newVelocityY = Math.sin(collisionAngle) * finalVelocityX + Math.sin(collisionAngle + Math.PI / 2) * velocityY;
        final double otherNewVelocityX = Math.cos(collisionAngle) * finalOtherVelocityX + Math.cos(collisionAngle + Math.PI / 2) * otherVelocityY;
        final double otherNewVelocityY = Math.sin(collisionAngle) * finalOtherVelocityX + Math.sin(collisionAngle + Math.PI / 2) * otherVelocityY;

        setVelocity(newVelocityX, newVelocityY, velocityR);
        other.setVelocity(otherNewVelocityX, otherNewVelocityY, other.velocityR);

        this.lastCollider = other;
        other.lastCollider = this;

    }

    @Override
    public void update(double time) {
        super.update(time);
    }

}
