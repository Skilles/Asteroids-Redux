package cs1302.game;

import cs1302.game.content.AsteroidManager;
import cs1302.game.content.HUDManager;
import cs1302.game.content.sprites.Player;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Random;

public class Asteroids extends Game {

    private Random rng;
    private Player player;
    AsteroidManager asteroidManager;
    HUDManager hudManager;

    private Image background;

    public Asteroids(Stage stage, int width, int height) {
        super("Asteroids", stage, width, height);
    }

    @Override
    public void init(String title, Stage stage) {
        super.init(title, stage);
        Globals.setGame(this);
        // Set the background color and load the background image
        setStyle("-fx-background-color: black");
        background = new Image("file:resources/bg.png", Globals.WIDTH, Globals.HEIGHT, false, true);

        rng = new Random(123);
        player = new Player();
        player.setPosition(300, 300);

        asteroidManager = new AsteroidManager(player);
        asteroidManager.generateAsteroids(20, rng);

        hudManager = new HUDManager();
        player.setHudManager(hudManager);

        ctx.setFill( Color.GREEN );
        ctx.setStroke( Color.BLACK );
        ctx.setLineWidth(1);
        Font font = new Font("Calibri", 20);
        ctx.setFont(font);
    }

    @Override
    public void update(long currentNanoTime) {
        super.update(currentNanoTime);
        asteroidManager.updateSprites(elapsedTime());
        hudManager.update(elapsedTime());

        if (player.isAlive()) {
            handleControls(elapsedTime());

            player.brake(elapsedTime(), 200);
            player.update(elapsedTime());
        } else if (isKeyPressed()) {
            reset();
        }
    }

    @Override
    public void render(long delta) {
        super.render(delta);
        drawBackground();
        asteroidManager.drawSprites(ctx);
        hudManager.render(ctx);
        hudManager.drawHealth(ctx, player);

        if (player.isAlive()) {
            player.render(ctx);
        }
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

    private void drawBackground() {
        ctx.drawImage(background, 0, 0);
    }

    public void reset() {
        // TODO : reset the player, asteroids, menu, etc
    }

    @Override
    public void play() {
        super.play();
        getChildren().remove(mainMenu);
    }

}
