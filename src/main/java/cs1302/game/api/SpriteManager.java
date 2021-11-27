package cs1302.game.api;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class SpriteManager {

    protected final List<Sprite> sprites;

    private final Queue<Sprite> spritesToAdd;

    public SpriteManager() {
        sprites = new ArrayList<>();
        spritesToAdd = new LinkedBlockingQueue<>();
    }

    public void addSprite(Sprite sprite) {
        spritesToAdd.add(sprite);
    }

    public void addSprites(Sprite... sprites) {
        this.spritesToAdd.addAll(List.of(sprites));
    }

    public void removeSprite(Sprite sprite) {
        sprites.remove(sprite);
    }

    public void removeSprite(int index) {
        sprites.remove(index);
    }

    public void clearSprites() {
        sprites.clear();
    }

    public int getSpriteCount() {
        return sprites.size();
    }

    public Sprite getSprite(int index) {
        return sprites.get(index);
    }

    public void updateSprites(double delta) {
        while (!spritesToAdd.isEmpty()) {
            sprites.add(spritesToAdd.remove());
        }
    }

    public void drawSprites(GraphicsContext ctx) {
        for (Sprite sprite : sprites) {
            sprite.render(ctx);
        }
    }

}
