package cs1302.game;

import cs1302.game.api.Asteroid;
import cs1302.game.api.Bullet;
import cs1302.game.api.Player;
import cs1302.game.api.Sprite;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

public class Asteroids extends Game {

    private Random rng;
    private Player player;
    private List<Asteroid> asteroidList;
    private List<Bullet> bulletList;

    public Asteroids(Stage stage, int width, int height) {
        super(stage, width, height);
    }

    @Override
    public void init(Stage stage) {
        super.init(stage);
        rng = new Random(123);
        ctx.setGlobalAlpha(0.6);
        player = new Player();
        player.setPosition(300, 300);

        bulletList = new ArrayList<>();

        asteroidList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Asteroid asteroid = new Asteroid(Sprite.Size.LARGE);
            asteroid.setPosition(rng.nextInt(width), rng.nextInt(height));
            asteroid.setVelocity((rng.nextBoolean() ? rng.nextDouble() : -rng.nextDouble()) * 100, (rng.nextBoolean() ? rng.nextDouble() : -rng.nextDouble()) * 100, rng.nextBoolean() ? rng.nextFloat() : -rng.nextFloat());
            asteroidList.add(asteroid);
        }

        ctx.setFill( Color.GREEN );
        ctx.setStroke( Color.BLACK );
        ctx.setLineWidth(1);
    }

    @Override
    public void update(long currentNanoTime) {
        super.update(currentNanoTime);

        handleControls(elapsedTime());

        player.brake(elapsedTime(), 200);
        player.update(elapsedTime());

        Iterator<Asteroid> iterator = asteroidList.iterator();
        Set<Asteroid> newAsteroids = null;
        while (iterator.hasNext()) {
            Asteroid asteroid = iterator.next();
            if (!asteroid.isAlive()) {
                iterator.remove();
                if (newAsteroids == null) {
                    newAsteroids = new HashSet<>();
                }
                if (!asteroid.getSize().equals(Sprite.Size.SMALL)) {
                    newAsteroids.addAll(asteroid.getChildren());
                }
                // asteroidList.remove(asteroid);
                continue;
            }
            asteroid.update(elapsedTime());
            if (player.intersects(asteroid)) {
                asteroid.collided = true;
                // pause();
            } else {
                asteroid.collided = false;
            }
            for (Asteroid anotherAsteroid : asteroidList) {
                if (asteroid.equals(anotherAsteroid)) continue;
                if (asteroid.intersects(anotherAsteroid)) {
                    asteroid.collide(anotherAsteroid);
                    break;
                }
            }
            Iterator<Bullet> iter = bulletList.iterator();
            while (iter.hasNext()) {
                Bullet bullet = iter.next();
                if (!bullet.isAlive()) {
                    iter.remove();
                    // bulletList.remove(bullet);
                    continue;
                }
                if (asteroid.intersects(bullet)) {
                    asteroid.kill();
                    bullet.kill();
                }
            }
        }
        if (newAsteroids != null) {
            asteroidList.addAll(newAsteroids);
        }
        for (Bullet bullet : bulletList) {
            bullet.update(elapsedTime());
        }
    }

    @Override
    public void render(long delta) {
        super.render(delta);
        render(player);
        for (Asteroid asteroid : asteroidList) {
            asteroid.render(ctx);
        }
        for (Bullet bullet : bulletList) {
            bullet.render(ctx);
        }
    }

    void render(Sprite... sprites) {
        for (Sprite sprite : sprites) {
            sprite.render(ctx);
        }
    }

    private void handleControls(double delta) {
        isKeyPressed(KeyCode.LEFT, () -> player.turnLeft(delta));
        isKeyPressed(KeyCode.RIGHT, () -> player.turnRight(delta));
        isKeyPressed(KeyCode.UP, () -> player.accelerate(delta));
        isKeyPressed(KeyCode.DOWN, () -> player.decelerate(delta));
        isKeyPressed(KeyCode.A, () -> player.turnLeft(delta));
        isKeyPressed(KeyCode.D, () -> player.turnRight(delta));
        isKeyPressed(KeyCode.W, () -> player.accelerate(delta));
        isKeyPressed(KeyCode.S, () -> player.decelerate(delta));

        isKeyPressed(KeyCode.SPACE, () -> player.shoot(bulletList, delta));
    }

}
