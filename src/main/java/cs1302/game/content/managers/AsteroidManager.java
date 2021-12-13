package cs1302.game.content.managers;

import cs1302.game.content.Globals;
import cs1302.game.content.sprites.Asteroid;
import cs1302.game.content.sprites.PhysicSprite;
import cs1302.game.content.sprites.Player;
import cs1302.game.content.sprites.Sprite;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Responsible for manipulating the asteroids in the game.
 */
public class AsteroidManager extends SpriteManager {

    private final Player player;

    public static int NUM_ASTEROIDS = 8;

    /**
     * Instantiates a new Asteroid manager.
     *
     * @param player the player
     */
    public AsteroidManager(Player player) {
        super();
        this.player = player;
    }

    @Override
    protected void init() {
        Globals.setAsteroidManager(this);
    }

    @Override
    public void update(double delta) {
        sprites.removeIf(sprite -> !sprite.isAlive());
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
        if (sprites.isEmpty()) {
            NUM_ASTEROIDS++;
            generateAsteroids();
        }
    }

    /**
     * Check whether the given asteroid needs to be collided with another asteroid and does so.
     *
     * @param asteroid the asteroid to collide
     */
    private void collideAsteroid(Asteroid asteroid) {
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

    }

    /**
     * Generates a certain amount of asteroids randomly throughout the screen.
     */
    public void generateAsteroids() {
        for (int i = 0; i < NUM_ASTEROIDS; i++) {
            // Create the asteroid
            Asteroid asteroid = new Asteroid(Sprite.Size.XLARGE);
            asteroid.randomPositionOnEdge();

            // Add the asteroid to the list of sprites
            spawnAsteroid(asteroid);
        }

    }

    /**
     * Spawn an asteroid.
     *
     * @param asteroid the asteroid
     */
    public void spawnAsteroid(Asteroid asteroid) {
        sprites.add(asteroid);
        initializeAsteroid(asteroid);
    }

    /**
     * Initialize an asteroid by linking it to the bullet manager.
     *
     * @param asteroid the asteroid
     */
    private void initializeAsteroid(Asteroid asteroid) {
        // Bullet on asteroid collision
        Globals.bulletManager.bindFunction(asteroid, bullet -> {
            // Need to check if the bullet is alive twice
            if (bullet.isAlive() && bullet.getParent() instanceof Player) {
                for (Asteroid child: asteroid.getChildren()) {
                    spawnAsteroid(child);
                }
                asteroid.kill();
                bullet.kill();
            }
        });
    }

    /**
     * Reset the manager.
     */
    public void reset() {
        sprites.clear();
        generateAsteroids();
    }

}
