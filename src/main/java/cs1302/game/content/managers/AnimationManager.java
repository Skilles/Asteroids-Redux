package cs1302.game.content.managers;

import cs1302.game.content.Globals;
import cs1302.game.content.animations.Animation;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AnimationManager extends Manager {

    private final List<Animation> animations;

    public AnimationManager() {
        super();
        this.animations = new ArrayList<>();
    }

    @Override
    protected void init() {
        Globals.setAnimationManager(this);
    }

    @Override
    public void render(GraphicsContext ctx) {
        for (Animation animation : animations) {
            animation.render(ctx);
        }
    }

    public void update(long currentNanoTime) {
        for (Iterator<Animation> iterator = animations.iterator(); iterator.hasNext(); ) {
            Animation animation = iterator.next();
            animation.handle(currentNanoTime);
            if (animation.isFinished()) {
                iterator.remove();
            }
        }
    }

    public void add(Animation animation) {
        this.animations.add(animation);
    }

    public void clear() {
        this.animations.clear();
    }

}
