package nanometro.level.builder;

import com.badlogic.gdx.utils.Timer;
import nanometro.gfx.Location;
import nanometro.level.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class LevelImpl implements Level {
  private final String name;
  private final String description;
  private final List<Location> locations;
  private final List<Location> restLocations;
  private final List<Float> delayTimes;

  LevelImpl(String name, String description, List<Location> restLocations, List<Float> delayTimes) {
    this.name = name;
    this.description = description;
    this.locations = new ArrayList<>(10);
    this.restLocations = new ArrayList<>(restLocations);
    this.delayTimes = new ArrayList<>(delayTimes);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    return description;
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
      throw new IllegalStateException("LevelLoader load error");
    }
    for (int i = 0; i < this.restLocations.size(); i++) {
      if (this.delayTimes.get(i) == 0.0f) {
        Location l = restLocations.get(i);
        locations.add(l);
        l.init();
      } else {
        int finalI = i;
        Timer.schedule(new Timer.Task() {
          @Override
          public void run() {
            Location l = restLocations.get(finalI);
            locations.add(l);
            l.init();

          }
        }, delayTimes.get(finalI));
      }
    }
  }
}
