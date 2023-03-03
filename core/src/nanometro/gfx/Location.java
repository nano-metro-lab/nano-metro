package nanometro.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import nanometro.GameScreen;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static nanometro.GameScreen.*;

public class Location {
    public enum LocationType implements nanometro.model.shared.LocationType {
        CIRCLE, TRIANGLE, SQUARE, PREVIEW
    }

    public Hashtable<LocationType, String> imgs = new Hashtable<>();

    public LocationType getType() {
        return this.type;
    }

    public Vector2 requestPlatform() {
        for (Vector2 v: this.platformPool) {
            if (!this.occupiedPlatform.contains(v)) {
                this.occupiedPlatform.add(v);
                return v;
            }
        }
        return null;
    }

    public void releasePlatform(Vector2 v) {
        this.occupiedPlatform.remove(v);
    }


    private List<Vector2> platformPool = new ArrayList<>();
    private List<Vector2> occupiedPlatform = new ArrayList<>();
    private Vector2 position;
    Body locationBody;
    BitmapFont debugFont;
    List<Passenger> passengerList = new ArrayList<>(30);
    LocationType type;
    Texture locationImage;
    Sprite locationSprite;
    SpriteBatch batch = GameScreen.batch;
    ShapeRenderer shape = GameScreen.shape;

    public void addPassenger(Passenger p) {
        this.passengerList.add(p);
    }

    public void removePassenger(Passenger p) {
        this.passengerList.remove(p);
    }


    public Location(float x, float y, LocationType type) {
        this.position = new Vector2(x, y);
        this.type = type;

        BodyDef locationBodyDef = new BodyDef();
        this.locationBody = world.createBody(locationBodyDef);
        CircleShape locationShape = new CircleShape();
        locationShape.setRadius(2f);
        this.locationBody.createFixture(locationShape, 0.0f);
        locationShape.dispose();
        this.locationBody.setUserData(this);
        this.locationBody.setTransform(position.x, position.y, 0);

        Vector2 platformOffset = new Vector2(0.6f, 0.6f);
        this.platformPool.add(this.position);
        this.platformPool.add(this.position.cpy().add(platformOffset));
        this.platformPool.add(this.position.cpy().add(platformOffset).add(platformOffset));
        this.platformPool.add(this.position.cpy().add(platformOffset).add(platformOffset).add(platformOffset));


        // debug font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        parameter.color = Color.BLACK;
        BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        this.debugFont = font;

        //
        imgs.put(LocationType.CIRCLE, "./CIRCLE.png");
        imgs.put(LocationType.TRIANGLE, "./TRIANGLE.png");
        imgs.put(LocationType.SQUARE, "./SQUARE.png");
        ///

        locationImage = new Texture(Gdx.files.internal(imgs.get(this.type)));
        locationSprite = new Sprite(locationImage);
        locationSprite.setSize(6f, 6f);

        // add location to model
        modelService.addStation(this, this.getType());

    }

    public void drawDebug() {
        Vector3 p = new Vector3(position.x + 1.5f, position.y + 1f, 0);
        camera.project(p);
        debugBatch.begin();
        debugFont.draw(debugBatch, passengerList.toString(), p.x, p.y);
        debugBatch.end();
    }

    public void draw() {


        batch.begin();
//        Vector3 v = new Vector3(this.position.x, this.position.y, 0);
//        camera.project(v);
        batch.setProjectionMatrix(camera.combined);
//        batch.draw(locationImage, v.x - 50f, v.y  - 50f, 100f, 100f);
//        locationSprite.setPosition(v.x - 50f, v.y  - 50f);
        locationSprite.setPosition(this.position.x - this.locationSprite.getWidth() / 2 , this.position.y - this.locationSprite.getWidth() / 2);
        locationSprite.draw(batch);
        batch.end();
        //
        Vector2 qPosition = this.position.cpy().add(1.7f, 0.6f);
        float qGap = 1f;
        for (Passenger p : this.passengerList) {
            p.draw(qPosition);
            qPosition.add(qGap, 0);
        }
    }

    public void destroy() {world.destroyBody(this.locationBody);
    }

    public Vector2 getPosition() {
        return position;
    }
}

