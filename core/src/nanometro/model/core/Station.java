package nanometro.model.core;

import nanometro.model.shared.LocationType;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Station {
  private final Set<Line> lines = new HashSet<>();
  private final RoutesMap routesMap = new RoutesMap();
  private final LocationType type;

  public Station(LocationType type) {
    this.type = type;
  }

  public LocationType getType() {
    return type;
  }

  public void addLine(Line line) {
    if (!lines.add(line)) {
      throw new RuntimeException("station " + this + " is already on line " + line);
    }
  }

  public void removeLine(Line line) {
    if (!lines.remove(line)) {
      throw new RuntimeException("station " + this + " is not on line " + line);
    }
  }

  public Stream<Route> getRoutes(LocationType destinationType) {
    return routesMap.get(destinationType);
  }

  public void clearRoutesMap() {
    routesMap.clear();
  }

  private class RoutesMap {
    private final Map<Line, LineRoutesMap> map = new HashMap<>();

    RoutesMap() {
    }

    Stream<Route> get(LocationType destinationType) {
      List<Route> routes = Station.this.lines.stream()
        .filter(Predicate.not(Line::isFindingRoutes))
        .flatMap((line) -> getLineRoutes(destinationType, line))
        .toList();
      return Route.getBest(routes);
    }

    private Stream<Route> getLineRoutes(LocationType destinationType, Line line) {
      LineRoutesMap lineRoutesMap = getLineRoutesMap(line);
      return lineRoutesMap.get(destinationType);
    }

    private LineRoutesMap getLineRoutesMap(Line line) {
      return Optional.ofNullable(map.get(line))
        .orElseGet(() -> {
          LineRoutesMap lineRoutesMap = new LineRoutesMap(line);
          map.put(line, lineRoutesMap);
          return lineRoutesMap;
        });
    }

    void clear() {
      map.clear();
    }

    private class LineRoutesMap {
      private final Line line;
      private final Map<LocationType, List<Route>> map = new HashMap<>();

      LineRoutesMap(Line line) {
        this.line = line;
      }

      Stream<Route> get(LocationType destinationType) {
        return Optional.ofNullable(map.get(destinationType))
          .map(Collection::stream)
          .orElseGet(() -> {
            List<Route> routes = line.findRoutes(destinationType, Station.this).toList();
            map.put(destinationType, routes);
            return routes.stream();
          });
      }
    }
  }
}
