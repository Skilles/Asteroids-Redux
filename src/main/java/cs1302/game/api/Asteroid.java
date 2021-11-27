package cs1302.game.api;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Asteroid extends PhysicSprite {

    static final double MAX_ROTATION_VELOCITY = 1;
    static final double MAX_VELOCITY = 50;

    private static final Random rng = new Random();

    private final Size size;

    public boolean collided = false;

    public Asteroid() {
        this(Size.HUGE);
    }

    public Asteroid(Size size) {
        super("file:resources/sprites/asteroid.png", size, size.getMassMultiplier() * 100);
        this.size = size;
        // this.mass += Math.random() * 50;
    }

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
        drawChildBoundary(gc, Color.BEIGE);
        if (collided) {
            drawBoundary(gc, Color.YELLOW);
        } else {
            drawBoundary(gc, Color.GREEN);
        }
    }

    private void drawChildBoundary(GraphicsContext gc, Color color) {
        gc.setFill(color);
        Shape collisionShape = getSpawnArea();
        double cx = collisionShape.getBoundsInParent().getMinX();
        double cy = collisionShape.getBoundsInParent().getMinY();
        double w = collisionShape.getBoundsInParent().getWidth();
        double h = collisionShape.getBoundsInParent().getHeight();
        gc.fillOval(cx, cy, w, h);
        gc.setFill(Color.GREEN);
    }

    @Override
    public Shape getBoundary() {
        Circle circle = new Circle(positionX, positionY, width / 4);
        // circle.getTransforms().add(new Rotate(this.angle, positionX, positionY));
        return circle;
    }

    @Override
    public void onKill() {

    }

    public Size getSize() {
        return size;
    }

    Set<Asteroid> getChildren() {
        Set<Asteroid> splitList = new HashSet<>();
        if (size == Size.TINY) {
            return splitList;
        }

        int pieces = rng.nextInt(3) + 1;
        Size newSize = Size.values()[size.ordinal() - 1];

        for (int i = 0; i < pieces; i++) {
            Asteroid asteroid = spawnChildInArea(getSpawnArea(), newSize);

            // Check if the asteroid is colliding with another asteroid
            if (splitList.stream().noneMatch(child -> child.intersects(asteroid))) {
                splitList.add(asteroid);
            } else {
                i--;
            }

        }
        return splitList;
    }

    public Shape getSpawnArea() {
        return new Circle(positionX, positionY, width / 2);
    }

    private static Asteroid spawnChildInArea(Shape shape, Size size) {
        Asteroid child = new Asteroid(size);
        double radius = shape.getBoundsInParent().getWidth() / 2;
        double x = shape.getBoundsInParent().getCenterX();
        double y = shape.getBoundsInParent().getCenterY();

        double a = rng.nextDouble();
        double b = rng.nextDouble();
        if (b < a) {
            double temp = b;
            b = a;
            a = temp;
        }
        double newPointX = x + radius * b * Math.cos(2 * Math.PI * a / b);
        double newPointY = y + radius * b * Math.sin(2 * Math.PI * a / b);

        child.setPosition(newPointX, newPointY);
        return child;
    }
}
