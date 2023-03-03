package nanometro.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import nanometro.model.service.ModelService;


import java.util.List;

import static nanometro.GameScreen.camera;
import static nanometro.GameScreen.shape;

public class Passenger {
    private Location.LocationType type; // destination type
    public Location location;
    public Location nextHop;
    public Passenger(Location.LocationType type) {
        this.type = type;
        this.location = null;
    }

    public void draw(Vector2 qPosition) {
        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.valueOf("#1f1f1f"));
        if (this.getType() == Location.LocationType.TRIANGLE) {
            float x1, x2, x3, y1, y2, y3;
            x1 = qPosition.x;
            x2 = qPosition.x + 0.84f;
            x3 = qPosition.x + 0.42f;
            y1 = qPosition.y;
            y2 = qPosition.y;
            y3 = qPosition.y + 0.75f;
            shape.triangle(x1, y1, x2, y2, x3, y3);
        } else if (this.getType() == Location.LocationType.SQUARE) {
            shape.rect(qPosition.x, qPosition.y, 0.75f, 0.75f);
        } else if (this.getType() == Location.LocationType.CIRCLE) {
            shape.circle(qPosition.x + 0.35f, qPosition.y + 0.35f, 0.4f, 20);
        }
        shape.end();
    }

    public Location.LocationType getType() {
        return this.type;
    }

    public void setLocation(Location l) {
        this.location = l;
    }

    @Override
    public String toString() {
        return type.toString();
    }

    public void destroy() {
        return;
    }
}
