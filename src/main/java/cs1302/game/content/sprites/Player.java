package cs1302.game.content.sprites;

import cs1302.game.content.BulletManager;
import cs1302.game.content.HUDManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class Player extends Sprite {

    private final double hAcceleration;
    private final double vAcceleration;

    private final BulletManager bulletManager;
    private HUDManager hudManager;

    // FIXME need cooldown manager tied to delta
    private final int COOLDOWN = 70;
    private int currentCooldown = 0;
    private int health = 5;

    public Player() {
        super("file:resources/sprites/spaceship.png", Size.SMALL, 300);
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
        // drawBoundary(gc, Color.AQUA);
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

    public void setHudManager(HUDManager hudManager) {
        if (this.hudManager != null) {
            throw new RuntimeException("HUD Manager already set!");
        }
        this.hudManager = hudManager;
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

    public HUDManager getHudManager() {
        return hudManager;
    }

}
