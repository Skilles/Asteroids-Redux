package cs1302.game;

import cs1302.game.api.Sprite;
import javafx.animation.AnimationTimer;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.BitSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Game extends StackPane {

    protected final Logger logger = Logger.getLogger("cs1302.game.Game");

    int width;
    int height;

    Canvas canvas;
    GraphicsContext ctx;
    Bounds bounds;

    private AnimationTimer timer;
    private final BitSet keysPressed = new BitSet();
    private long delta;
    private double elapsedTime;

    public Game(Stage stage, int width, int height) {
        this.width = width;
        this.height = height;
        init(stage);
    }

    public void init(Stage stage) {
        this.canvas = new Canvas(width, height);
        this.ctx = canvas.getGraphicsContext2D();
        this.bounds = new BoundingBox(0, 0, width, height);
        Sprite.setBounds(bounds);
        setMinWidth(width);
        setMinHeight(height);

        stage.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
        stage.addEventFilter(KeyEvent.KEY_RELEASED, this::handleKeyReleased);

        getChildren().addAll(canvas);

        this.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(now);
                render(now);
            }
        };
        delta = System.nanoTime();
        logger.setLevel(Level.WARNING);
    }

    public void update(long currentNanoTime) {
        // calculate time since last update.
        elapsedTime = (currentNanoTime - delta) / 1000000000.0;
        delta = currentNanoTime;
    }

    public void render(long currentNanoTime) {
        ctx.clearRect(0, 0, width, height);
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
    }

    protected double elapsedTime() {
        return elapsedTime;
    }

}
