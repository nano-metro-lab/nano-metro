package nanometro.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;

import static nanometro.GameScreen.*;

public class Tip {
    float radius = 1f;
    float offset = 3f;
    Line line;
    Body tipBody;
    Vector2 position;
    Sensor sensor;
    Station station;

    public Tip(Line l, Station s) {
        this.line = l;
        this.position = new Vector2(s.platform.x - offset, s.platform.y - offset);
        this.station = s;

        BodyDef tipBodyDef = new BodyDef();
        tipBodyDef.type = BodyDef.BodyType.StaticBody;
        CircleShape tipShape = new CircleShape();
        tipShape.setRadius(radius);
        this.tipBody = world.createBody(tipBodyDef);
        this.tipBody.createFixture(tipShape, 0.0f);
        this.tipBody.setTransform(position.x, position.y, 0);
        this.tipBody.setUserData(this);
        tipShape.dispose();

    }

    public void update(Station s) {

    }

    public void draw() {
        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.valueOf(this.line.colour));
        shape.circle(this.position.x, this.position.y,  radius, 20);
        shape.end();
    }

    public void destroy() {
        world.destroyBody(this.tipBody);

    }
}
