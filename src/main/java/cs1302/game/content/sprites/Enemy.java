package cs1302.game.content.sprites;

import cs1302.game.content.Globals;
import cs1302.game.content.managers.SoundManager;
import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

public class Enemy extends Sprite {

    private static final Random rng = new Random();

    private static int moveTime = 3;

    private final Type type;

    private int angleOffset = 30;

    public Enemy(Type type) {
        super(type.path, type.size);
        MIN_SPEED = 50;
        MAX_SPEED = 100;
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
            if (rng.nextBoolean()) {
                randomVelocity();
            }
            shoot();
        }
    }

    private void shoot() {
        if (type == Type.SMALL) {
            // shoots at the player in a 60 degree cone
            Player player = Globals.game.getPlayer();
            double angle = Math.atan2(player.position.getY() - position.getY(),
                    player.position.getX() - position.getX()) * (180 / Math.PI);
            if (angleOffset >= 0) {
                int neg = rng.nextBoolean() ? -1 : 1;
                angle += neg * rng.nextInt(angleOffset + 1);
            }
            setRotation(angle + 90);
            Globals.bulletManager.add(new Bullet(this, 300));
        } else {
            // shoots randomly
            double angle = rng.nextDouble() * 360;
            setRotation(angle + 90);
            Globals.bulletManager.add(new Bullet(this, 300));
        }
        Globals.soundManager.playSound(SoundManager.Sounds.ENEMY_LASER_SHOOT);
    }

    public static void setMoveTime(int moveTime) {
        Enemy.moveTime = moveTime;
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
    }

    @Override
    protected void onOffScreen() {

    }

    @Override
    protected void onKill() {
        explode();
        Globals.hudManager.addScore(getScore());
    }

    public int getScore() {
        return type == Type.SMALL ? 1000 : 200;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public enum Type {
        SMALL("file:resources/sprites/enemy_medium.png", Size.SMALL),
        LARGE("file:resources/sprites/enemy_large.png", Size.MEDIUM);

        final String path;
        final Size size;

        Type(String path, Size size) {
            this.path = path;
            this.size = size;
        }
    }

    public void setAngleOffset(int offset) {
        angleOffset = offset;
    }

    public int getAngleOffset() {
        return angleOffset;
    }

}
