package cs1302.game.api;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

public class Sprite
{
    private Image image;
    double positionX;
    double positionY;
    double velocityX;
    double velocityY;
    double width;
    double height;

    private float angle;
    private Rotate rotation;
    double radAngle;

    public Sprite() {
        positionX = 0;
        positionY = 0;
        velocityX = 0;
        velocityY = 0;
        rotation = new Rotate();
    }

    public Sprite(String path) {
        this();
        setImage(path);
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

    public void setVelocity(double x, double y) {
        velocityX = x;
        velocityY = y;
    }

    public void addVelocity(double x, double y) {
        velocityX += x;
        velocityY += y;
    }

    public void update(double time) {
        positionX += velocityX * time;
        positionY += velocityY * time;
    }

    public void render(GraphicsContext gc) {
        gc.save();
        /*applyRotation(gc);
        rotatePosition();
        gc.drawImage(image, positionX, positionY);*/
        gc.translate(positionX, positionY);
        gc.rotate(rotation.getAngle());
        gc.translate(-(width / 2), -(height / 2));
        gc.drawImage(image, 0, 0);
        gc.restore();
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, width, height);
    }

    public boolean intersects(Sprite s) {
        return s.getBoundary().intersects( this.getBoundary() );
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

    public void rotate(float angle) {
        rotation = new Rotate(angle, positionX + width / 2, positionY + height / 2);
        this.angle = angle;
        this.radAngle = Math.toRadians(angle);
    }

    private void rotatePosition() {
        double centerX = positionX + width / 2;
        double centerY = positionY + height / 2;

        double radAngle = Math.toRadians(Math.abs(rotation.getAngle() - angle));

        final double tempX = positionX;
        final double tempY = positionY;

        positionX = centerX + Math.cos(radAngle) * (tempX - centerX) - Math.sin(radAngle) * (tempY - centerY);
        positionY = centerY + Math.sin(radAngle) * (tempX - centerX) + Math.cos(radAngle) * (tempY - centerY);


        angle = (float) rotation.getAngle();
    }

    public void addRotation(float angle) {
        rotate(this.angle + angle);
    }

    private void applyRotation(GraphicsContext gc) {
        // gc.rotate(rotation.getAngle());
        gc.transform(new Affine(rotation));
    }

    public enum Size {
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
}
