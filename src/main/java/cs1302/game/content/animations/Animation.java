package cs1302.game.content.animations;

import javafx.scene.canvas.GraphicsContext;

public abstract class Animation {

    protected boolean finished = true;

    public abstract void handle(long now);

    public abstract void render(GraphicsContext ctx);

    public void start() {
        finished = false;
    }

    public void reset() {
        finished = true;
    }

    public boolean isFinished() {
        return finished;
    }

}
