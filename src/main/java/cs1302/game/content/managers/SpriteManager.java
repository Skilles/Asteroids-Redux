package cs1302.game.content.managers;

import cs1302.game.content.sprites.Sprite;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

/**
 * A special type of manager that manages a collection of sprites.
 */
public abstract class SpriteManager extends Manager {

    protected final List<Sprite> sprites;

    /**
     * Instantiates a new Sprite manager.
     */
    public SpriteManager() {
        super();
        sprites = new ArrayList<>();
    }

    /**
     * Update.
     *
     * @param delta the delta
     */
    public abstract void update(double delta);

    /**
     * Render the sprites to the canvas given the graphics context.
     *
     * @param ctx the graphics context of the canvas
     */
    public void render(GraphicsContext ctx) {
        for (Sprite sprite : sprites) {
            sprite.render(ctx);
        }
    }

    /**
     * Called when a new game is started.
     */
    public abstract void reset();

}
