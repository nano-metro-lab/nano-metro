package nanometro.level;

import nanometro.gfx.Location;

import java.util.ArrayList;
import java.util.List;

public class LevelBuilder {
  private String name;
  private final List<Location> locations = new ArrayList<>();
  private final List<Location> initialLocations = new ArrayList<>();

  public LevelBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public LevelBuilder addLocation(float x, float y, Location.LocationType type) {
    Location location = new Location(x, y, type);
    locations.add(location);
    return this;
  }

  public LevelBuilder addInitialLocation(float x, float y, Location.LocationType type) {
    Location location = new Location(x, y, type);
    initialLocations.add(location);
    return this;
  }

  public Level build() {
    if (name == null) {
      throw new IllegalStateException("Name is not set");
    }
    return new LevelImpl(name, initialLocations, locations);
  }
}
