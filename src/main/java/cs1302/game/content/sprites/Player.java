package cs1302.game.content.sprites;

import cs1302.game.content.Globals;
import cs1302.game.content.managers.SoundManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class Player extends PhysicSprite {

    private double hAcceleration;
    private double vAcceleration;

    // FIXME need cooldown manager tied to delta
    private static final int SHOOT_COOLDOWN = 75;
    private int currentShootCooldown = 0;

    private static final int DAMAGE_COOLDOWN = 250;
    private int currentDamageCooldown = 0;
    private int health = 3;

    private static final int HYPERSPACE_COOLDOWN = 10;
    double hyperspaceTimer;

    public Player() {
        super("file:resources/sprites/spaceship.png", Size.SMALL, 300);
        init();
    }

    public void init() {
        hAcceleration = 500;
        vAcceleration = 500;

        Globals.bulletManager.bindFunction(this, bullet -> {
            if (bullet.getParent() != this && bullet.intersects(this)) {
                bullet.kill();
                damage(1);
            }
        });
    }

    public void turnRight(double delta) {
        addVelocity(0, 0, 1000 * delta);
    }

    public void turnLeft(double delta) {
        addVelocity(0, 0, -1000 * delta);
    }

    public void accelerate(double delta) {
        setVelocity(velocity.getX() + hAcceleration * Math.sin(radAngle) * delta,
                velocity.getY() - vAcceleration * Math.cos(radAngle) * delta, velocity.getZ());
    }

    public void decelerate(double delta) {
        setVelocity(velocity.getX() - hAcceleration * Math.sin(radAngle) * delta,
                velocity.getY() + vAcceleration * Math.cos(radAngle) * delta, velocity.getZ());
    }

    public void brake(double delta, double force) {
        force *= delta;
        double velocityX = velocity.getX() * (1 - force / 100);
        double velocityY = velocity.getY() * (1 - force / 100);
        double velocityR = velocity.getZ() * 0.97;

        setVelocity(velocityX, velocityY, velocityR);
    }

    @Override
    public void update(double delta) {
        super.update(delta);
        if (currentShootCooldown > 0) {
            currentShootCooldown -= delta;
        }
        if (currentDamageCooldown > 0) {
            currentDamageCooldown -= delta;
        }
        if (hyperspaceTimer > 0) {
            hyperspaceTimer -= delta;
        }
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
    }

    @Override
    public Shape getBoundary() {
        return new Circle(position.getX(), position.getY(), bounds.getWidth() / 2);
    }

    @Override
    public void onKill() {
        super.onKill();
        Globals.hudManager.setGameOver(true);
    }

    public void hyperspace() {
        if (hasHyperspace()) {
            hyperspaceTimer = HYPERSPACE_COOLDOWN;
            Globals.soundManager.playSound(SoundManager.Sounds.HYPERSPACE);
            setPosition(rng.nextDouble() * Globals.WIDTH, rng.nextDouble() * Globals.HEIGHT);
        }
    }

    public void shoot() {
        if (currentShootCooldown <= 0) {
            currentShootCooldown = SHOOT_COOLDOWN;
            Globals.bulletManager.add(new Bullet(this, 300));
            Globals.soundManager.playSound(SoundManager.Sounds.LASER_SHOOT);
        }
    }

    public void damage(int damage) {
        if (currentDamageCooldown <= 0) {
            currentDamageCooldown = DAMAGE_COOLDOWN;
            health -= damage;
            Globals.soundManager.playSound(SoundManager.Sounds.SHIP_DAMAGE);
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

    public void reset() {
        health = 3;
        currentDamageCooldown = 0;
        currentShootCooldown = 0;
        hyperspaceTimer = 0;
        setPosition(Globals.WIDTH / 2.0, Globals.HEIGHT / 2.0);
        setVelocity(0, 0, 0);
        alive = true;
        init();
    }

    public boolean hasHyperspace() {
        return hyperspaceTimer <= 0;
    }

    public void addHealth(int i) {
        health += i;
    }

}
