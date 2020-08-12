package domain;

import java.util.Optional;
import java.util.Set;

public interface Repository {

  Set<Person> getAll();

  Optional<Person> getByID(Integer id);

  void delete(Person person);

  void update(Person person);

  void add(Person person);
}
