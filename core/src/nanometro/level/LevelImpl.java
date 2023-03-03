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
  private final List<Float> delayTimes;

  LevelImpl(String name, List<Location> restLocations, List<Float> delayTimes) {
    this.name = name;
    this.locations = new ArrayList<>(10);
    this.restLocations = restLocations;
    this.delayTimes = new ArrayList<>(delayTimes);
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
    if (this.restLocations.size() < 1 || this.restLocations.size() != this.delayTimes.size()) {
      throw new IllegalStateException("Level load error");
    }
    for (int i = 0; i < this.restLocations.size(); i++) {
      if (this.delayTimes.get(i) == 0.0f) {
        locations.add(restLocations.get(i));
      } else {
        int finalI = i;
        Timer.schedule(new Timer.Task() {
          @Override
          public void run() {
            locations.add(restLocations.get(finalI));
          }
        }, delayTimes.get(finalI));
      }
    }
  }
}
