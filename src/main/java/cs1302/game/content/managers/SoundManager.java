package cs1302.game.content.managers;

import cs1302.game.content.Globals;
import javafx.scene.media.AudioClip;

public class SoundManager {

    public SoundManager() {
        Globals.setSoundManager(this);
        // Force load all the sounds to avoid hangups during gameplay
        Sounds.values();
    }

    public void playSound(Sounds sound) {
        sound.getPlayer().play();
    }

    public enum Sounds {
        EXPLOSION("explosion.wav"),
        SHIP_DAMAGE("ship_damage.wav"),
        LASER_SHOOT("laser_shoot.wav"),
        ENEMY_LASER_SHOOT("laser_shoot2.wav"),
        HYPERSPACE("hyperspace.wav");

        private final AudioClip sound;

        Sounds(String fileName) {
            this.sound = new AudioClip("file:resources/sounds/" + fileName);
        }

        public AudioClip getPlayer() {
            return sound;
        }
    }

}
