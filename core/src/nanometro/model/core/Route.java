package nanometro.model.core;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public record Route(
  Station next,
  Station last,
  int length,
  int transfer
) {
  private static final List<Comparator<Route>> comparators = List.of(
    Comparator.comparingInt(Route::transfer)
      .thenComparingInt(Route::length),
    Comparator.comparingInt(Route::length)
      .thenComparingInt(Route::transfer)
  );

  public static <T> Predicate<Route> equalingTo(T target, Function<Route, T> keyExtractor) {
    return (route) -> keyExtractor.apply(route).equals(target);
  }

  static Predicate<Route> equalingTo(Route target, Comparator<Route> comparator) {
    return (route) -> route == target || comparator.compare(route, target) == 0;
  }

  static Stream<Route> getBest(Collection<Route> routes) {
    return comparators.stream()
      .flatMap((comparator) -> getBest(routes, comparator))
      .distinct();
  }

  private static Stream<Route> getBest(Collection<Route> routes, Comparator<Route> comparator) {
    Route bestRoute = routes.stream().min(comparator).orElse(null);
    if (bestRoute == null) {
      return Stream.empty();
    }
    return routes.stream()
      .filter(Route.equalingTo(bestRoute, comparator));
  }
}
