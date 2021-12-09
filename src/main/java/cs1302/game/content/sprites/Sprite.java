package cs1302.game.content.sprites;

import cs1302.game.content.Globals;
import cs1302.game.content.animations.Explosion;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.Objects;
import java.util.Random;

public abstract class Sprite {
    private Image image;
    public double MIN_SPEED = 50;
    public double MAX_SPEED = 500;
    public static final Random rng = new Random();

    protected boolean alive;

    Point2D position;
    Bounds bounds;
    Point3D velocity;

    double angle;
    double radAngle;

    public Sprite() {
        alive = true;
        position = new Point2D(0, 0);
        velocity = new Point3D(0, 0, 0);
        angle = 0;
        bounds = new BoundingBox(0, 0, 0, 0);
    }

    public Sprite(String path, Size size) {
        this();
        setImage(path);
        resize(size);
    }

    public void setImage(Image i) {
        image = i;
        bounds = new BoundingBox(0, 0, i.getWidth(), i.getHeight());
    }

    public void setImage(String filename) {
        Image i = new Image(filename);
        setImage(i);
    }

    public void setPosition(double x, double y) {
        position = new Point2D(x, y);
    }

    public void setVelocity(double x, double y, double r) {
        velocity = new Point3D(x, y, r);
    }

    public void addVelocity(double x, double y, double r) {
        velocity = velocity.add(x, y, r);
    }

    public void update(double delta) {
        // setPosition(positionX + velocityX * delta, positionY + velocityY * delta);
        position = position.add(velocity.getX() * delta, velocity.getY() * delta);

        addRotation(velocity.getZ() * delta);

        wrapAround();
    }

    public void randomVelocity() {
        int negX = rng.nextBoolean() ? -1 : 1;
        double vx = rng.nextDouble() * (MAX_SPEED - MIN_SPEED) + MIN_SPEED;
        int negY = rng.nextBoolean() ? -1 : 1;
        double vy = rng.nextDouble() * (MAX_SPEED - MIN_SPEED) + MIN_SPEED;
        setVelocity(vx * negX, vy * negY, 0);
    }

    public void render(GraphicsContext gc) {
        gc.save();
        gc.translate(position.getX(), position.getY());
        gc.rotate(angle);
        gc.translate(-(bounds.getWidth() / 2), -(bounds.getHeight() / 2));
        gc.drawImage(image, 0, 0);
        gc.restore();
    }

    protected void drawBoundary(GraphicsContext gc, Color color) {
        gc.setFill(color);
        Shape collisionShape = getBoundary();
        double cx = collisionShape.getBoundsInParent().getMinX();
        double cy = collisionShape.getBoundsInParent().getMinY();
        double w = collisionShape.getBoundsInParent().getWidth();
        double h = collisionShape.getBoundsInParent().getHeight();
        gc.fillOval(cx, cy, w, h);
        gc.setFill(Color.GREEN);
    }

    public Shape getBoundary() {
        final double w = bounds.getWidth();
        final double h = bounds.getHeight();
        return new Rectangle(position.getX() - w / 2, position.getY() - h / 2, w, h);
        // return new Rectangle2D(positionX, positionY, width, height);
    }

    public boolean intersects(Sprite s) {
        return Shape.intersect(getBoundary(), s.getBoundary()).getBoundsInLocal().getWidth() != -1;
        // return s.getBoundary().intersects( this.getBoundary() );
    }

    public void resize(Size size) {
        image = new Image(image.getUrl(), size.width, size.height, true, true);
        bounds = new BoundingBox(0, 0, image.getWidth(), image.getHeight());
    }

    public void setRotation(double angle) {
        this.angle = angle;
        this.radAngle = Math.toRadians(angle);
    }

    public void addRotation(double angle) {
        setRotation(this.angle + angle);
    }

    public void kill() {
        onKill();
        alive = false;
    }

    public void randomPositionOnEdge() {
        switch (rng.nextInt(4)) {
            // Top
            case 0:
                int x = rng.nextInt(Globals.WIDTH);
                setPosition(x, 0);
                break;
            // Right
            case 1:
                int y = rng.nextInt(Globals.HEIGHT);
                setPosition(Globals.WIDTH, y);
                break;
            // Bottom
            case 2:
                x = rng.nextInt(Globals.WIDTH);
                setPosition(x, Globals.HEIGHT);
                break;
            // Left
            case 3:
                y = rng.nextInt(Globals.HEIGHT);
                setPosition(0, y);
                break;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    protected void onOffScreen() {

    }

    protected abstract void onKill();

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

    public void explode() {
        // Create a new explosion
        new Explosion((int) position.getX(), (int) position.getY(), (float) bounds.getWidth() / 2.0f);
    }

    public enum Size {
        TINY(25, 25),
        SMALL(50, 50),
        MEDIUM(75, 75),
        LARGE(100, 100),
        XLARGE(150, 150),
        HUGE(200, 200);

        private final int width;
        private final int height;

        Size(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, velocity);
    }

}
