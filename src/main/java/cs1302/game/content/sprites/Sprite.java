package cs1302.game.content.sprites;

import cs1302.game.content.Globals;
import cs1302.game.content.animations.Explosion;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.Objects;
import java.util.Random;

/**
 * An abstract class representing a sprite in the game. Sprites have a position, bounds, velocity,
 * and an image representing their state in the game. Sprites can be drawn to the screen and updated
 * using on an external time delta.
 */
public abstract class Sprite {
    private Image image;
    public double minSpeed = 50;
    public double maxSpeed = 500;
    public static final Random RNG = new Random();

    protected boolean alive;

    Point2D position;
    Bounds bounds;
    Point3D velocity;

    double angle;
    double radAngle;

    /**
     * Instantiates a new Sprite.
     *
     * @param path the path
     * @param size the size
     */
    public Sprite(String path, Size size) {
        init();
        setImage(path);
        resize(size);
    }

    /**
     * Initializes the position, velocity and status of the sprite.
     */
    public void init() {
        alive = true;
        position = new Point2D(0, 0);
        velocity = new Point3D(0, 0, 0);
        angle = 0;
        bounds = new BoundingBox(0, 0, 0, 0);
    }

    /**
     * Sets image.
     *
     * @param filename the filename
     */
    public void setImage(String filename) {
        image = new Image(filename);
        bounds = new BoundingBox(0, 0, image.getWidth(), image.getHeight());
    }

    /**
     * Sets the x and y values of the position vector.
     *
     * @param x the x
     * @param y the y
     */
    public void setPosition(double x, double y) {
        position = new Point2D(x, y);
    }

    /**
     * Sets the x, y, and rotational velocity of the 3D velocity vector.
     *
     * @param x the x velocity
     * @param y the y velocity
     * @param r the rotational velocity
     */
    public void setVelocity(double x, double y, double r) {
        velocity = new Point3D(x, y, r);
    }

    /**
     * Add values to the existing velocity vector.
     *
     * @param x the x velocity
     * @param y the y velocity
     * @param r the rotational velocity
     */
    public void addVelocity(double x, double y, double r) {
        velocity = velocity.add(x, y, r);
    }

    /**
     * Update the sprite given the given time delta.
     *
     * @param delta the elapsed time since the last update
     */
    public void update(double delta) {
        // setPosition(positionX + velocityX * delta, positionY + velocityY * delta);
        position = position.add(velocity.getX() * delta, velocity.getY() * delta);

        addRotation(velocity.getZ() * delta);

        wrapAround();
    }

    /**
     * Give the sprite a random velocity. Used by certain sprites for spawning or movement.
     */
    public void randomVelocity() {
        int negX = RNG.nextBoolean() ? -1 : 1;
        double vx = RNG.nextDouble() * (maxSpeed - minSpeed) + minSpeed;
        int negY = RNG.nextBoolean() ? -1 : 1;
        double vy = RNG.nextDouble() * (maxSpeed - minSpeed) + minSpeed;
        setVelocity(vx * negX, vy * negY, 0);
    }

    /**
     * Render the sprite to the canvas using the given graphics context.
     *
     * @param gc the graphics context of the canvas
     */
    public void render(GraphicsContext gc) {
        gc.save();
        gc.translate(position.getX(), position.getY());
        gc.rotate(angle);
        gc.translate(-(bounds.getWidth() / 2), -(bounds.getHeight() / 2));
        gc.drawImage(image, 0, 0);
        gc.restore();
    }

    /*protected void drawBoundary(GraphicsContext gc, Color color) {
        gc.setFill(color);
        Shape collisionShape = getBoundary();
        double cx = collisionShape.getBoundsInParent().getMinX();
        double cy = collisionShape.getBoundsInParent().getMinY();
        double w = collisionShape.getBoundsInParent().getWidth();
        double h = collisionShape.getBoundsInParent().getHeight();
        gc.fillOval(cx, cy, w, h);
        gc.setFill(Color.GREEN);
    }*/

