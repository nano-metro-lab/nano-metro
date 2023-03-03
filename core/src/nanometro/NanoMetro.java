package nanometro;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class NanoMetro extends Game {
    SpriteBatch batch;
    SpriteBatch debugBatch;
    ShapeRenderer shape;
    Box2DDebugRenderer debugRenderer;
    Music music;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        debugBatch = new SpriteBatch();
        debugRenderer = new Box2DDebugRenderer();
        music = Gdx.audio.newMusic(Gdx.files.internal("assets/Lazy-Afternoon.mp3"));
        this.setScreen(new GameScreen(this));
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        batch.dispose();
        shape.dispose();
        debugRenderer.dispose();
        music.dispose();
    }
}
