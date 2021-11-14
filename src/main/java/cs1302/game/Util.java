package cs1302.game;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

import java.util.Random;

public class Util {

    private static final Random rng = new Random();

    public static Point2D randomPointInCircle(Circle circle) {
        double circleX = circle.getCenterX();
        double circleY = circle.getCenterY();
        double maxX = circleX + circle.getRadius();
        double maxY = circleY + circle.getRadius();
        Point2D point = new Point2D(circleX, circleY);
        // Randomize coords
        double xOffset = rng.nextInt((int) circle.getRadius());
        double yOffset = rng.nextInt((int) circle.getRadius());
        point.subtract(rng.nextBoolean() ? circleX + xOffset : circleX - xOffset, rng.nextBoolean() ? circleY + yOffset : circleY - yOffset);
        return point;
    }

    public static Point2D randomVelocity(double minX, double maxX, double minY, double maxY) {
        double x = rng.nextDouble() * (maxX - minX) + minX;
        double y = rng.nextDouble() * (maxY - minY) + minY;
        return new Point2D(x, y);
    }

}
