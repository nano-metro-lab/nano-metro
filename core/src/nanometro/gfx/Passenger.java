package nanometro.gfx;

import nanometro.model.service.ModelService;


import java.util.List;

public class Passenger {
    private Location.LocationType type; // destination type
    public Location location;
    public Location nextHop;
    public Passenger(Location.LocationType type) {
        this.type = type;
        this.location = null;
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
