package http;

import domain.Person;
import domain.PersonService;
import http.app.PersonController;
import http.app.PersonCreateController;
import http.app.PersonDeleteController;
import http.app.PersonReadController;
import http.app.PersonUpdateController;
import http.support.HttpRequest;
import http.support.HttpResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class PersonControllerTests {

  private final PersonService personService = new PersonService(new RepositoryStub());

  @Test
  public void handlerMapsGetPersonWhenPathContainsIDNotPresentOnWorld() {
    PersonReadController getController = new PersonReadController(personService);
    Map<String, String> requestParameters = Map.of("", "");
    HttpRequest httpRequest = new HttpRequest("/persons/12", "GET", requestParameters);

    String message = "Error, the person you are attempting to get is invalid!";
    HttpResponse actual = getController.handle(httpRequest);
    HttpResponse expected = new HttpResponse(message, 404, "application/json");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void handlerMapsAddPersonWhenRequestIsEmpty() {
    PersonCreateController postController = new PersonCreateController(personService);
    Map<String, String> requestParameters = Map.of("", "");
    HttpRequest httpRequest = new HttpRequest("/people", "POST", requestParameters);

    String message = "Error, the person you are attempting to add is invalid!";
    HttpResponse actual = postController.handle(httpRequest);
    HttpResponse expected = new HttpResponse(message, 403, "application/json");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void handlerMapsDeletePersonWhenPathIsEmpty() {
    PersonDeleteController deleteController = new PersonDeleteController(personService);
    Map<String, String> requestParameters = Map.of("", "");
    HttpRequest httpRequest = new HttpRequest("/people", "DELETE", requestParameters);

    String message = "Error, the person you are attempting to delete is invalid!";
    HttpResponse actual = deleteController.handle(httpRequest);
    HttpResponse expected = new HttpResponse(message, 403, "application/json");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void handlerMapsDeletePersonWhenPathContainsIDNotPresentOnWorld() {
    PersonDeleteController deleteController = new PersonDeleteController(personService);
    Map<String, String> requestParameters = Map.of("", "");
    HttpRequest httpRequest = new HttpRequest("/people/23", "DELETE", requestParameters);

    String message = "Error, the person you are attempting to delete is invalid!";
    HttpResponse actual = deleteController.handle(httpRequest);
    HttpResponse expected = new HttpResponse(message, 403, "application/json");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void handlerMapsChangePersonWhenFirstHalfOfRequestIsHalfEmpty() { //TODO: update test name to reflect actual test
    PersonUpdateController putController = new PersonUpdateController(personService);
    Map<String, String> requestParameters = Map.of("2", "");
    HttpRequest httpRequest = new HttpRequest("/people", "PUT", requestParameters);

    String message = "Error, the person you are attempting to update to is invalid!";
    HttpResponse actual = putController.handle(httpRequest);
    HttpResponse expected = new HttpResponse(message, 403, "application/json");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void handlerMapsChangePersonWhenPathAndRequestIsEmpty() {
    PersonUpdateController putController = new PersonUpdateController(personService);
    Map<String, String> requestParameters = Map.of("", "");
    HttpRequest httpRequest = new HttpRequest("/people", "PUT", requestParameters);

    String message = "Error, the person you are attempting to update to is invalid!";
    HttpResponse actual = putController.handle(httpRequest);
    HttpResponse expected = new HttpResponse(message, 403, "application/json");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void handlerMapsGetRequestToServiceGetAllFunction() {
    PersonReadController getController = new PersonReadController(personService);
    Map<String, String> requestParameters = Map.of("", "");
    HttpRequest httpRequest = new HttpRequest("/people", "GET", requestParameters);

    JSONObject actualAsJSON = (JSONObject) new JSONArray(getController.handle(httpRequest).getMessage()).get(2);
    Person actual = new Person(actualAsJSON.getInt("ID"), actualAsJSON.getString("Name"),
        actualAsJSON.getBoolean("IsOwner"));
    Person expected = personService.getPerson(3).orElse(null);

    Assert.assertEquals(actual, expected);
  }

  @Test
  public void handlerMapsGetRequestToServiceGetSpecificWhenPathIsProvided() {
    PersonReadController getController = new PersonReadController(personService);
    Map<String, String> requestParameters = Map.of("", "");
    HttpRequest httpRequest = new HttpRequest("/persons/1", "GET", requestParameters);

    String message = Objects.requireNonNull(personService.getPerson(1).orElse(null)).toJSON().toString();
    HttpResponse actual = getController.handle(httpRequest);
    HttpResponse expected = new HttpResponse(message, 200, "application/json");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void handlerMapsGetRequestToServiceGetAllWhenPathIsProvidedWithNoID() {
    PersonReadController getController = new PersonReadController(personService);
    Map<String, String> requestParameters = Map.of("", "");
    HttpRequest httpRequest = new HttpRequest("/people/", "GET", requestParameters);

    JSONObject actualAsJSON = (JSONObject) new JSONArray(getController.handle(httpRequest).getMessage()).get(2);
    Person actual = new Person(actualAsJSON.getInt("ID"), actualAsJSON.getString("Name"),
        actualAsJSON.getBoolean("IsOwner"));
    Person expected = personService.getPerson(3).orElse(null);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void handlerMapsPostRequestToServiceAddFunction() {
    PersonCreateController postController = new PersonCreateController(personService);
    Map<String, String> requestParameters = Map.of("Name", "Jacky");
    HttpRequest httpRequest = new HttpRequest("/people", "POST", requestParameters);

    String message = new Person(4, "Jacky", false).toJSON().toString();
    HttpResponse actual = postController.handle(httpRequest);
    HttpResponse expected = new HttpResponse(message, 201, "application/json");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void handlerMapsDeleteRequestToServiceDeleteFunction() {
    PersonDeleteController deleteController = new PersonDeleteController(personService);
    Map<String, String> requestParameters = Map.of("", "");
    Person person = personService.getPerson(2).orElse(null);
    HttpRequest httpRequest = new HttpRequest("/people/2", "DELETE", requestParameters);

    String message = Objects.requireNonNull(person).toJSON().toString();
    HttpResponse actual = deleteController.handle(httpRequest);
    HttpResponse expected = new HttpResponse(message, 200, "application/json");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void handlerMapsPutRequestToServiceChangeFunction() {
    PersonUpdateController putController = new PersonUpdateController(personService);
    Map<String, String> requestParameters = Map.of("Name", "Jacky");
    HttpRequest httpRequest = new HttpRequest("/people/2", "PUT", requestParameters);

    HttpResponse actual = putController.handle(httpRequest);
    String message = Objects.requireNonNull(personService.getPerson(2).orElse(null)).toJSON().toString();
    HttpResponse expected = new HttpResponse(message, 200, "application/json");

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void handlerFiresOffFallbackResponseForUnsupportedHttpRequests() throws IOException {
    PersonController putController = new PersonController(personService);
    Map<String, String> requestParameters = Map.of("Name", "Jacky");
    HttpRequest httpRequest = new HttpRequest("/people/2", "PATCH", requestParameters);

    HttpResponse actual = putController.handle(httpRequest);
    String message = "Unsupported request type!";
    HttpResponse expected = new HttpResponse(message, 405, "text/html");

    Assert.assertEquals(expected, actual);
  }
}
