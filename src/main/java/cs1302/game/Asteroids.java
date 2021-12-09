package cs1302.game;

import cs1302.game.content.Globals;
import cs1302.game.content.managers.*;
import cs1302.game.content.sprites.Player;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Asteroids extends Game {

    private Player player;
    private AsteroidManager asteroidManager;
    private HUDManager hudManager;
    private AnimationManager animationManager;
    private BulletManager bulletManager;
    private EnemyManager enemyManager;

    private Image background;

    private double deathTime;
    boolean paused;

    public Asteroids(Stage stage, int width, int height) {
        super("Asteroids Redux", stage, width, height);
    }

    @Override
    public void init(String title, Stage stage) {
        super.init(title, stage);
        Globals.setGame(this);
        // Set the background color and load the background image
        setStyle("-fx-background-color: black");
        background = new Image("file:resources/bg.png", Globals.WIDTH, Globals.HEIGHT, false, true);

        // Create the animation manager
        animationManager = new AnimationManager();
        // Create the bullet manager
        bulletManager = new BulletManager();

        // Create the player
        player = new Player();
        player.setPosition(Globals.WIDTH / 2.0, Globals.HEIGHT / 2.0);

        // Create the hud manager
        hudManager = new HUDManager();
        // Create the enemy manager
        enemyManager = new EnemyManager();

        // Create the asteroid manager
        asteroidManager = new AsteroidManager(player);
        asteroidManager.generateAsteroids();

        // Create the sound manager
        new SoundManager();

        ctx.setFill(Color.GREEN);
        ctx.setStroke(Color.BLACK);
        ctx.setLineWidth(1);
        ctx.setFont(new Font("Calibri", 20));
        ctx.setTextAlign(javafx.scene.text.TextAlignment.CENTER);
    }

    @Override
    public void update(long currentNanoTime) {
        super.update(currentNanoTime);
        if (getChildren().size() > 1) {
            return;
        }
        pauseTimer += elapsedTime();
        if (paused) {
            hudManager.show(ctx, "Press ESC to resume", Color.GREEN);
            if (pauseTimer >= 0.5 && isKeyPressed(KeyCode.ESCAPE)) {
                pauseTimer = 0;
                paused = false;
            }
            return;
        } else if (pauseTimer >= 0.5) {
            pauseTimer = 0;
        }

        asteroidManager.update(elapsedTime());
        animationManager.update(currentNanoTime);
        bulletManager.update(elapsedTime());
        enemyManager.update(elapsedTime());

        if (isKeyPressed(KeyCode.ESCAPE)) {
            pause();
        }

        if (player.isAlive()) {
            handleControls(elapsedTime());

            player.brake(elapsedTime(), 150);
            player.update(elapsedTime());
        } else {
            deathTime += elapsedTime();
            if (deathTime > 1) {
                hudManager.show(ctx, "Press any key to restart", Color.DARKGREEN, true);
                if (isKeyPressed()) {
                    reset();
                }
            }
        }
    }

    @Override
    public void render(long currentNanoTime) {
        super.render(currentNanoTime);
        drawBackground();
        asteroidManager.render(ctx);
        animationManager.render(ctx);
        hudManager.render(ctx);
        bulletManager.render(ctx);
        enemyManager.render(ctx);

        if (player.isAlive()) {
            player.render(ctx);
        }
    }

    private void handleControls(double delta) {
        isKeyPressed(KeyCode.A, () -> player.turnLeft(delta));
        isKeyPressed(KeyCode.D, () -> player.turnRight(delta));
        isKeyPressed(KeyCode.W, () -> player.accelerate(delta));
        isKeyPressed(KeyCode.S, () -> player.decelerate(delta));

        isKeyPressed(KeyCode.SPACE, () -> player.shoot());
        isKeyPressed(KeyCode.SHIFT, () -> player.hyperspace());
    }

    private void drawBackground() {
        ctx.drawImage(background, 0, 0);
    }

    public void reset() {
        super.pause();
        bulletManager.reset();
        player.reset();
        asteroidManager.reset();
        hudManager.reset();
        mainMenu.main();
        enemyManager.reset();
        deathTime = 0;
    }

    @Override
    public void play() {
        super.play();
        getChildren().remove(mainMenu);
    }

    public Player getPlayer() {
        return player;
    }

    double pauseTimer;
    @Override
    public void pause() {
        if (pauseTimer > 0.5) {
            paused = true;
        }
    }

}
