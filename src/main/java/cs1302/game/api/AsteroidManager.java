package cs1302.game.api;

import cs1302.game.Game;
import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

public class AsteroidManager extends SpriteManager {

    private Player player;

    private boolean playerDead;

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
        boolean playerDead = false;
        for (Sprite sprite : sprites) {
            Asteroid asteroid = (Asteroid) sprite;

            asteroid.update(delta);
            if (asteroid.intersects(player)) {
                playerDead = true;
                asteroid.collided = true;
            } else {
                asteroid.collided = false;
            }

            sprites.stream().filter(other -> other instanceof PhysicSprite && other != sprite && asteroid.intersects(other)).forEach(other -> asteroid.collide((PhysicSprite) other));
        }
        this.playerDead = playerDead;
        sprites.removeIf(sprite -> !sprite.isAlive());
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
            Sprite.Size size = Sprite.Size.values()[rng.nextInt(Sprite.Size.values().length)];

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
        double vx = (rng.nextDouble() * 2 - 1) * Asteroid.MAX_VELOCITY;
        double vy = (rng.nextDouble() * 2 - 1) * Asteroid.MAX_VELOCITY;

        // Generate a random rotation for the asteroid
        double rotation = rng.nextDouble() * 360;

        // Generate a random rotation velocity for the asteroid
        double vr = (rng.nextDouble() * 2 - 1) * Asteroid.MAX_ROTATION_VELOCITY;

        asteroid.setVelocity(vx, vy, vr);
        asteroid.setRotation(rotation);

        // Ensure the bullet despawns if it collides with the asteroid
        player.getBulletManager().bindFunction(asteroid, bullet -> {
            if (bullet.intersects(asteroid)) {
                for (Asteroid child: asteroid.getChildren()) {
                    initializeAsteroid(child, rng);
                    addSprite(child);
                }
                asteroid.kill();
                bullet.kill();
            }
        });
    }

}
