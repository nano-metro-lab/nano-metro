package nanometro.level;

import com.badlogic.gdx.utils.Timer;
import nanometro.gfx.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class LevelImpl implements Level {
  private final String name;
  private final List<Location> locations;
  private final List<Location> restLocations;

  LevelImpl(String name, List<Location> locations, List<Location> restLocations) {
    this.name = name;
    this.locations = new ArrayList<>(locations);
    this.restLocations = new ArrayList<>(restLocations);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public List<Location> getLocations() {
    return Collections.unmodifiableList(locations);
  }

  @Override
  public void start() {
    schedule();
  }

  private void schedule() {
    Timer.schedule(new Timer.Task() {
      @Override
      public void run() {
        if (restLocations.isEmpty()) {
          cancel();
        } else {
          schedule();
          locations.add(restLocations.remove(0));
        }
      }
    }, 20);
  }
}
