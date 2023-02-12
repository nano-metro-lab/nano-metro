package nanometro.gfx;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import static nanometro.GameScreen.camera;
import static nanometro.GameScreen.world;
import static nanometro.GameScreen.modelService;


public class Sensor {
    private Body sensorBody;
    private Vector2 sensorPosition;
    public Section section;

    public Sensor(Section s, Vector2 v) {
        BodyDef sensorBodyDef = new BodyDef();
        sensorBodyDef.type = BodyDef.BodyType.StaticBody;
        CircleShape sensorShape = new CircleShape();
        sensorShape.setRadius(0.5f);
        this.sensorBody = world.createBody(sensorBodyDef);
        this.sensorBody.createFixture(sensorShape, 0.0f);
        this.sensorBody.setTransform(v.x, v.y, 0);
        this.sensorBody.setUserData(this);
        sensorShape.dispose();
        this.sensorPosition = v;
        this.section = s;
    }

    public Vector2 getSensorPosition() {
        return this.sensorPosition;
    }

    public void destroy() {
        world.destroyBody(this.sensorBody);
    }

}
