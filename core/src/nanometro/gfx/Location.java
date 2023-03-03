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
        if (this.platformPool.isEmpty()) {
            return null;
        }
        Vector2 v = this.platformPool.get(0);
        this.platformPool.remove(v);
        return v;
    }


    private List<Vector2> platformPool = new ArrayList<>();
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
        batch.begin();
        debugFont.draw(batch, passengerList.toString(), p.x, p.y);
        batch.end();
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
        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        for (Passenger p : this.passengerList) {
            shape.setColor(Color.valueOf("#1f1f1f"));
            if (p.getType() == LocationType.TRIANGLE) {
                float x1, x2, x3, y1, y2, y3;
                x1 = qPosition.x;
                x2 = qPosition.x + 0.84f;
                x3 = qPosition.x + 0.42f;
                y1 = qPosition.y;
                y2 = qPosition.y;
                y3 = qPosition.y + 0.75f;
                shape.triangle(x1, y1, x2, y2, x3, y3);
            } else if (p.getType() == LocationType.SQUARE) {
                shape.rect(qPosition.x, qPosition.y, 0.75f, 0.75f);
            } else if (p.getType() == LocationType.CIRCLE) {
                shape.circle(qPosition.x + 0.35f, qPosition.y + 0.35f, 0.4f, 20);
            }
            qPosition.add(qGap, 0);
        }
        shape.end();


    }

    public void destroy() {world.destroyBody(this.locationBody);
    }

    public Vector2 getPosition() {
        return position;
    }
}

