package domain;

import http.RepositoryStub;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;

public class PersonServiceTests {

  private final PersonService personService = new PersonService(new RepositoryStub());

  @Test
  public void serviceSuccessfullyAddsPersonWhenPersonIsNotInWorld() {
    Person jeremy = new Person(4, "Jeremy", false);
    boolean actual = personService.addPerson(jeremy);
    Assert.assertTrue(actual);
  }

  @Test
  public void serviceSuccessfullyGetsPersonWhoExistsOnWorld() {
    Person expected = new Person(2, "Jack", false);
    Optional<Person> actual = personService.getPerson(2);
    Assert.assertEquals(expected, actual.orElse(null));
  }

  @Test
  public void serviceSuccessfullyDeletesPersonWhoIsInWorld() {
    boolean actual = personService.deletePerson(2);
    Assert.assertTrue(actual);
  }

  @Test
  public void serviceSuccessfullyChangesPersonWhenNewPersonIsNotInWorld() {
    boolean actual = personService.changePersonsName(2, "Jacky");
    Assert.assertTrue(actual);
  }

  @Test
  public void serviceFailsToGetPersonWhenPersonIsNotInWorld() {
    Optional<Person> actual = personService.getPerson(12);
    Assert.assertFalse(actual.isPresent());
  }

  @Test
  public void serviceFailsToAddsPersonWhenPersonIsInWorld() {
    Person jack = new Person(1, "Jack", false);
    boolean actual = personService.addPerson(jack);
    Assert.assertFalse(actual);
  }

  @Test
  public void serviceFailsToChangePersonWhenNewPersonIsInWorld() {
    boolean actual = personService.changePersonsName(3, "Jill");
    Assert.assertFalse(actual);
  }

  @Test
  public void serviceFailsToChangePersonWhoIsNotInWorld() {
    boolean actual = personService.changePersonsName(0, "Jacky");
    Assert.assertFalse(actual);
  }

  @Test
  public void serviceFailsToDeletePersonWhenPersonIsRootUser() {
    boolean actual = personService.deletePerson(1);
    Assert.assertFalse(actual);
  }

  @Test
  public void serviceFailsToDeletePersonWhenPersonIsRootUserWithDifferentName() {
    boolean actual = personService.deletePerson(1);
    Assert.assertFalse(actual);
  }

  @Test
  public void serviceFailsToDeletePersonWhenPersonDoesntExist() {
    boolean actual = personService.deletePerson(0);
    Assert.assertFalse(actual);
  }
}
