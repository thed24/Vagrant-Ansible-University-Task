package http.app;

import domain.Person;
import domain.PersonService;
import http.support.HttpRequest;
import http.support.HttpResponse;
import org.json.JSONObject;

public class PersonCreateController implements Controller {

  private final PersonService personService;

  public PersonCreateController(PersonService personService) {
    this.personService = personService;
  }

  @Override
  public HttpResponse handle(HttpRequest request) {
    if (HttpRequest.areParametersEmpty(request, 1)) {
      return failResponse();
    }
    //TODO: look into changing constructor
    Person person = new Person(1, request.getRequestParameters().get("Name"), false);
    return personService.addPerson(person)
        ? successResponse(person)
        : failResponse();
  }

  private HttpResponse failResponse() {
    String message = "Error, the person you are attempting to add is invalid!";
    return new HttpResponse(message, 403, "text/html");
  }

  private HttpResponse successResponse(Person person) {
    JSONObject personAsJson = person.toJSON();
    return new HttpResponse(personAsJson.toString(), 201, "application/json");
  }
}
