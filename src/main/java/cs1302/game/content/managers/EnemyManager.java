package cs1302.game.content.managers;

import cs1302.game.content.Globals;
import cs1302.game.content.sprites.Enemy;
import cs1302.game.content.sprites.Player;
import javafx.scene.canvas.GraphicsContext;

/**
 * A special sprite manager solely for the two enemies in the game. Handles their spawning behavior
 * and difficulty.
 */
public class EnemyManager extends Manager {

    private Enemy smallEnemy;
    private Enemy largeEnemy;

    private double smallTimer;
    private double largeTimer;

    /**
     * Instantiates a new Enemy manager.
     */
    public EnemyManager() {
        super();
        Globals.setEnemyManager(this);
    }

    @Override
    protected void init() {
        smallEnemy = new Enemy(Enemy.Type.SMALL);
        largeEnemy = new Enemy(Enemy.Type.LARGE);
        smallEnemy.setAlive(false);
        largeEnemy.setAlive(false);

        Globals.bulletManager.bindFunction(smallEnemy, (bullet) -> {
            if (bullet.getParent() instanceof Player) {
                smallEnemy.kill();
                bullet.kill();
            }
        });
        Globals.bulletManager.bindFunction(largeEnemy, (bullet) -> {
            if (bullet.getParent() instanceof Player) {
                largeEnemy.kill();
                bullet.kill();
            }
        });
    }

    /**
     * Update the enemies with the elapsed time delta.
     *
     * @param delta the delta
     */
    public void update(double delta) {
        if (smallEnemy.isAlive()) {
            smallEnemy.update(delta);
        } else {
            smallTimer += delta;
        }
        if (largeEnemy.isAlive()) {
            largeEnemy.update(delta);
        } else {
            largeTimer += delta;
        }

        if ((Globals.hudManager.getScore() >= 100 && smallTimer >= 30)
                || (Globals.hudManager.getScore() >= 10000 && smallTimer >= 7)) {
            spawnSmall();
            smallTimer = 0;
        }

        if (largeTimer >= 15 && Globals.hudManager.getScore() < 40000) {
            spawnLarge();
            largeTimer = 0;
        }

        if (Globals.hudManager.getScore() % 5000 == 0 && Globals.hudManager.getScore() != 0) {
            smallEnemy.setAngleOffset(smallEnemy.getAngleOffset() - 5);
        }
    }

    /**
     * Spawn the small enemy.
     */
    private void spawnSmall() {
        spawnEnemy(smallEnemy);
    }

    /**
     * Spawn the large enemy.
     */
    private void spawnLarge() {
        spawnEnemy(largeEnemy);
    }

    /**
     * Spawn the specified enemy.
     *
     * @param enemy the enemy to spawn
     */
    private void spawnEnemy(Enemy enemy) {
        // Set the enemy's position to a random location at a random edge on the screen
        enemy.randomPositionOnEdge();
        // Set a random velocity
        enemy.randomVelocity();
        // Set the enemy to alive
        enemy.setAlive(true);
    }

    @Override
    public void render(GraphicsContext ctx) {
        if (smallEnemy.isAlive()) {
            smallEnemy.render(ctx);
        }
        if (largeEnemy.isAlive()) {
            largeEnemy.render(ctx);
        }
    }

    /**
     * Reset.
     */
    public void reset() {
        smallEnemy.setAlive(false);
        largeEnemy.setAlive(false);
        smallTimer = 0;
        largeTimer = 0;
        init();
    }

}
