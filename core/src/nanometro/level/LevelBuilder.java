package nanometro.level;

import nanometro.gfx.Location;

import java.util.ArrayList;
import java.util.List;

public class LevelBuilder {
  private String name;
  private final List<Location> restLocations = new ArrayList<>();
  private final List<Float> delayTimes = new ArrayList<>();

  public LevelBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public LevelBuilder addLocation(float x, float y, Location.LocationType type, float delayTime) {
    Location location = new Location(x, y, type);
    restLocations.add(location);
    delayTimes.add(delayTime);
    return this;
  }

  public LevelLoader build() {
    if (name == null) {
      throw new IllegalStateException("Name is not set");
    }
    return new LevelLoaderImpl(name, restLocations, delayTimes);
  }
}
