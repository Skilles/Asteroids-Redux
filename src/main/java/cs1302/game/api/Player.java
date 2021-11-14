package cs1302.game.api;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.List;

public class Player extends Sprite {

    private final double hAcceleration;
    private final double vAcceleration;

    // FIXME need cooldown manager tied to delta
    private final int COOLDOWN = 70;
    private int currentCooldown = 0;

    public Player() {
        super("file:resources/sprites/spaceship.png", Size.SMALL);
        hAcceleration = 500;
        vAcceleration = 500;
    }

    public void turnRight(double delta) {
        // velocityR = 1;
        addRotation(150 * delta);
    }

    public void turnLeft(double delta) {
        // velocityR = -1;
        addRotation(-150 * delta);
    }

    public void accelerate(double time) {
        velocityX += hAcceleration * Math.sin(radAngle) * time;
        velocityY -= vAcceleration * Math.cos(radAngle) * time;
    }

    public void decelerate(double time) {
        velocityX -= hAcceleration * Math.sin(radAngle) * time;
        velocityY += vAcceleration * Math.cos(radAngle) * time;
    }

    public void brake(double delta, double force) {
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
        force = force * delta;

        velocityX *= (1 - force / 100);
        velocityY *= (1 - force / 100);
        velocityR *= 0.95 * delta;
    }

    @Override
    public void update(double time) {
        super.update(time);
        if (currentCooldown > 0) {
            currentCooldown--;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
        Shape collisionShape = getBoundary();
        gc.fillRect(collisionShape.getBoundsInParent().getCenterX(), collisionShape.getBoundsInParent().getCenterY(), collisionShape.getBoundsInParent().getWidth(), collisionShape.getBoundsInParent().getHeight());
        // gc.fillRect(positionX - width / 3, positionY - height / 2.5, width * (2.0 / 3), height * (2.0 / 3));
    }

    @Override
    public Shape getBoundary() {
        // Rectangle rectangle = new Rectangle(positionX - width / 3, positionY - height / 2.5, width * (2.0 / 3), height * (2.0 / 3));
        Rectangle rectangle = new Rectangle(positionX - width / 1.5, positionY - height / 1.5, width * (2.0 / 3), height * (2.0 / 3));
        // rectangle.getTransforms().add(new Rotate(this.angle, rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2));
        // rectangle.setRotate(this.angle);
        return rectangle;
    }

    @Override
    protected void onOffScreen() {

    }

    @Override
    public void onKill() {

    }

    public void shoot(List<Bullet> bulletList, double delta) {
        if (currentCooldown == 0) {
            currentCooldown = COOLDOWN;
            delta = delta * 500;
            bulletList.add(new Bullet(this, 300 * delta));
        }
    }

}
