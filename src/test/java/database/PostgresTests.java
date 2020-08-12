package database;

import com.opentable.db.postgres.junit.EmbeddedPostgresRules;
import com.opentable.db.postgres.junit.SingleInstancePostgresRule;
import domain.Person;
import java.util.Set;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

public class PostgresTests {

  @ClassRule
  public static final SingleInstancePostgresRule db = EmbeddedPostgresRules.singleInstance();
  @Rule
  public final EnvironmentVariables environmentVariables = new EnvironmentVariables();
  private HibernatePersonRepository database;

  @Before
  public void setupDatabase() {
    environmentVariables.set("DB_HOST", "localhost");
    environmentVariables.set("DB_PORT", String.valueOf(db.getEmbeddedPostgres().getPort()));
    environmentVariables.set("DB_USER", "postgres");
    environmentVariables.set("DB_PASSWORD", "postgres");
    environmentVariables.set("DB_NAME", "postgres");
    database = new HibernatePersonRepository();
  }

  @After
  public void teardownDatabase() {
    Set<Person> allInDatabase = database.getAll();
    for (Person person : allInDatabase) {
      database.delete(person);
    }
  }

  @Test
  public void databaseInsertsRootUserOnConnection() {
    Person expected = new Person(1, "Dominic", true);

    Assert.assertEquals(expected, database.getAll().iterator().next());
  }

  @Test
  public void databaseHoldingNothingReturnsPersonAfterPersonIsAdded() {
    Person expected = new Person(2, "Jack", false);

    database.add(expected);

    Assert.assertTrue(database.getAll().contains(expected));
  }

  @Test
  public void databaseHoldingNameWithSpaceReturnsNameWithSpaceIntact() {
    Person expected = new Person(2, "Jack Jill", false);

    database.add(expected);

    Assert.assertTrue(database.getAll().contains(expected));
  }

  @Test
  public void databaseHoldingTwoPeopleReturnsOnePersonAfterOtherIsRemoved() {
    Person expected = new Person(2, "Jack", false);
    Person personToRemove = new Person(3, "Jill", false);

    database.add(expected);
    database.add(personToRemove);
    database.delete(personToRemove);

    Set<Person> actual = database.getAll();
    Assert.assertTrue(actual.contains(expected));
  }

  @Test
  public void databaseHoldingOnePersonReturnsNewPersonAfterChange() {
    Person expected = new Person(1, "Jack", false);

    database.add(expected);
    expected.setName("jill");
    database.update(expected);

    Set<Person> actual = database.getAll();
    Assert.assertTrue(actual.contains(expected));
  }
}
