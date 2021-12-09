package cs1302.game;

import cs1302.game.content.Globals;
import cs1302.game.content.MainMenu;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.BitSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import static cs1302.game.content.Globals.HEIGHT;
import static cs1302.game.content.Globals.WIDTH;

public abstract class Game extends StackPane {

    protected final Logger logger = Logger.getLogger("cs1302.game.Game");

    public static int FPS;

    GraphicsContext ctx;

    private AnimationTimer timer;
    private final BitSet keysPressed = new BitSet();
    private long delta;
    private double elapsedTime;
    private int fpsCounter;

    protected MainMenu mainMenu;
    private String title;

    public Game(String title, Stage stage, int width, int height) {
        Globals.setWidth(width);
        Globals.setHeight(height);
        init(title, stage);
    }

    public void init(String title, Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        this.ctx = canvas.getGraphicsContext2D();
        this.title = title;
        this.mainMenu = new MainMenu(this);
        setMinWidth(WIDTH);
        setMinHeight(HEIGHT);

        stage.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
        stage.addEventFilter(KeyEvent.KEY_RELEASED, this::handleKeyReleased);

        getChildren().addAll(canvas, mainMenu);

        this.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                render(now);
                update(now);
            }
        };
        delta = System.nanoTime();
        logger.setLevel(Level.WARNING);
    }

    public void update(long currentNanoTime) {
        // calculate time since last update.
        elapsedTime = (currentNanoTime - delta) / 1000000000.0;
        delta = currentNanoTime;

        // calculate the fps
        fpsCounter++;
        if (fpsCounter == 100) {
            FPS = (int) (1.0 / (elapsedTime));
            fpsCounter = 0;
        }
    }

    public void render(long currentNanoTime) {
        // clear the canvas
        ctx.clearRect(0, 0, WIDTH, HEIGHT);
    }

    /**
     * Add the key code for the pressed key to the set of pressed keys.
     * @param event associated key event
     */
    private void handleKeyPressed(KeyEvent event) {
        logger.info(event.toString());
        keysPressed.set(event.getCode().getCode());
    } // handleKeyPressed

    /**
     * Remove the key code for the released key from the set of pressed keys.
     * @param event associated key event
     */
    private void handleKeyReleased(KeyEvent event) {
        logger.info(event.toString());
        keysPressed.clear(event.getCode().getCode());
    } // handleKeyReleased

    /**
     * Return whether or not a key is currently pressed.
     * @param key the key code to check
     * @return {@code true} if the key is pressed; otherwise {@code false}
     */
    protected final boolean isKeyPressed(KeyCode key) {
        return keysPressed.get(key.getCode());
    } // isKeyPressed

    /**
     * Return whether or not any key is pressed.
     * @return {@code true} if any key is pressed; otherwise {@code false}
     */
    protected final boolean isKeyPressed() {
        return !keysPressed.isEmpty();
    } // isKeyPressed

    /**
     * Return whether or not a key is currently pressed. If the key is pressed, then
     * {@code handler.run()} is run on the calling thread before the method returns.
     * @param key the key code to check
     * @param handler the object whose {@code run} method is invoked
     * @return {@code true} if the key is pressed; otherwise {@code false}
     */
    protected final boolean isKeyPressed(KeyCode key, Runnable handler) {
        if (isKeyPressed(key)) {
            handler.run();
            return true;
        } else {
            return false;
        } // if
    } // isKeyPressed

    public void play() {
        timer.start();
    }

    public void pause() {
        timer.stop();
        ctx.clearRect(0, 0, WIDTH, HEIGHT);
    }

    public double elapsedTime() {
        return elapsedTime;
    }

    public String getTitle() {
        return title;
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

}
