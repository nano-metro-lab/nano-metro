package nanometro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import nanometro.audio.BackgroundMusic;
import nanometro.audio.EventSound;
import nanometro.gfx.*;
import nanometro.level.LevelLoader;
import nanometro.model.ModelServiceFactory;
import nanometro.model.service.ModelService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameScreen implements Screen {
    boolean DEBUG = false;
    public final NanoMetro game;

    public static OrthographicCamera camera;
    public static ScreenViewport viewport;
    public static World world = new World(new Vector2(0, 0), false); // non-gravity Todo
    public static List<Line> lineList = new ArrayList<Line>(5);
    public static List<Train> trainList = new ArrayList<Train>(5);
    public static List<Colour> colourList = new ArrayList<>(5);
    public static List<Location> locationList = new ArrayList<>(10);

    public static ModelService<Location, Line> modelService = ModelServiceFactory.getInstance();
    public static SpriteBatch batch;
    public static ShapeRenderer shape;
    public static SpriteBatch debugBatch;

    private _Input_1 input1;
    public static final EventSound popSound =
            new EventSound(Gdx.audio.newMusic(Gdx.files.internal("./audio/pop.mp3")), 1.5f);
    public static final EventSound f6Sound =
            new EventSound(Gdx.audio.newMusic(Gdx.files.internal("./audio/f6.mp3")), 0.06f);
    public static final EventSound g6Sound =
            new EventSound(Gdx.audio.newMusic(Gdx.files.internal("./audio/g6.mp3")), 0.06f);
    public static final EventSound clickSound =
            new EventSound(Gdx.audio.newMusic(Gdx.files.internal("./audio/click.wav")), 0.08f);
    public static final BackgroundMusic bgm =
            new BackgroundMusic(Gdx.audio.newMusic(Gdx.files.internal("./audio/lazy_afternoon.mp3")), 0.2f);
            
    public static float maxDistance = 0;
    private Bezier<Vector2> bezierPath;

    public GameScreen(NanoMetro game, LevelLoader levelLoader) {
        this.game = game;
        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
		viewport = new ScreenViewport(camera);
		viewport.setUnitsPerPixel(0.05f);
		viewport.apply();

        game.batch = new SpriteBatch();
        game.debugBatch = new SpriteBatch();
        debugBatch = game.debugBatch;
        batch = game.batch;
        game.shape = new ShapeRenderer();
        game.shape.setProjectionMatrix(camera.combined);
        shape = game.shape;
        input1 = new _Input_1();
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(input1);
        Gdx.input.setInputProcessor(inputMultiplexer);
        setup(levelLoader);

        bgm.play();
        
        camera.zoom -= 0.165f;
        baseZoom = camera.zoom;
        camera.zoom -= 0.15f;

    }

    private void setup(LevelLoader levelLoader) {
        levelLoader.load();

        Timer.schedule(new Timer.Task() {
            private final Random random = new Random();
            @Override
            public void run() {
                Timer.schedule(this, random.nextFloat(4));
                Location location = getRandomLocation();
                Location.LocationType passengerType;
                do {
                    passengerType = getRandomLocationType();
                } while (passengerType == location.getType());
                location.addPassenger(new Passenger(passengerType));
            }

            private Location getRandomLocation() {
                int locationIndex = random.nextInt(locationList.size());
                return locationList.get(locationIndex);
            }

            private Location.LocationType getRandomLocationType() {
                Location.LocationType[] locationTypes = Location.LocationType.values();
                Location.LocationType locationType;
                do {
                    int locationTypeIndex = random.nextInt(locationTypes.length);
                    locationType = locationTypes[locationTypeIndex];
                } while (locationType == Location.LocationType.PREVIEW);
                return locationType;
            }
        }, 2);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to clear are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
//		ScreenUtils.clear(0, 0, 0.2f, 1);
//		ScreenUtils.clear(Color.valueOf("#002B4AFF"));
        ScreenUtils.clear(Color.valueOf("#f0eef0"));
//		ScreenUtils.clear(Color.WHITE);
//		batch.setProjectionMatrix(camera.combined);
        // tell the camera to update its matrices.
        camera.update();
        zoom();
        for (Line line : lineList) {
            line.draw();
        }
        if (DEBUG) game.debugRenderer.render(world, camera.combined);
        for (Train train : trainList) {
//			Gdx.gl.glLineWidth(5);
            train.run();
            if (DEBUG) train.drawDebug();
            train.draw();
        }
        for (Location l : locationList) {
            if (DEBUG) l.drawDebug();
            l.draw();
        }
        input1.draw();
        world.step(1/60f, 6, 2);
    }

    private float baseZoom;
    private final float zoomFullTime = 10;
    private float zoomTime = zoomFullTime;
    public static float zoomOffset = 0;
    private void zoom() {
        float startZoom = camera.zoom - zoomOffset;
        float endZoom = baseZoom + maxDistance * 0.005f - zoomOffset;
        if (startZoom == endZoom) return;
        if (zoomTime == zoomFullTime) {
            this.bezierPath = new Bezier<>(new Vector2(0, startZoom),
                    new Vector2(-2, startZoom),
                    new Vector2(zoomTime, endZoom),
                    new Vector2(zoomFullTime + 2, endZoom));
            zoomTime -= Gdx.graphics.getDeltaTime();
        } else if (zoomTime > 0) {
            Vector2 v = new Vector2();
            this.bezierPath.valueAt(v, 1 - zoomTime / zoomFullTime);
            camera.zoom = v.y;
            camera.zoom += zoomOffset;
            zoomTime -= Gdx.graphics.getDeltaTime();
        } else {
            zoomTime = zoomFullTime;
        }

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
