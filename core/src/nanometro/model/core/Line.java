package nanometro.model.core;

import nanometro.model.shared.LocationType;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class Line {
  private static final List<Comparator<Route>> routeComparators = List.of(
    Comparator.comparingInt(Route::transfer),
    Comparator.comparingInt(Route::length)
  );

  private final Map<Station, StationNode> nodeMap = new HashMap<>();
  private boolean isFindingRoutes;

  public Line() {
  }

  private static Stream<Route> getBestRoutes(Collection<Route> routes) {
    return routeComparators.stream()
      .flatMap((routeComparator) -> Route.getBest(routes, routeComparator))
      .distinct();
  }

  public void update(List<Station> stations) {
    if (stations.isEmpty()) {
      for (Station station : nodeMap.keySet()) {
        station.removeLine(Line.this);
      }
      nodeMap.clear();
      return;
    }
    if (!nodeMap.isEmpty()) {
      Set<Station> staleStations = new HashSet<>(nodeMap.keySet());
      for (Station station : stations) {
        staleStations.remove(station);
      }
      for (Station station : staleStations) {
        nodeMap.remove(station);
        station.removeLine(Line.this);
      }
    }
    StationNode sentinel = new StationNode(null);
    StationNode lastNode = sentinel;
    for (Station station : stations) {
      StationNode node = Optional.ofNullable(nodeMap.get(station))
        .orElseGet(() -> {
          StationNode newNode = new StationNode(station);
          nodeMap.put(station, newNode);
          station.addLine(Line.this);
          return newNode;
        });
      node.left = lastNode;
      node.right = null;
      lastNode.right = node;
      lastNode = node;
    }
    StationNode firstNode = sentinel.right;
    firstNode.left = null;
  }

  boolean isFindingRoutes() {
    return isFindingRoutes;
  }

  Stream<Route> findRoutes(LocationType destinationType, Station station) {
    isFindingRoutes = true;
    Stream<Route> routeStream = Stream.concat(
      findRoutes(destinationType, station, StationNode::getLeft),
      findRoutes(destinationType, station, StationNode::getRight)
    );
    isFindingRoutes = false;
    return getBestRoutes(routeStream.toList());
  }

  private Stream<Route> findRoutes(LocationType destinationType, Station station, UnaryOperator<StationNode> successor) {
    StationNode routeStartNode = Optional.ofNullable(nodeMap.get(station))
      .orElseThrow(() -> new RuntimeException("station " + station + " is not on this line"));
    StationNode routeNextNode = successor.apply(routeStartNode);
    if (routeNextNode == null) {
      return Stream.empty();
    }
    List<Route> routes = new ArrayList<>();
    StationNodeIterator nodeIterator = new StationNodeIterator(routeNextNode, successor);
    while (nodeIterator.hasNext()) {
      int nodeIndex = nodeIterator.nextIndex();
      StationNode node = nodeIterator.next();
      if (node.station.getType().equals(destinationType)) {
        Route route = new Route(routeNextNode.station, node.station, nodeIndex + 1, 0);
        routes.add(route);
        break;
      } else {
        node.station.getRoutes(destinationType)
          .forEach((transferRoute) -> {
            int length = transferRoute.length() + nodeIndex + 1;
            int transfer = transferRoute.transfer() + 1;
            Route route = new Route(routeNextNode.station, node.station, length, transfer);
            routes.add(route);
          });
      }
    }
    return getBestRoutes(routes);
  }

  private static class StationNode {
    final Station station;
    StationNode left;
    StationNode right;

    StationNode(Station station) {
      this.station = station;
    }

    StationNode getLeft() {
      return left;
    }

    StationNode getRight() {
      return right;
    }
  }

  private static class StationNodeIterator implements Iterator<StationNode> {
    private final UnaryOperator<StationNode> successor;
    private StationNode next;
    private int nextIndex;

    StationNodeIterator(StationNode next, UnaryOperator<StationNode> successor) {
      this.successor = successor;
      this.next = next;
    }

    int nextIndex() {
      return nextIndex;
    }

    @Override
    public boolean hasNext() {
      return next != null;
    }

    @Override
    public StationNode next() {
      StationNode node = next;
      next = successor.apply(next);
      nextIndex++;
      return node;
    }
  }
}
