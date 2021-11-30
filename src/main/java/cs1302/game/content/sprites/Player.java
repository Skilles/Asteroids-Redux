package cs1302.game.content.sprites;

import cs1302.game.content.BulletManager;
import cs1302.game.content.HUDManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class Player extends PhysicSprite {

    private final double hAcceleration;
    private final double vAcceleration;

    private final BulletManager bulletManager;
    private HUDManager hudManager;

    // FIXME need cooldown manager tied to delta
    private static final int SHOOT_COOLDOWN = 50;
    private int currentShootCooldown = 0;

    private static final int MAX_HEALTH = 5;
    private static final int DAMAGE_COOLDOWN = 100;
    private int currentDamageCooldown = 0;
    private int health = 3;

    public Player() {
        super("file:resources/sprites/spaceship.png", Size.SMALL, 300);
        hAcceleration = 500;
        vAcceleration = 500;
        bulletManager = new BulletManager();
    }

    public void turnRight(double delta) {
        addRotation(150 * delta);
    }

    public void turnLeft(double delta) {
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
        force = force * delta;

        velocityX *= (1 - force / 100);
        velocityY *= (1 - force / 100);
        velocityR *= 0.95 * delta;
    }

    @Override
    public void update(double time) {
        super.update(time);
        if (currentShootCooldown > 0) {
            currentShootCooldown--;
        }
        if (currentDamageCooldown > 0) {
            currentDamageCooldown--;
        }

        bulletManager.update(time);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (currentDamageCooldown > 0) {
            drawDamage(gc);
            super.render(gc);
            gc.setEffect(null);
        } else {
            super.render(gc);
        }

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
        hudManager.setGameOver(true);
    }

    public void setHudManager(HUDManager hudManager) {
        if (this.hudManager != null) {
            throw new RuntimeException("HUD Manager already set!");
        }
        this.hudManager = hudManager;
    }

    public void shoot(double delta) {
        if (currentShootCooldown == 0) {
            currentShootCooldown = SHOOT_COOLDOWN;
            bulletManager.add(new Bullet(this, 300));
        }
    }

    public BulletManager getBulletManager() {
        return bulletManager;
    }

    public HUDManager getHudManager() {
        return hudManager;
    }

    public void damage(int damage) {
        if (currentDamageCooldown == 0) {
            currentDamageCooldown = DAMAGE_COOLDOWN;
            health -= damage;
            if (health <= 0) {
                kill();
            }
        }
    }

    private void drawDamage(GraphicsContext gc) {
        ColorAdjust tint = new ColorAdjust();
        tint.setHue(0.8);
        gc.setEffect(tint);
    }

    public int getHealth() {
        return health;
    }

}
