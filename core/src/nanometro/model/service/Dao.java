package nanometro.model.service;

import java.util.Collection;
import java.util.stream.Stream;

interface Dao<Id, Entity> {
  Entity get(Id id);

  Id getId(Entity entity);

  Stream<Entity> getAll();

  Stream<Entity> getAll(Collection<Id> ids);

  void add(Id id, Entity entity);

  void deleteAll();
}
