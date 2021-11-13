package cs1302.game;

import cs1302.game.api.Player;
import cs1302.game.api.Sprite;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

public class Asteroids extends Game {

    private Random rng;
    private Player player;

    public Asteroids(Stage stage, int width, int height) {
        super(stage, width, height);
    }

    @Override
    public void init(Stage stage) {
        super.init(stage);
        player = new Player();
        player.resize(Sprite.Size.SMALL);
        player.setPosition(300, 300);

        ctx.setFill( Color.GREEN );
        ctx.setStroke( Color.BLACK );
        ctx.setLineWidth(1);
    }

    @Override
    public void update(long currentNanoTime) {
        super.update(currentNanoTime);

        handlePlayerMovement(elapsedTime());

        player.brake(1.1f);
        player.update(elapsedTime());
    }

    @Override
    public void render(long delta) {
        super.render(delta);
        render(player);
        System.out.println(player);
    }

    void render(Sprite... sprites) {
        for (Sprite sprite : sprites) {
            sprite.render(ctx);
        }
    }

    private void handlePlayerMovement(double delta) {
        isKeyPressed(KeyCode.LEFT, () -> player.addRotation(-1));
        isKeyPressed(KeyCode.RIGHT, () -> player.addRotation(1));
        isKeyPressed(KeyCode.UP, () -> player.addVelocity(0, -1));
        isKeyPressed(KeyCode.DOWN, () -> player.addVelocity(0, 1));
        isKeyPressed(KeyCode.A, () -> player.addRotation(-1));
        isKeyPressed(KeyCode.D, () -> player.addRotation(1));
        isKeyPressed(KeyCode.W, () -> player.accelerate(delta));
        isKeyPressed(KeyCode.S, () -> player.decelerate(delta));
    }

}
