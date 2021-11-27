package cs1302.game.api;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Player extends Sprite {

    private final double hAcceleration;
    private final double vAcceleration;

    private BulletManager bulletManager;

    // FIXME need cooldown manager tied to delta
    private final int COOLDOWN = 70;
    private int currentCooldown = 0;

    public Player() {
        super("file:resources/sprites/spaceship.png", Size.SMALL);
        hAcceleration = 500;
        vAcceleration = 500;
        bulletManager = new BulletManager();
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

        bulletManager.update(time);
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
        drawBoundary(gc, Color.AQUA);
        bulletManager.render(gc);
    }

    @Override
    public Shape getBoundary() {
        Circle circle = new Circle(positionX, positionY, width / 2);
        // rectangle.getTransforms().add(new Rotate(this.angle, positionX, positionY));
        // rectangle.setRotate(this.angle);
        return circle;
    }

    @Override
    protected void onOffScreen() {

    }

    @Override
    public void onKill() {

    }

    public void shoot(double delta) {
        if (currentCooldown == 0) {
            currentCooldown = COOLDOWN;
            bulletManager.add(new Bullet(this, 300));
        }
    }

    public BulletManager getBulletManager() {
        return bulletManager;
    }

    static class BulletManager {

        private final List<Bullet> bulletList;

        private final List<Pair<Sprite, Consumer<Bullet>>> consumers;

        public BulletManager() {
            bulletList = new ArrayList<>();
            consumers = new ArrayList<>();
        }

        public void update(double delta) {
            bulletList.removeIf(bullet -> !bullet.isAlive());
            consumers.removeIf(pair -> !pair.getKey().isAlive());

            for (Bullet bullet : bulletList) {
                bullet.update(delta);
                for (int i = 0; i < consumers.size(); i++) {
                    Pair<Sprite, Consumer<Bullet>> consumerPair = consumers.get(i);
                    consumerPair.getValue().accept(bullet);
                }
            }
        }

        public void render(GraphicsContext gc) {
            for (Bullet bullet : bulletList) {
                bullet.render(gc);
            }
        }

        public void add(Bullet bullet) {
            bulletList.add(bullet);
        }

        public void remove(Bullet bullet) {
            bulletList.remove(bullet);
        }

        public void removeAll() {
            bulletList.clear();
        }

        public List<Bullet> getBullets() {
            return bulletList;
        }

        public void bindFunction(Sprite sprite, Consumer<Bullet> consumer) {
            consumers.add(new Pair<>(sprite, consumer));
        }

        public void removeConsumer(Sprite sprite) {
            consumers.removeIf(pair -> pair.getKey() == sprite);
        }
    }

}
