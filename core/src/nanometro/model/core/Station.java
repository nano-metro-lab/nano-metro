package nanometro.model.core;

import nanometro.model.shared.LocationType;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Station {
  private final LocationType type;
  private final List<Line> lines = new ArrayList<>();
  private final RoutesMap routesMap = new RoutesMap();

  public Station(LocationType type) {
    this.type = type;
  }

  public LocationType getType() {
    return type;
  }

  public void addLine(Line line) {
    lines.add(line);
  }

  public void removeLine(Line line) {
    lines.remove(line);
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
        .map(this::getLineRoutesMap)
        .map(LineRoutesMap.getting(destinationType))
        .flatMap(Collection::stream)
        .toList();
      return Route.getBest(routes);
    }

    void clear() {
      map.clear();
    }

    private LineRoutesMap getLineRoutesMap(Line line) {
      return Optional.ofNullable(map.get(line))
        .orElseGet(() -> {
          LineRoutesMap lineRoutesMap = new LineRoutesMap(line);
          map.put(line, lineRoutesMap);
          return lineRoutesMap;
        });
    }

    private class LineRoutesMap {
      private final Line line;
      private final Map<LocationType, List<Route>> map = new HashMap<>();

      LineRoutesMap(Line line) {
        this.line = line;
      }

      static Function<LineRoutesMap, List<Route>> getting(LocationType destinationType) {
        return (lineRoutesMap) -> lineRoutesMap.get(destinationType);
      }

      List<Route> get(LocationType destinationType) {
        return Optional.ofNullable(map.get(destinationType))
          .orElseGet(() -> {
            List<Route> routes = line.findRoutes(destinationType, Station.this).toList();
            map.put(destinationType, routes);
            return routes;
          });
      }
    }
  }
}
