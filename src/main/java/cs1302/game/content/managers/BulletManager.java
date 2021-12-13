package cs1302.game.content.managers;

import cs1302.game.content.Globals;
import cs1302.game.content.sprites.Bullet;
import cs1302.game.content.sprites.Enemy;
import cs1302.game.content.sprites.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Responsible for managing the bullets fired by the player and enemies. Handles bullet collisions.
 */
public class BulletManager extends Manager {

    private final List<Bullet> bulletList;

    private final List<Pair<Sprite, Consumer<Bullet>>> consumers;

    /**
     * Instantiates a new Bullet manager.
     */
    public BulletManager() {
        super();
        bulletList = new ArrayList<>();
        consumers = new ArrayList<>();
    }

    @Override
    protected void init() {
        Globals.setBulletManager(this);
    }

    /**
     * Update all the bullets by checking if they are colliding with certain sprites and killing
     * them and the bullet.
     *
     * @param delta the elapsed time since the last update
     */
    public void update(double delta) {
        bulletList.removeIf(bullet -> !bullet.isAlive());
        consumers.removeIf(pair -> !pair.getKey().isAlive() && !(pair.getKey() instanceof Enemy));

        for (Bullet bullet : bulletList) {
            bullet.update(delta);
            //noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < consumers.size(); i++) {
                Pair<Sprite, Consumer<Bullet>> consumerPair = consumers.get(i);
                Sprite sprite = consumerPair.getKey();
                if (!bullet.getParent().equals(sprite) && sprite.isAlive()
                        && bullet.intersects(sprite)) {
                    consumerPair.getValue().accept(bullet);
                }
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        for (Bullet bullet : bulletList) {
            if (bullet.getParent() instanceof Enemy) {
                ColorAdjust tint = new ColorAdjust();
                tint.setHue(0.8);
                gc.setEffect(tint);
                bullet.render(gc);
                gc.setEffect(null);
                continue;
            }
            bullet.render(gc);
        }
    }

    /**
     * Add a bullet to the game.
     *
     * @param bullet the bullet
     */
    public void add(Bullet bullet) {
        bulletList.add(bullet);
    }

    /**
     * Bind a sprite to a bullet consumer to be called when a bullet collides with the sprite.
     * Used for collision behavior.
     *
     * @param sprite   the sprite to bind to
     * @param consumer a consumer to be called when the bullet collides with the sprite
     */
    public void bindFunction(Sprite sprite, Consumer<Bullet> consumer) {
        consumers.add(new Pair<>(sprite, consumer));
    }

    /**
     * Reset.
     */
    public void reset() {
        bulletList.clear();
        consumers.clear();
    }

}
