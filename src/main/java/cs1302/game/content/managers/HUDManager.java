package cs1302.game.content.managers;

import cs1302.game.Game;
import cs1302.game.content.Globals;
import cs1302.game.content.sprites.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Responsible for the HUD in the game (health, score, etc.).
 */
public class HUDManager extends Manager {

    private int score;

    private boolean gameOver;

    private static int SCORE_THRESHOLD = 5000;

    /**
     * Instantiates a new Hud manager.
     */
    public HUDManager() {
        super();
        this.score = 0;
    }

    @Override
    protected void init() {
        Globals.setHudManager(this);
    }

    @Override
    public void render(GraphicsContext ctx) {
        ctx.setTextAlign(javafx.scene.text.TextAlignment.LEFT);
        drawScore(ctx);
        drawFPS(ctx);
        drawHealth(ctx, Globals.game.getPlayer());
        drawHyperspace(ctx, Globals.game.getPlayer());
        ctx.setTextAlign(javafx.scene.text.TextAlignment.CENTER);
        if (gameOver) {
            int x = Globals.WIDTH / 2;
            int y = Globals.HEIGHT / 2;
            ctx.save();
            ctx.setFont(new Font("Calibri", 30));
            ctx.fillText("Game Over", x, y);
            ctx.restore();
        }
    }

    /**
     * Gets the current score of the game.
     *
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Add a value to the current score.
     *
     * @param score the value to add
     */
    public void addScore(int score) {
        this.score += score;
        if (Globals.game.getPlayer().isAlive() && this.score >= SCORE_THRESHOLD) {
            SCORE_THRESHOLD += 5000;
            Globals.game.getPlayer().addHealth(1);
        }
    }

    /**
     * Draw the game score.
     *
     * @param ctx the graphics context
     */
    private void drawScore(GraphicsContext ctx) {
        ctx.fillText("Score: " + score, 10, 20);
    }

    /**
     * Draw the player's health.
     *
     * @param ctx    the graphics context
     * @param player the player
     */
    public void drawHealth(GraphicsContext ctx, Player player) {
        ctx.fillText("Health: " + player.getHealth(), 10, 40);
    }

    /**
     * Draw hyperspace indicator (whether the player can hyperspace).
     *
     * @param ctx    the graphics context
     * @param player the player
     */
    public void drawHyperspace(GraphicsContext ctx, Player player) {
        if (player.hasHyperspace()) {
            ctx.fillText("Hyperspace Available", Globals.WIDTH / 2.0 - 85, 20);
        }
    }

    /**
     * Draw fps.
     *
     * @param ctx the graphics context
     */
    private void drawFPS(GraphicsContext ctx) {
        ctx.fillText("FPS: " + Game.FPS, Globals.WIDTH - 75, 20);
    }

    /**
     * Sets the game over status of the game.
     *
     * @param gameOver the game over
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * Show a message at the center of the screen without fading in.
     *
     * @param ctx     the graphics context
     * @param message the message
     * @param color   the color
     */
    public void show(GraphicsContext ctx, String message, Color color) {
        show(ctx, message, color, false);
    }

    /**
     * Show a message at the center of the screen.
     *
     * @param ctx     the graphics context
     * @param message the message
     * @param color   the color of the text
     * @param fadeIn  whether the text should fade in
     */
    public void show(GraphicsContext ctx, String message, Color color, boolean fadeIn) {
        if (fadeIn) {
            fadeIn(ctx, message, color);
            return;
        }
        ctx.fillText(message, Globals.WIDTH / 2.0, Globals.HEIGHT / 2.0 + 30);
    }

    /**
     * Reset.
     */
    public void reset() {
        score = 0;
        gameOver = false;
    }

    double timer;

    /**
     * Fade in a message over 3 seconds in the center of the screen.
     *
     * @param ctx     the graphics context
     * @param message the message
     * @param color   the color of the text
     */
    public void fadeIn(GraphicsContext ctx, String message, Color color) {
        if (timer < 3) {
            timer += Globals.game.elapsedTime();
            ctx.save();
            ctx.setGlobalAlpha(timer / 2);
            ctx.setFill(color);
            ctx.fillText(message, Globals.WIDTH / 2.0, Globals.HEIGHT / 2.0 + 30);
            ctx.restore();
        } else {
            ctx.fillText(message, Globals.WIDTH / 2.0, Globals.HEIGHT / 2.0 + 30);
        }
    }

}
