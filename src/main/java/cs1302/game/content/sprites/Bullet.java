package cs1302.game.content.sprites;

import javafx.scene.shape.Shape;

/**
 * The Bullet sprite that is shot by the player and enemy. Kills whatever it hits if the parent is
 * not the sprite that is hit.
 */
public class Bullet extends Sprite {

    private final Sprite parent;

    private static final int LIFE_TIME = 4;
    private double lifeTime;

    /**
     * Instantiates a new Bullet.
     *
     * @param parent the parent
     * @param force  the force
     */
    public Bullet(Sprite parent, double force) {
        super("file:resources/sprites/bullet_long.png", Size.TINY);
        double cos = Math.cos(parent.radAngle - 1.57);
        double sin = Math.sin(parent.radAngle - 1.57);
        setPosition(parent.position.getX() + 20 * cos, parent.position.getY() + 20 * sin);
        setVelocity(force * cos, force * sin, 0);
        this.setRotation(parent.angle - 90);
        this.parent = parent;
    }

    @Override
    public void update(double delta) {
        super.update(delta);
        lifeTime += delta;
        if (lifeTime > LIFE_TIME) {
            kill();
        }
    }

    @Override
    public Shape getBoundary() {
        Shape boundary = super.getBoundary();
        boundary.setRotate(angle);
        return boundary;
    }

    @Override
    protected void onKill() {

    }

    /**
     * Gets the parent of this bullet.
     *
     * @return the sprite that shot this bullet
     */
    public Sprite getParent() {
        return parent;
    }

}
