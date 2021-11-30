package cs1302.game;

import cs1302.game.api.AsteroidManager;
import cs1302.game.api.HUDManager;
import cs1302.game.api.Player;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

public class Asteroids extends Game {

    private Random rng;
    private Player player;
    AsteroidManager asteroidManager;
    HUDManager hudManager;

    public Asteroids(Stage stage, int width, int height) {
        super(stage, width, height);
    }

    @Override
    public void init(Stage stage) {
        super.init(stage);
        rng = new Random(123);
        ctx.setGlobalAlpha(0.6);
        player = new Player();
        player.setPosition(300, 300);

        asteroidManager = new AsteroidManager(player);
        asteroidManager.generateAsteroids(10, rng);

        hudManager = new HUDManager();
        player.setHudManager(hudManager);

        ctx.setFill( Color.GREEN );
        ctx.setStroke( Color.BLACK );
        ctx.setLineWidth(1);
    }

    @Override
    public void update(long currentNanoTime) {
        super.update(currentNanoTime);

        handleControls(elapsedTime());

        player.brake(elapsedTime(), 200);
        player.update(elapsedTime());

        asteroidManager.updateSprites(elapsedTime());
        hudManager.update(elapsedTime());

    }

    @Override
    public void render(long delta) {
        super.render(delta);
        player.render(ctx);
        asteroidManager.drawSprites(ctx);
        hudManager.render(ctx);
    }

    private void handleControls(double delta) {
        isKeyPressed(KeyCode.LEFT, () -> player.turnLeft(delta));
        isKeyPressed(KeyCode.RIGHT, () -> player.turnRight(delta));
        isKeyPressed(KeyCode.UP, () -> player.accelerate(delta));
        isKeyPressed(KeyCode.DOWN, () -> player.decelerate(delta));
        isKeyPressed(KeyCode.A, () -> player.turnLeft(delta));
        isKeyPressed(KeyCode.D, () -> player.turnRight(delta));
        isKeyPressed(KeyCode.W, () -> player.accelerate(delta));
        isKeyPressed(KeyCode.S, () -> player.decelerate(delta));

        isKeyPressed(KeyCode.SPACE, () -> player.shoot(delta));
    }

}
