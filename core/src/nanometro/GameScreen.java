package nanometro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import nanometro.gfx.*;
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
    private Train testTrain;
    private Section testSection;
    private Line testLine;

    private _Input_1 input1;
    private _Input_2 input2;


    public GameScreen(NanoMetro game) {
        this.game = game;
        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 40, 40);
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
//        input2 = new _Input_2();
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(input1);
//        inputMultiplexer.addProcessor(input2);
        Gdx.input.setInputProcessor(inputMultiplexer);
        setup();
    }

    private void setup() {
        // colours set up
        Colour c1 = new Colour("#fcce05");
        Colour c2 = new Colour("#1c4094");
        Colour c3 = new Colour("#f03024");
        Colour c4 = new Colour("#e0e0e0");
        colourList = List.of(c1, c2, c3);

        // locations set up
        Location l1 = new Location(4, 35, Location.LocationType.CIRCLE);
        Location l2 = new Location(12, 20, Location.LocationType.CIRCLE);
        Location l3 = new Location(20, 20, Location.LocationType.CIRCLE);
        Location l4 = new Location(28, 17, Location.LocationType.CIRCLE);
        Location l5 = new Location(36, 8, Location.LocationType.TRIANGLE);
        Location l6 = new Location(9, 15, Location.LocationType.CIRCLE);
        Location l7 = new Location(9, 7, Location.LocationType.SQUARE);
        Location l8 = new Location(38, 35, Location.LocationType.TRIANGLE);
        locationList = List.of(l1, l2, l3, l4, l5, l6, l7, l8);

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
        for (Line line : lineList) {
            line.draw();
        }
        if (DEBUG) game.debugRenderer.render(world, camera.combined);
        for (Train train : trainList) {
//			Gdx.gl.glLineWidth(5);
            train.run();
//            train.debugDraw();
            train.draw();
        }
        for (Location l : locationList) {
//            l.drawDebug();
            l.draw();
        }
        input1.draw();

        world.step(1/60f, 6, 2);
//		System.out.println(testLine);

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
