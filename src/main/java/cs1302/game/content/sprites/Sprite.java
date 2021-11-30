package cs1302.game.content.sprites;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.Objects;

public abstract class Sprite {
    private Image image;
    double positionX;
    double centerX;
    double positionY;
    double centerY;
    double velocityX;
    double velocityY;
    double velocityR;
    double width;
    double height;

    private boolean alive;

    static Bounds bounds;

    final double maxWidth = 1280;
    final double maxHeight = 720;

    double angle;
    double radAngle;

    public Sprite() {
        positionX = 0;
        positionY = 0;
        velocityX = 0;
        velocityY = 0;
        alive = true;
    }

    public Sprite(String path, Size size) {
        this();
        setImage(path);
        resize(size);
    }

    public void setImage(Image i) {
        image = i;
        width = i.getWidth();
        height = i.getHeight();
    }

    public void setImage(String filename) {
        Image i = new Image(filename);
        setImage(i);
    }

    public void setPosition(double x, double y) {
        positionX = x;
        positionY = y;
    }

    public void setVelocity(double x, double y, double r) {
        velocityX = x;
        velocityY = y;
        velocityR = r;
    }

    public void addVelocity(double x, double y) {
        velocityX += x;
        velocityY += y;
    }

    public void update(double time) {
        positionX += velocityX * time;
        positionY += velocityY * time;

        centerX = positionX + width / 2;
        centerY = positionY + height / 2;

        setRotation(angle + velocityR);

        if (wrapAround()) {
            onOffScreen();
        }
    }

    public void render(GraphicsContext gc) {
        gc.save();
        gc.translate(positionX, positionY);
        gc.rotate(angle);
        gc.translate(-(width / 2), -(height / 2));
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
        return new Rectangle(positionX - width / 2, positionY - height / 2, width, height);
        // return new Rectangle2D(positionX, positionY, width, height);
    }

    public boolean intersects(Sprite s) {
        return Shape.intersect(getBoundary(), s.getBoundary()).getBoundsInLocal().getWidth() != -1;
        // return s.getBoundary().intersects( this.getBoundary() );
    }

    public String toString() {
        return " Position: [" + positionX + "," + positionY + "]"
                + " Velocity: [" + velocityX + "," + velocityY + "]"
                + " Rotation: [" + angle +  "]";
    }

    public void resize(Size size) {
        image = new Image(image.getUrl(), size.width, size.height, true, true);
        width = image.getWidth();
        height = image.getHeight();
    }

    public void setRotation(double angle) {
        this.angle = angle;
        this.radAngle = Math.toRadians(angle);
    }

    public void addRotation(double angle) {
        setRotation(this.angle + angle);
    }

    public void addRotationVelocity(double velocity) {
        velocityR += velocity;
    }

    public void kill() {
        onKill();
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    protected abstract void onOffScreen();

    protected abstract void onKill();

    protected boolean wrapAround() {
        if (centerX > maxWidth + width) {
            positionX = -(width / 2);
        } else if (centerX < 0) {
            positionX = maxWidth + width / 2;
        } else if (centerY > maxHeight + height) {
            positionY = -(height / 2);
        } else if (centerY < 0) {
            positionY = maxHeight + height / 2;
        } else {
            return false;
        }
        return true;
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

        public int getScore() {
            return (int) (Math.pow(1 + values().length - ordinal(), 2) * 100);
        }
    }

    public static void setBounds(Bounds bounds) {
        Sprite.bounds = bounds;
    }

    @Override
    public int hashCode() {
        return Objects.hash(centerX, centerY, angle);
    }

}
