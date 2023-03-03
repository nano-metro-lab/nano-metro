package nanometro.level;

import nanometro.gfx.Location;

import java.util.List;

public interface Level {
  String getName();

  List<Location> getLocations();

  void start();
}
