package cs1302.game.api;

import javafx.scene.canvas.GraphicsContext;

public class HUDManager extends SpriteManager {

    private long score;


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


}
