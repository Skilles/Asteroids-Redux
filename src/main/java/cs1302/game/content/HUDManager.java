package cs1302.game.content;

import cs1302.game.Game;
import cs1302.game.content.sprites.Player;
import javafx.scene.canvas.GraphicsContext;

public class HUDManager extends SpriteManager {

    private long score;

    private boolean gameOver;

    public HUDManager() {
        super();
        score = 0;
    }

    public void update(double delta) {
        super.updateSprites(delta);
    }

    public void render(GraphicsContext ctx) {
        super.drawSprites(ctx);
        drawScore(ctx);
        drawFPS(ctx);
        if (gameOver) {
            int x = Game.WIDTH / 2 - 35;
            int y = Game.HEIGHT / 2;
            ctx.fillText("Game Over", x, y);
        }
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getScore() {
        return score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    private void drawScore(GraphicsContext ctx) {
        ctx.fillText("Score: " + score, 10, 20);
    }

    public void drawHealth(GraphicsContext ctx, Player player) {
        ctx.fillText("Health: " + player.getHealth(), 10, 40);
    }

    private void drawFPS(GraphicsContext ctx) {
        ctx.fillText("FPS: " + Game.FPS, 10, 60);
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }


}
