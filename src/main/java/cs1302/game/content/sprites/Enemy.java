package cs1302.game.content.sprites;

import cs1302.game.content.Globals;
import cs1302.game.content.managers.SoundManager;
import javafx.scene.canvas.GraphicsContext;

/**
 * An enemy sprite that has two types. The large enemy shoots a bullet randomly while the small
 * enemy shoots a bullet at the player with an increasing accuracy.
 */
public class Enemy extends Sprite {

    private static int moveTime = 3;

    private final Type type;

    private int angleOffset = 30;

    /**
     * Instantiates a new Enemy.
     *
     * @param type the type
     */
    public Enemy(Type type) {
        super(type.path, type.size);
        minSpeed = 50;
        maxSpeed = 100;
        // Set a random velocity
        randomVelocity();
        this.type = type;
    }

    double timer;

    @Override
    public void update(double delta) {
        super.update(delta);
        timer += delta;
        if (timer > moveTime) {
            timer = 0;
            if (RNG.nextBoolean()) {
                randomVelocity();
            }
            shoot();
        }
    }

    /**
     * Shoot a bullet with different behavior depending on the type of enemy.
     */
    private void shoot() {
        if (type == Type.SMALL) {
            // shoots at the player in a 60 degree cone
            Player player = Globals.game.getPlayer();
            double angle = Math.atan2(player.position.getY() - position.getY(),
                    player.position.getX() - position.getX()) * (180 / Math.PI);
            if (angleOffset >= 0) {
                int neg = RNG.nextBoolean() ? -1 : 1;
                angle += neg * RNG.nextInt(angleOffset + 1);
            }
            setRotation(angle + 90);
            Globals.bulletManager.add(new Bullet(this, 300));
        } else {
            // shoots randomly
            double angle = RNG.nextDouble() * 360;
            setRotation(angle + 90);
            Globals.bulletManager.add(new Bullet(this, 300));
        }
        Globals.soundManager.playSound(SoundManager.Sounds.ENEMY_LASER_SHOOT);
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
    }

    @Override
    protected void onKill() {
        explode();
        Globals.hudManager.addScore(getScore());
    }

    /**
     * Gets the score this enemy is worth.
     *
     * @return the score
     */
    public int getScore() {
        return type == Type.SMALL ? 1000 : 200;
    }

    /**
     * Sets the alive status of this enemy. Needed for reusing the enemy instance.
     *
     * @param alive the new alive status
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * An enum that stores the different enemy types' size and image path.
     */
    public enum Type {
        SMALL("file:resources/sprites/enemy_medium.png", Size.SMALL),
        LARGE("file:resources/sprites/enemy_large.png", Size.MEDIUM);

        final String path;
        final Size size;

        /**
         * Instantiates a new Type.
         *
         * @param path the path
         * @param size the size
         */
        Type(String path, Size size) {
            this.path = path;
            this.size = size;
        }
    }

    /**
     * Sets the angle offset for the small enemy. Higher values makes the enemy shoot less accurate.
     *
     * @param offset the offset in degrees
     */
    public void setAngleOffset(int offset) {
        angleOffset = offset;
    }

    /**
     * Gets the current angle offset of the small enemy.
     *
     * @return the angle offset in degrees
     */
    public int getAngleOffset() {
        return angleOffset;
    }

}
