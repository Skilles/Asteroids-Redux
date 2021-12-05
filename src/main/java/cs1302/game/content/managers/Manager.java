package cs1302.game.content.managers;

import javafx.scene.canvas.GraphicsContext;

public abstract class Manager {

    public Manager() {
        init();
    }

    protected abstract void init();

    public abstract void render(GraphicsContext ctx);

}
