package nanometro.level.builder;

import nanometro.gfx.Location;
import nanometro.level.Level;

import java.util.ArrayList;
import java.util.List;

public class LevelBuilder {
  private String name;
  private String description;
  private final List<Location> restLocations = new ArrayList<>();
  private final List<Float> delayTimes = new ArrayList<>();

  public LevelBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public LevelBuilder setDescription(String description) {
    this.description = description;
    return this;
  }

  public LevelBuilder addLocation(float x, float y, Location.LocationType type, float delayTime) {
    Location location = new Location(x, y, type);
    restLocations.add(location);
    delayTimes.add(delayTime);
    return this;
  }

  public Level build() {
    if (name == null) {
      throw new IllegalStateException("Name is not set");
    }
    if (description == null) {
      throw new IllegalStateException("Description is not set");
    }
    return new LevelImpl(name, description, restLocations, delayTimes);
  }
}
