package http;

import domain.Person;
import domain.Repository;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class RepositoryStub implements Repository {

  private Set<Person> people = new LinkedHashSet<>();
  private int lastID;

  public RepositoryStub() {
    people.add(new Person(1, "Dominic", true));
    people.add(new Person(2, "Jack", false));
    people.add(new Person(3, "Jill", false));
    lastID = people.size();
  }

  @Override
  public Set<Person> getAll() {
    return people;
  }

  @Override
  public void delete(Person person) {
    Set<Person> set = new HashSet<>();
    for (Person currPerson : people) {
      if (!currPerson.getId().equals(person.getId())) {
        set.add(currPerson);
      }
    }
    people = set;
  }

  @Override
  public void update(Person person) {
    Set<Person> set = new HashSet<>();
    for (Person currPerson : people) {
      if (currPerson.getId().equals(person.getId())) {
        currPerson.setName(person.getName());
      }
      set.add(currPerson);
    }
    people = set;
  }

  @Override
  public void add(Person person) {
    lastID++;
    person.setId(lastID);
    people.add(person);
  }

  @Override
  public Optional<Person> getByID(Integer id) {
    return people.stream().filter(person -> person.getId().equals(id)).findAny();
  }
}
