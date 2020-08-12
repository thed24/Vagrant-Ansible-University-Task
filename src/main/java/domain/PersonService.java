package domain;

import java.util.Optional;
import java.util.Set;

public class PersonService {

  private final Repository database;

  public PersonService(Repository database) {
    this.database = database;
  }

  public boolean changePersonsName(Integer id, String name) {
    Optional<Person> person = database.getByID(id);
    if (person.isPresent() && !person.get().getIsOwner() && isNameAvailable(name, database.getAll())) {
      person.get().setName(name);
      database.update(person.get());
      return true;
    }
    return false;
  }

  public boolean addPerson(Person person) { //TODO: Exceptions / Result
    if (isNameAvailable(person.getName(), database.getAll())) {
      database.add(person);
      return true;
    }
    return false;
  }

  public boolean deletePerson(Integer id) {
    Optional<Person> person = database.getByID(id);
    if (person.isPresent() && !person.get().getIsOwner()) {
      database.delete(person.get());
      return true;
    }
    return false;
  }

  public Set<Person> getPeople() {
    return database.getAll();
  }

  public Optional<Person> getPerson(Integer id) {
    return database.getByID(id);
  }

  private static boolean isNameAvailable(String name, Set<Person> people) {
    return people.stream().noneMatch(currentPerson -> currentPerson.getName().equals(name));
  }
}
