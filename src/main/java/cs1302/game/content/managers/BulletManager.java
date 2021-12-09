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

public class BulletManager extends Manager {

    private final List<Bullet> bulletList;

    private final List<Pair<Sprite, Consumer<Bullet>>> consumers;

    public BulletManager() {
        super();
        bulletList = new ArrayList<>();
        consumers = new ArrayList<>();
    }

    protected void init() {
        Globals.setBulletManager(this);
    }

    public void update(double delta) {
        bulletList.removeIf(bullet -> !bullet.isAlive());
        consumers.removeIf(pair -> !pair.getKey().isAlive() && !(pair.getKey() instanceof Enemy));

        for (Bullet bullet : bulletList) {
            bullet.update(delta);
            //noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < consumers.size(); i++) {
                Pair<Sprite, Consumer<Bullet>> consumerPair = consumers.get(i);
                Sprite sprite = consumerPair.getKey();
                if (!bullet.getParent().equals(sprite) && sprite.isAlive() && bullet.intersects(sprite)) {
                    consumerPair.getValue().accept(bullet);
                }
            }
        }
    }

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

    public void add(Bullet bullet) {
        bulletList.add(bullet);
    }

    public void bindFunction(Sprite sprite, Consumer<Bullet> consumer) {
        consumers.add(new Pair<>(sprite, consumer));
    }

    public void reset() {
        bulletList.clear();
        consumers.clear();
    }

}
