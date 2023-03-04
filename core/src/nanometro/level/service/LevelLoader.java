package nanometro.level.service;

import nanometro.gfx.Location;

import java.util.List;

public interface LevelLoader {
  String getName();

  List<Location> getLocations();

  void start();
}
