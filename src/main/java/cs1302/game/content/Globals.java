package cs1302.game.content;

import cs1302.game.Asteroids;
import cs1302.game.content.managers.AnimationManager;
import cs1302.game.content.managers.AsteroidManager;
import cs1302.game.content.managers.BulletManager;
import cs1302.game.content.managers.HUDManager;

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

    /* Final method setters */

    public static void setGame(Asteroids game) {
        checkDuplicate(Globals.game, "Game");
        Globals.game = game;
    }

    public static void setAnimationManager(AnimationManager animationManager) {
        checkDuplicate(Globals.animationManager, "AnimationManager");
        Globals.animationManager = animationManager;
    }

    public static void setAsteroidManager(AsteroidManager asteroidManager) {
        checkDuplicate(Globals.asteroidManager, "AsteroidManager");
        Globals.asteroidManager = asteroidManager;
    }

    public static void setBulletManager(BulletManager bulletManager) {
        checkDuplicate(Globals.bulletManager, "BulletManager");
        Globals.bulletManager = bulletManager;
    }

    public static void setHudManager(HUDManager hudManager) {
        checkDuplicate(Globals.hudManager, "HUDManager");
        Globals.hudManager = hudManager;
    }

    /* Variable method setters */
    
    public static void setWidth(int width) {
        Globals.WIDTH = width;
    }
    
    public static void setHeight(int height) {
        Globals.HEIGHT = height;
    }

    /* Helper methods */

    private static void checkDuplicate(Object object, String name) {
        if (object != null) {
            throw new IllegalStateException(name + " already set!");
        }
    }

}