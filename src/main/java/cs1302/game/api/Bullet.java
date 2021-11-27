package cs1302.game.api;

public class Bullet extends Sprite {

    private Sprite parent;

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
    protected boolean wrapAround() {
        return centerX > maxWidth + width || centerX < 0 || centerY > maxHeight + height || centerY < 0;
    }

    @Override
    protected void onOffScreen() {
        kill();
    }

    @Override
    public void onKill() {

    }

}
