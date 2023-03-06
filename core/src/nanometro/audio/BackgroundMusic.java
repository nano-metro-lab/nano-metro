package nanometro.audio;

import com.badlogic.gdx.audio.Music;

public class BackgroundMusic {
    private Music music;
    private float volume;
    public BackgroundMusic (Music music, float volume) {
        this.music = music;
        this.volume = volume;
    }
    public void play() {
        this.music.setLooping(true);
        this.music.setVolume(volume);
        this.music.play();
    }
}