    /**
     * Returns the collision shape of the sprite.
     *
     * @return the collision shape
     */
    public Shape getBoundary() {
        final double w = bounds.getWidth();
        final double h = bounds.getHeight();
        return new Rectangle(position.getX() - w / 2, position.getY() - h / 2, w, h);
        // return new Rectangle2D(positionX, positionY, width, height);
    }

    /**
     * Returns whether the sprite is currently colliding with the given sprite.
     *
     * @param s the sprite to check against
     * @return true if the sprites are colliding
     */
    public boolean intersects(Sprite s) {
        return Shape.intersect(getBoundary(), s.getBoundary()).getBoundsInLocal().getWidth() != -1;
        // return s.getBoundary().intersects( this.getBoundary() );
    }

    /**
     * Resize the current sprite's image. Current image cannot be null.
     *
     * @param size the new size of the image
     */
    public void resize(Size size) {
        image = new Image(image.getUrl(), size.width, size.height, true, true);
        bounds = new BoundingBox(0, 0, image.getWidth(), image.getHeight());
    }

    /**
     * Sets the rotation angle of the sprite.
     *
     * @param angle the angle in degrees
     */
    public void setRotation(double angle) {
        this.angle = angle;
        this.radAngle = Math.toRadians(angle);
    }

    /**
     * Add rotation to the sprite's current rotation.
     *
     * @param angle the angle in degrees
     */
    public void addRotation(double angle) {
        setRotation(this.angle + angle);
    }

    /**
     * Kill the current sprite, setting the alive status to false and triggering the onKill event.
     */
    public void kill() {
        onKill();
        alive = false;
    }

    /**
     * Places the sprite at a random position on a random edge of the screen.
     */
    public void randomPositionOnEdge() {
        switch (RNG.nextInt(4)) {
        // Top
        case 0:
            int x = RNG.nextInt(Globals.WIDTH);
            setPosition(x, 0);
            break;
        // Right
        case 1:
            int y = RNG.nextInt(Globals.HEIGHT);
            setPosition(Globals.WIDTH, y);
            break;
        // Bottom
        case 2:
            x = RNG.nextInt(Globals.WIDTH);
            setPosition(x, Globals.HEIGHT);
            break;
        // Left
        case 3:
            y = RNG.nextInt(Globals.HEIGHT);
            setPosition(0, y);
            break;
        }
    }

    /**
     * Returns whether the sprite is currently alive.
     *
     * @return true if the sprite is alive
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Called when the sprite is killed.
     */
    protected abstract void onKill();

    /**
     * Responsible for updating the sprite's position when it goes off screen, wrapping it around
     * to the other side.
     */
    private void wrapAround() {
        double centerX = bounds.getCenterX() + position.getX();
        double centerY = bounds.getCenterY() + position.getY();
        if (centerX > Globals.WIDTH + bounds.getWidth()) {
            setPosition(-(bounds.getWidth() / 2), position.getY());
        } else if (centerX < 0) {
            setPosition(Globals.WIDTH + (bounds.getWidth() / 2), position.getY());
        } else if (centerY > Globals.HEIGHT + bounds.getHeight()) {
            setPosition(position.getX(), -(bounds.getHeight() / 2));
        } else if (centerY < 0) {
            setPosition(position.getX(), Globals.HEIGHT + (bounds.getHeight() / 2));
        }
    }

    /**
     * Create a new explosion at the sprite's position with the appropriate size.
     */
    public void explode() {
        // Create a new explosion
        new Explosion((int) position.getX(), (int) position.getY(),
                (float) bounds.getWidth() / 2.0f);
    }

    /**
     * The Size enum for resizing sprite images to constant sizes.
     */
    public enum Size {
        TINY(25, 25),
        SMALL(50, 50),
        MEDIUM(75, 75),
        LARGE(100, 100),
        XLARGE(150, 150),
        HUGE(200, 200);

        private final int width;
        private final int height;

        /**
         * Instantiates a new Size.
         *
         * @param width  the width
         * @param height the height
         */
        Size(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, velocity);
    }

}
