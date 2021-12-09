package cs1302.game.content.sprites;

import javafx.scene.shape.Shape;

public class Bullet extends Sprite {

    private final Sprite parent;

    private static final int LIFE_TIME = 4;
    private double lifeTime;

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
    protected void onOffScreen() {

    }

    @Override
    public void onKill() {

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

    public Sprite getParent() {
        return parent;
    }

}
