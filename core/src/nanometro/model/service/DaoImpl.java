package nanometro.model.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

class DaoImpl<Id, Entity> implements Dao<Id, Entity> {
  private final Map<Id, Entity> entityMap = new HashMap<>();
  private final Map<Entity, Id> idMap = new HashMap<>();

  DaoImpl() {
  }

  @Override
  public Entity get(Id id) {
    return Optional.ofNullable(entityMap.get(id))
      .orElseThrow(() -> new RuntimeException("id " + id + " does not exist"));
  }

  @Override
  public Id getId(Entity entity) {
    return Optional.ofNullable(idMap.get(entity))
      .orElseThrow(() -> new RuntimeException("entity " + entity + " does not exist"));
  }

  @Override
  public Stream<Entity> getAll() {
    return entityMap.values().stream();
  }

  @Override
  public Stream<Entity> getAll(Collection<Id> ids) {
    return ids.stream().map(this::get);
  }

  @Override
  public void add(Id id, Entity entity) {
    if (entityMap.containsKey(id)) {
      throw new RuntimeException("id " + id + " already exists");
    }
    if (idMap.containsKey(entity)) {
      throw new RuntimeException("entity " + entity + " already exists");
    }
    entityMap.put(id, entity);
    idMap.put(entity, id);
  }

  @Override
  public void deleteAll() {
    entityMap.clear();
    idMap.clear();
  }
}
