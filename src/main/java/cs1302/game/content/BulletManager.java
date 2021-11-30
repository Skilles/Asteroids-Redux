package cs1302.game.content;

import cs1302.game.content.sprites.Bullet;
import cs1302.game.content.sprites.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BulletManager {

    private final List<Bullet> bulletList;

    private final List<Pair<Sprite, Consumer<Bullet>>> consumers;

    public BulletManager() {
        bulletList = new ArrayList<>();
        consumers = new ArrayList<>();
    }

    public void update(double delta) {
        bulletList.removeIf(bullet -> !bullet.isAlive());
        consumers.removeIf(pair -> !pair.getKey().isAlive());

        for (Bullet bullet : bulletList) {
            bullet.update(delta);
            //noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < consumers.size(); i++) {
                Pair<Sprite, Consumer<Bullet>> consumerPair = consumers.get(i);
                consumerPair.getValue().accept(bullet);
            }
        }
    }

    public void render(GraphicsContext gc) {
        for (Bullet bullet : bulletList) {
            bullet.render(gc);
        }
    }

    public void add(Bullet bullet) {
        bulletList.add(bullet);
    }

    public void remove(Bullet bullet) {
        bulletList.remove(bullet);
    }

    public void removeAll() {
        bulletList.clear();
    }

    public List<Bullet> getBullets() {
        return bulletList;
    }

    void bindFunction(Sprite sprite, Consumer<Bullet> consumer) {
        consumers.add(new Pair<>(sprite, consumer));
    }

    void removeConsumer(Sprite sprite) {
        consumers.removeIf(pair -> pair.getKey() == sprite);
    }

}
