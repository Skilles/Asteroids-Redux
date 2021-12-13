package cs1302.game.content;

import cs1302.game.Asteroids;
import cs1302.game.content.managers.*;

/**
 * Stores global variables to be accessed by all classes.
 */
public final class Globals {
    
    // The width of the game window
    public static int WIDTH = 1280;
    
    // The height of the game window
    public static int HEIGHT = 720;
    
    // The game instance
    public static Asteroids game;

    // The different managers
    public static AnimationManager animationManager;
    public static AsteroidManager asteroidManager;
    public static BulletManager bulletManager;
    public static HUDManager hudManager;
    public static EnemyManager enemyManager;
    public static SoundManager soundManager;

    /* Final method setters */

    /**
     * Sets the game.
     *
     * @param game the game
     */
    public static void setGame(Asteroids game) {
        checkDuplicate(Globals.game, "Game");
        Globals.game = game;
    }

    /**
     * Sets animation manager.
     *
     * @param animationManager the animation manager
     */
    public static void setAnimationManager(AnimationManager animationManager) {
        checkDuplicate(Globals.animationManager, "AnimationManager");
        Globals.animationManager = animationManager;
    }

    /**
     * Sets asteroid manager.
     *
     * @param asteroidManager the asteroid manager
     */
    public static void setAsteroidManager(AsteroidManager asteroidManager) {
        checkDuplicate(Globals.asteroidManager, "AsteroidManager");
        Globals.asteroidManager = asteroidManager;
    }

    /**
     * Sets bullet manager.
     *
     * @param bulletManager the bullet manager
     */
    public static void setBulletManager(BulletManager bulletManager) {
        checkDuplicate(Globals.bulletManager, "BulletManager");
        Globals.bulletManager = bulletManager;
    }

    /**
     * Sets hud manager.
     *
     * @param hudManager the hud manager
     */
    public static void setHudManager(HUDManager hudManager) {
        checkDuplicate(Globals.hudManager, "HUDManager");
        Globals.hudManager = hudManager;
    }

    /**
     * Sets enemy manager.
     *
     * @param enemyManager the enemy manager
     */
    public static void setEnemyManager(EnemyManager enemyManager) {
        checkDuplicate(Globals.enemyManager, "EnemyManager");
        Globals.enemyManager = enemyManager;
    }

    /**
     * Sets sound manager.
     *
     * @param soundManager the sound manager
     */
    public static void setSoundManager(SoundManager soundManager) {
        checkDuplicate(Globals.soundManager, "SoundManager");
        Globals.soundManager = soundManager;
    }

    /* Variable method setters */

    /**
     * Sets width of the game window.
     *
     * @param width the width of the game window
     */
    public static void setWidth(int width) {
        Globals.WIDTH = width;
    }

    /**
     * Sets height of the game window.
     *
     * @param height the height of the game window
     */
    public static void setHeight(int height) {
        Globals.HEIGHT = height;
    }

    /* Helper methods */

    /**
     * Check's whether a variable has already been set.
     *
     * @param object the object variable
     * @param name   the name of the variable
     */
    private static void checkDuplicate(Object object, String name) {
        if (object != null) {
            throw new IllegalStateException(name + " already set!");
        }
    }

}
