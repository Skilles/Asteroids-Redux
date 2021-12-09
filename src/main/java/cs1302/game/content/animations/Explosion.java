package cs1302.game.content.animations;

import cs1302.game.content.Globals;
import cs1302.game.content.managers.SoundManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.time.Duration;
import java.util.Random;

public class Explosion extends Animation {

    private static final int MAX_PARTICLES = 200;
    private static final int EXPLOSIVENESS = 5;
    private static final int MAX_DURATION = 150;

    final Rectangle[] rectangles;
    final long[] delays;
    final float[] angles;
    final long speed;
    final Random random;

    final float x, y, radius;
    final int numParticles;

    float aliveRatio;
    int counter;

    public Explosion(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.finished = false;

        numParticles = Math.min(MAX_PARTICLES, (int) ((radius * radius) / 70));
        rectangles = new Rectangle[numParticles];
        delays = new long[numParticles];
        angles = new float[numParticles];
        speed = Duration.ofSeconds(10 - EXPLOSIVENESS).toNanos();
        random = new Random();

        init();
    }

    private void init() {
        for (int i = 0; i < numParticles; i++) {
            rectangles[i] = new Rectangle(5, 5, Color.hsb(60, 1, 1));
            delays[i] = (long) (Math.random() * speed);
            angles[i] = (float) (2 * Math.PI * random.nextFloat());
        }

        Globals.animationManager.add(this);
        Globals.soundManager.playSound(SoundManager.Sounds.EXPLOSION);
    }

    public void handle(long now) {

        for (int i = 0; i < rectangles.length * aliveRatio; i++) {
            Rectangle r = rectangles[i];
            double angle = angles[i];
            long t = (now - delays[i]) % speed;
            double d = t * radius / speed;

            r.setOpacity((speed - t) / (double) speed);
            r.setX(x + d * Math.cos(angle));
            r.setY(y + d * Math.sin(angle));
            r.setFill(Color.hsb(60 * (1 - d / radius), 1, 1));
        }

        // Using a logarithmic function, calculate the aliveRatio based on the duration
        aliveRatio = (float) (1 - Math.log10(1 + (float) counter++ / (MAX_DURATION / 7.8)));

        if (aliveRatio <= 0) {
            finished = true;
        }
    }

    @Override
    public void render(GraphicsContext ctx) {
        // For each rectangle, render it in the graphics context
        for (int i = 0; i < rectangles.length * aliveRatio; i++) {
            Rectangle r = rectangles[i];
            ctx.save();
            ctx.setFill(r.getFill());
            ctx.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
            ctx.restore();
        }
    }

}
