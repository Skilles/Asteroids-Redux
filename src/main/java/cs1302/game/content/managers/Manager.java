package cs1302.game.content.managers;

import javafx.scene.canvas.GraphicsContext;

/**
 * An abstract class that represents a manager. Something that manages things that can be rendered
 * to a screen. Update implementation is handled on a case-by-case basis.
 */
public abstract class Manager {

    /**
     * Instantiates a new Manager.
     */
    public Manager() {
        init();
    }

    /**
     * Init.
     */
    protected abstract void init();

    /**
     * Render.
     *
     * @param ctx the ctx
     */
    public abstract void render(GraphicsContext ctx);

}
