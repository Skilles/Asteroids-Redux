package cs1302.game.content.managers;

import cs1302.game.content.Globals;
import cs1302.game.content.animations.Animation;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Responsible for managing all animations in the game (currently only explosion).
 */
public class AnimationManager extends Manager {

    private final List<Animation> animations;

    /**
     * Instantiates a new Animation manager.
     */
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

    /**
     * Update the animations given the current time in nanoseconds.
     *
     * @param currentNanoTime the current time in nanoseconds.
     */
    public void update(long currentNanoTime) {
        for (Iterator<Animation> iterator = animations.iterator(); iterator.hasNext(); ) {
            Animation animation = iterator.next();
            animation.handle(currentNanoTime);
            if (animation.isFinished()) {
                iterator.remove();
            }
        }
    }

    /**
     * Add an animation to be managed by this manager.
     *
     * @param animation the animation
     */
    public void add(Animation animation) {
        this.animations.add(animation);
    }

}
