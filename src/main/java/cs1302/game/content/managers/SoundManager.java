package cs1302.game.content.managers;

import cs1302.game.content.Globals;
import javafx.scene.media.AudioClip;

/**
 * Responsible for all the sound effects in the game.
 */
public class SoundManager {

    /**
     * Instantiates a new Sound manager.
     */
    public SoundManager() {
        Globals.setSoundManager(this);
        // Force load all the sounds to avoid hangups during gameplay
        Sounds.values();
    }

    /**
     * Play a sound.
     *
     * @param sound the sound
     */
    public void playSound(Sounds sound) {
        sound.getClip().play();
    }

    /**
     * The different types of Sounds that can be played.
     */
    public enum Sounds {
        EXPLOSION("explosion.wav"),
        SHIP_DAMAGE("ship_damage.wav"),
        LASER_SHOOT("laser_shoot.wav"),
        ENEMY_LASER_SHOOT("laser_shoot2.wav"),
        HYPERSPACE("hyperspace.wav");

        private final AudioClip sound;

        /**
         * Instantiates a new Sound.
         *
         * @param fileName the file name of the sound
         */
        Sounds(String fileName) {
            this.sound = new AudioClip("file:resources/sounds/" + fileName);
        }

        /**
         * Gets the audio clip.
         *
         * @return the player
         */
        public AudioClip getClip() {
            return sound;
        }
    }

}
