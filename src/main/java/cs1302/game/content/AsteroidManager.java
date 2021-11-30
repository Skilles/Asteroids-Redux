package cs1302.game.content;

import cs1302.game.Game;
import cs1302.game.content.sprites.Asteroid;
import cs1302.game.content.sprites.PhysicSprite;
import cs1302.game.content.sprites.Player;
import cs1302.game.content.sprites.Sprite;
import javafx.scene.canvas.GraphicsContext;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class AsteroidManager extends SpriteManager {

    private Player player;

    public AsteroidManager(Player player) {
        super();
        this.player = player;
    }

    @Override
    public void drawSprites(GraphicsContext ctx) {
        for (Sprite sprite : sprites) {
            Asteroid asteroid = (Asteroid) sprite;

            asteroid.render(ctx);

        }
    }

    @Override
    public void updateSprites(double delta) {
        super.updateSprites(delta);
        for (Sprite sprite : sprites) {
            Asteroid asteroid = (Asteroid) sprite;

            asteroid.update(delta);
            if (asteroid.intersects(player)) {
                asteroid.collide(player);
                player.damage(1);
            }
            // Collide asteroids
            collideAsteroid(asteroid);
        }
        sprites.removeIf(sprite -> !sprite.isAlive());
    }

    private boolean collideAsteroid(Asteroid asteroid) {
        AtomicBoolean intersects = new AtomicBoolean(false);
        sprites.stream().filter(other -> {
            boolean collide = other instanceof PhysicSprite
                    && !other.equals(asteroid) && asteroid.intersects(other);

            if (collide) {
                intersects.set(true);
            }

            return collide;
        }).forEach(other -> asteroid.collide((PhysicSprite) other));

        // Asteroids start off as uncollidable to avoid colliding when they spawn
        final boolean intersect = intersects.get();
        if (!intersect) {
            asteroid.setCollidable(true);
        }

        return intersect;
    }

    /**
     * Generates a certain amount of asteroids randomly throughout the game.
     *
     * @param rng the random number generator
     * @param numAsteroids how many asteroids to generate
     */
    public void generateAsteroids(int numAsteroids, Random rng) {
        for (int i = 0; i < numAsteroids; i++) {
            // Generate a random position for the asteroid
            double x = rng.nextDouble() * Game.WIDTH;
            double y = rng.nextDouble() * Game.HEIGHT;

            // Generate a random size for the asteroid
            Sprite.Size size = Sprite.Size.values()[2 + rng.nextInt(Sprite.Size.values().length - 2)];

            // Create the asteroid
            Asteroid asteroid = new Asteroid(size);
            initializeAsteroid(asteroid, rng);
            asteroid.setPosition(x, y);

            // Add the asteroid to the list of sprites
            sprites.add(asteroid);


        }

    }

    private void initializeAsteroid(Asteroid asteroid, Random rng) {
        // Generate a random velocity for the asteroid
        double vx = (rng.nextDouble() * 2 - 1) * Asteroid.MAX_VELOCITY / (1 + Sprite.Size.values().length - asteroid.getSize().ordinal());
        double vy = (rng.nextDouble() * 2 - 1) * Asteroid.MAX_VELOCITY / (1 + Sprite.Size.values().length - asteroid.getSize().ordinal());

        // Generate a random rotation for the asteroid
        double rotation = rng.nextDouble() * 360;

        // Generate a random rotation velocity for the asteroid
        double vr = (rng.nextDouble() * 2 - 1) * Asteroid.MAX_ROTATION_VELOCITY;

        asteroid.setVelocity(vx, vy, vr);
        asteroid.setRotation(rotation);

        // Ensure the bullet despawns if it collides with the asteroid
        player.getBulletManager().bindFunction(asteroid, bullet -> {
            if (bullet.isAlive() && bullet.intersects(asteroid)) {
                for (Asteroid child: asteroid.getChildren()) {
                    initializeAsteroid(child, rng);
                    addSprite(child);
                }
                ((Player) bullet.getParent()).getHudManager().addScore(asteroid.getSize().getScore());
                asteroid.kill();
                bullet.kill();
            }
        });
    }

}
