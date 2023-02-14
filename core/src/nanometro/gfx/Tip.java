package nanometro.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;

import static nanometro.GameScreen.camera;
import static nanometro.GameScreen.world;
import static nanometro.GameScreen.modelService;

public class Tip {

    Line line;
    Body tipBody;
    Vector2 position;

    public Tip(Line l, Station s) {
        this.line = l;
        this.position = s.getPosition();

    }

    public void update(Station s) {

    }

    public void draw(ShapeRenderer shape) {
        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.valueOf(this.line.colour));
        shape.circle(this.position.x - 1, this.position.y - 1, 2);
        shape.end();
    }

    public void destroy() {

    }
}
