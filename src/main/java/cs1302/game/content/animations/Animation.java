package cs1302.game.content.animations;

import javafx.scene.canvas.GraphicsContext;

/**
 * An abstract class that represents an animation. Animations run as soon as they are instantiated.
 */
public abstract class Animation {

    protected boolean finished = true;

    /**
     * Handle.
     *
     * @param now the now
     */
    public abstract void handle(long now);

    /**
     * Render.
     *
     * @param ctx the ctx
     */
    public abstract void render(GraphicsContext ctx);

    /**
     * Is finished boolean.
     *
     * @return the boolean
     */
    public boolean isFinished() {
        return finished;
    }

}
