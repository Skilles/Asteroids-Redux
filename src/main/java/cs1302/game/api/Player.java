package cs1302.game.api;

public class Player extends Sprite {

    private double hAcceleration;
    private double vAcceleration;

    public Player() {
        super("file:resources/sprites/spaceship.png");
        hAcceleration = 500;
        vAcceleration = 500;
    }

    public void accelerate(double time) {
        velocityX += hAcceleration * Math.sin(radAngle) * time;
        velocityY -= vAcceleration * Math.cos(radAngle) * time;
    }

    public void decelerate(double time) {
        velocityX -= hAcceleration * Math.sin(radAngle) * time;
        velocityY += vAcceleration * Math.cos(radAngle) * time;
    }

    public void brake(float force) {
        /*if (velocityX > 0) {
            velocityX -= Math.min(force, velocityX);
        }
        if (velocityX < 0) {
            velocityX += Math.min(force, -velocityX);
        }
        if (velocityY > 0) {
            velocityY -= Math.min(force, velocityY);
        }
        if (velocityY < 0) {
            velocityY += Math.min(force, -velocityY);
        }*/

        velocityX *= (1 - force / 100);
        velocityY *= (1 - force / 100);
    }

}
