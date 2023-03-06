package nanometro;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import nanometro.level.loaders.London;

public class NanoMetro extends Game {
    SpriteBatch batch;
    SpriteBatch debugBatch;
    ShapeRenderer shape;
    Box2DDebugRenderer debugRenderer;

    public static NanoMetro game;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        debugBatch = new SpriteBatch();
        debugRenderer = new Box2DDebugRenderer();

//        this.setScreen(new HomeScreen());
        this.setScreen(new GameScreen(this, new London()));
        game = this; // quick fix, not sure good or bad.
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        batch.dispose();
        shape.dispose();
        debugRenderer.dispose();
    }
}
