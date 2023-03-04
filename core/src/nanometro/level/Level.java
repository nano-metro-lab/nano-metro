package nanometro.level;

import nanometro.gfx.Location;

import java.util.List;

public interface Level {
  String getName();

  String getDescription();

  List<Location> getLocations();

  void start();
}
