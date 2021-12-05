package cs1302.game.content.sprites;

public class Bullet extends Sprite {

    private final Sprite parent;

    private static final int LIFE_TIME = 4;
    private double lifeTime;

    public Bullet(Sprite parent, double force) {
        super("file:resources/sprites/longBullet.png", Size.TINY);
        double cos = Math.cos(parent.radAngle - 1.57);
        double sin = Math.sin(parent.radAngle - 1.57);
        this.positionX = parent.positionX + 20 * cos;
        this.positionY = parent.positionY + 20 * sin;
        this.setRotation(parent.angle - 90);
        this.velocityX = force * cos;
        this.velocityY = force * sin;

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

    public Sprite getParent() {
        return parent;
    }

}
