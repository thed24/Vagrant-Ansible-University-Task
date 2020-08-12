package http;

import static http.TestUtilities.sendRequest;

import domain.Person;
import domain.PersonService;
import domain.Repository;
import http.app.GreetingController;
import http.support.HttpRequest;
import http.support.HttpResponse;
import http.support.Server;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GreetingControllerTests {

  @Rule
  public final ExpectedException httpError = ExpectedException.none();
  private Server testServer;
  private int port;
  private URLConnection con;

  @Before
  public void initialiseTests() throws IOException {
    port = ThreadLocalRandom.current().nextInt(8000, 9000);
    Repository repository = new RepositoryStub();
    PersonService personService = new PersonService(repository);
    testServer = new Server(personService, port, "localhost");
    testServer.start();
    con = new URL("http://localhost:" + port).openConnection();
    sendRequest(String.valueOf(port), new HttpRequest("/", "GET", null));
  }

  @After
  public void cleanUpTests() {
    testServer.stop();
  }

  @Test
  public void controllerRespondsWithGreetingFormattedForOnePersonWhenOnePersonIsStored() throws IOException {
    sendRequest(String.valueOf(port), new HttpRequest("/people/2", "DELETE", null));
    sendRequest(String.valueOf(port), new HttpRequest("/people/3", "DELETE", null));
    sendRequest(String.valueOf(port), new HttpRequest("/", "GET", null));
    Assert.assertTrue(TestUtilities.getPage(con).contains("Hello Dominic"));
  }

  @Test
  public void controllerRespondsWithGreetingFormattedForTwoPersonWhenTwoPeopleAreStored() throws IOException {
    sendRequest(String.valueOf(port), new HttpRequest("/people/3", "DELETE", null));
    sendRequest(String.valueOf(port), new HttpRequest("/", "GET", null));
    Assert.assertTrue(TestUtilities.getPage(con).contains("Hello Dominic and Jack"));
  }

  @Test
  public void controllerRespondsWithGreetingFormattedForThreePeopleWhenThreePeopleAreStored() throws IOException {
    sendRequest(String.valueOf(port), new HttpRequest("/", "GET", null));
    Assert.assertTrue(TestUtilities.getPage(con).contains("Hello Dominic, Jack, and Jill"));
  }

  @Test
  public void controllerFailsToAllowDeleteRequestFromRootEndpoint() throws IOException {
    expectHttpErrorCode();
    sendRequest(String.valueOf(port), new HttpRequest("/", "DELETE", null));
  }

  @Test
  public void controllerFailsToAllowPutRequestFromRootEndpoint() throws IOException {
    expectHttpErrorCode();
    sendRequest(String.valueOf(port), new HttpRequest("/", "PUT", null));
  }

  @Test
  public void controllerFailsToAllowPostRequestFromRootEndpoint() throws IOException {
    expectHttpErrorCode();
    sendRequest(String.valueOf(port), new HttpRequest("/", "POST", null));
  }

  @Test
  public void controllerFormatsGreetingForFourPeopleAndGeneratesStatus200() {
    Set<Person> people = new HashSet<>();
    people.add(new Person(1, "Dominic", true));
    people.add(new Person(2, "Anton", false));
    people.add(new Person(3, "Long", false));
    people.add(new Person(4, "Potato", false));

    HttpResponse expected = new HttpResponse("Hello Anton, Potato, Dominic, and Long", 200, "text/HTML");
    HttpResponse actual = GreetingController.formattedGetOutput(people);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void controllerFormatsGreetingForThreePeopleAndGeneratesStatus200() {
    Set<Person> people = new HashSet<>();
    people.add(new Person(1, "Dominic", true));
    people.add(new Person(2, "Anton", false));
    people.add(new Person(3, "Long", false));

    HttpResponse expected = new HttpResponse("Hello Anton, Dominic, and Long", 200, "text/HTML");
    HttpResponse actual = GreetingController.formattedGetOutput(people);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void controllerFormatsGreetingForTwoPeopleAndGeneratesStatus200() {
    Set<Person> people = new HashSet<>();
    people.add(new Person(1, "Dominic", true));
    people.add(new Person(2, "Anton", false));

    HttpResponse expected = new HttpResponse("Hello Anton and Dominic", 200, "text/HTML");
    HttpResponse actual = GreetingController.formattedGetOutput(people);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void controllerFormatsGreetingForOnePersonAndGeneratesStatus200() {
    Set<Person> people = new HashSet<>();
    people.add(new Person(1, "Dominic", true));

    HttpResponse expected = new HttpResponse("Hello Dominic", 200, "text/HTML");
    HttpResponse actual = GreetingController.formattedGetOutput(people);

    Assert.assertEquals(expected, actual);
  }

  private void expectHttpErrorCode() {
    httpError.expect(IOException.class);
    httpError.expectMessage("Server returned HTTP response code: 405 for URL:");
  }
}
