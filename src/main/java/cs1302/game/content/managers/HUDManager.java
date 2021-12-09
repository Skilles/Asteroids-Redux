package cs1302.game.content.managers;

import cs1302.game.Game;
import cs1302.game.content.Globals;
import cs1302.game.content.sprites.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class HUDManager extends Manager {

    private int score;

    private boolean gameOver;

    private static int SCORE_THRESHOLD = 5000;

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

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int score) {
        this.score += score;
        if (Globals.game.getPlayer().isAlive() && this.score >= SCORE_THRESHOLD) {
            SCORE_THRESHOLD += 5000;
            Globals.game.getPlayer().addHealth(1);
        }
    }

    private void drawScore(GraphicsContext ctx) {
        ctx.fillText("Score: " + score, 10, 20);
    }

    public void drawHealth(GraphicsContext ctx, Player player) {
        ctx.fillText("Health: " + player.getHealth(), 10, 40);
    }

    public void drawHyperspace(GraphicsContext ctx, Player player) {
        if (player.hasHyperspace()) {
            ctx.fillText("Hyperspace Available", Globals.WIDTH / 2.0 - 85, 20);
        }
    }

    private void drawFPS(GraphicsContext ctx) {
        ctx.fillText("FPS: " + Game.FPS, Globals.WIDTH - 75, 20);
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void show(GraphicsContext ctx, String message, Color color) {
        show(ctx, message, color, false);
    }

    public void show(GraphicsContext ctx, String message, Color color, boolean fadeIn) {
        if (fadeIn) {
            fadeIn(ctx, message, color);
            return;
        }
        ctx.fillText(message, Globals.WIDTH / 2.0, Globals.HEIGHT / 2.0 + 30);
    }

    public void reset() {
        score = 0;
        gameOver = false;
    }

    double timer;
    public void fadeIn(GraphicsContext ctx, String message, Color color) {
        // Fade in a message over 3 seconds
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
