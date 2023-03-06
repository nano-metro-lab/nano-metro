package nanometro.audio;

import com.badlogic.gdx.audio.Music;

public class EventSound {
    private Music music;
    private float volume;

    public EventSound (Music music, float volume) {
        this.music = music;
        this.volume = volume;
    }

    public void play() {
        if (this.music.isPlaying()) {
            this.music.stop();
        }
        this.music.setVolume(volume);
        this.music.play();
    }
}
