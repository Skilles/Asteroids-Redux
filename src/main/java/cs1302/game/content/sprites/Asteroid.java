package cs1302.game.content.sprites;

import cs1302.game.content.Globals;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.HashSet;
import java.util.Set;

/**
 * An asteroid is a sprite that can be destroyed by bullets and split apart. Collides with other
 * asteroids and the player but not enemies. Smaller pieces are worth more points and have less
 * mass.
 */
public class Asteroid extends PhysicSprite {

    public static final double MAX_ROTATION_VELOCITY = 1;

    private final Size size;

    /**
     * Instantiates a new Asteroid.
     *
     * @param size the size
     */
    public Asteroid(Size size) {
        super("file:resources/sprites/asteroid.png", size, getMass(size));
        this.size = size;
        minSpeed = 150;
        maxSpeed = 300;
        setRotation(RNG.nextDouble() * 360);
        randomVelocity();
    }

    @Override
    public void randomVelocity() {
        super.randomVelocity();
        setVelocity(velocity.getX() / (1 + size.ordinal()),
                velocity.getY() / (1 + size.ordinal()),
                (RNG.nextDouble() * 2 - 1) * MAX_ROTATION_VELOCITY);
    }

    @Override
    public void update(double delta) {
        super.update(delta);
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
    }

    /*private void drawChildBoundary(GraphicsContext gc, Color color) {
        gc.setFill(color);
        Shape collisionShape = getSpawnArea();
        double cx = collisionShape.getBoundsInParent().getMinX();
        double cy = collisionShape.getBoundsInParent().getMinY();
        double w = collisionShape.getBoundsInParent().getWidth();
        double h = collisionShape.getBoundsInParent().getHeight();
        gc.fillOval(cx, cy, w, h);
        gc.setFill(Color.GREEN);
    }*/

    @Override
    public Shape getBoundary() {
        // circle.getTransforms().add(new Rotate(this.angle, positionX, positionY));
        return new Circle(position.getX(), position.getY(), bounds.getWidth() / 4);
    }

    @Override
    public void onKill() {
        super.onKill();
        Globals.hudManager.addScore(getScore());
    }

    /**
     * Returns a set of all the child asteroids for when this asteroid splits.
     *
     * @return a set of all the child asteroids
     */
    public Set<Asteroid> getChildren() {
        Set<Asteroid> splitList = new HashSet<>();
        if (size.ordinal() <= 1) {
            return splitList;
        }

        int pieces = 2;
        Size newSize = Size.values()[size.ordinal() - 1];

        for (int i = 0; i < pieces; i++) {
            Asteroid asteroid = spawnChild(newSize);
            asteroid.setCollidable(false);
            splitList.add(asteroid);
        }
        return splitList;
    }

    /**
     * Gets the area where children can spawn.
     *
     * @return the spawn area
     */
    public Shape getSpawnArea() {
        return new Circle(position.getX(), position.getY(), bounds.getWidth() / 3);
    }

    /**
     * Spawn's an asteroid of the given size in a random location inside the asteroid's spawn area.
     *
     * @param size the size of the child
     * @return the asteroid that was spawned
     */
    private Asteroid spawnChild(Size size) {
        Shape shape = getSpawnArea();
        Asteroid child = new Asteroid(size);
        double radius = shape.getBoundsInParent().getWidth() / 2;
        double x = shape.getBoundsInParent().getCenterX();
        double y = shape.getBoundsInParent().getCenterY();

        double a = RNG.nextDouble();
        double b = RNG.nextDouble();
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

    /**
     * Gets the mass value of the given size using the density of an asteroid.
     *
     * @param size the size of the asteroid
     * @return the mass of the asteroid
     */
    private static double getMass(Size size) {
        return (1 + size.ordinal()) * 330;
    }

    /**
     * Gets the score value of this asteroid depending on its size.
     *
     * @return the score
     */
    private int getScore() {
        switch (size) {
        case SMALL:
            return 100;
        case MEDIUM:
            return 50;
        case LARGE:
            return 20;
        case XLARGE:
            return 10;
        default:
            System.out.println("Invalid size " + size);
            return 0;
        }
    }

}
