package cs1302.game.api;

import cs1302.game.Util;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Asteroid extends PhysicSprite {

    private static final Random rng = new Random();

    private final Size size;

    public Asteroid(Size size) {
        super("file:resources/sprites/asteroid.png", size, size.getMassMultiplier() * 100);
        this.size = size;
        // this.mass += Math.random() * 50;
    }

    public boolean collided = false;

    @Override
    public void update(double time) {
        super.update(time);
    }

    @Override
    protected void onOffScreen() {
        velocityX = (rng.nextBoolean() ? rng.nextFloat() : -rng.nextFloat()) * 100;
        velocityY = (rng.nextBoolean() ? rng.nextFloat() : -rng.nextFloat()) * 100;
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
        // FIXME debug collisions
        if (collided) {
           gc.setFill(Color.YELLOW);
        }
        Shape collisionShape = getBoundary();
        // gc.fillOval(collisionShape.getBoundsInParent().getCenterX(), collisionShape.getBoundsInParent().getCenterY(), collisionShape.getBoundsInParent().getWidth(), collisionShape.getBoundsInParent().getHeight());
        gc.setFill(Color.GREEN);
    }

    @Override
    public Shape getBoundary() {
        Circle circle = new Circle(positionX - width / 4, positionY - height / 4, width / 4);
        circle.getTransforms().add(new Rotate(this.angle, positionX - width / 4, positionY - height / 3.5));
        return circle;
    }

    @Override
    public void onKill() {

    }

    public Size getSize() {
        return size;
    }

    public Set<Asteroid> getChildren() {
        Set<Asteroid> splitList = new HashSet<>();
        int pieces = rng.nextInt(3) + 1;
        Size newSize = Size.values()[size.ordinal() - 1];

        int hSpread = newSize.getWidth();
        int ySpread = newSize.getHeight();
        if (pieces == 1) {
            hSpread = 0;
            ySpread = 0;
        }
        for (int i = 0; i < pieces; i++) {
            Asteroid asteroid = new Asteroid(newSize);
            // Point2D newPos = Util.randomPointInCircle((Circle) getBoundary());
            // asteroid.setPosition(positionX + hSpread, positionY + ySpread);
            Point2D newVel = Util.randomVelocity(-velocityX * 1.5, velocityX * 1.5, -velocityY * 1.5, velocityY * 1.5);
            asteroid.setPosition(positionX, positionY);
            asteroid.setVelocity(newVel.getX(), newVel.getY(), rng.nextBoolean() ? -velocityR : velocityR);
            // TODO disable collisions until new collision system
            asteroid.setCollidable(false);
            /*if (rng.nextBoolean()) {
                asteroid.setVelocity(velocityX, velocityY, -velocityR);
            } else {
                asteroid.setVelocity(velocityX, -velocityY, velocityR);
            }*/
            hSpread -= newSize.getWidth() / 3;
            ySpread -= newSize.getHeight() / 2;
            splitList.add(asteroid);
        }
        return splitList;
    }

}
