package nanometro.model.service;

import nanometro.model.shared.LocationType;

import java.util.List;

public interface ModelService<StationId, LineId> {
  List<StationId> findDestinations(LocationType destinationType, LineId lindId, StationId stationId, StationId nextStationId);

  void addStation(StationId id, LocationType type);

  void addLine(LineId id);

  void updateLine(LineId id, List<StationId> stationIds);

  void reset();
}
