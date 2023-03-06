package nanometro.audio;

import com.badlogic.gdx.audio.Music;

public class BackgroundMusic {
    private Music music;
    public BackgroundMusic (Music music) {
        this.music = music;
    }
    public void play() {
        this.music.setLooping(true);
        this.music.play();
    }
}
