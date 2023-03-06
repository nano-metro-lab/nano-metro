package nanometro.audio;

import com.badlogic.gdx.audio.Music;

public class EventSound {
    private Music music;

    public EventSound (Music music) {
        this.music = music;
    }

    public void play() {
        if (! this.music.isPlaying()) {
            this.music.play();
        }
    }
}
