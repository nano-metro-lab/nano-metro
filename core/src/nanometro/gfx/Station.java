package nanometro.gfx;

import com.badlogic.gdx.math.Vector2;


public class Station {

    Section upper;
    Section lower;
    Location location;
    Vector2 platform;
    Line line;


    public Vector2 getPosition() {
        return this.location.getPosition();
    }

    public Vector2 getPlatform() {
        if (this.platform == null) {
            // request platform
            this.platform = this.location.requestPlatform(); // Todo null case
            return this.platform;
        } else {
            return this.platform;
        }
    }

    public Station(Line line, Location location) {
        setStation(location.getPosition());
        this.location = location;
        this.line = line;
    }


    public void destroy() {
    }

    public void setStation(Vector2 position) {
//        stationBody.setTransform(position.x, position.y, 0);
    }


    @Override
    public String toString() {
        return "No." + this.hashCode();
    }


}
