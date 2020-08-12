package http.app;

import domain.Person;
import domain.PersonService;
import http.support.HttpRequest;
import http.support.HttpResponse;
import org.json.JSONObject;

public class PersonUpdateController implements Controller {

  private final PersonService personService;

  public PersonUpdateController(PersonService personService) {
    this.personService = personService;
  }

  @Override
  public HttpResponse handle(HttpRequest request) {
    if (HttpRequest.areParametersEmpty(request, 1) || HttpRequest.isPathEmpty(request) || request.isPathValueInvalid()) {
      return failResponse();
    }

    Integer personID = Integer.valueOf(request.getIdFromPath());
    return personService.changePersonsName(personID, request.getRequestParameters().get("Name"))
        ? successResponse(personID)
        : failResponse();
  }

  private HttpResponse failResponse() {
    String message = "Error, the person you are attempting to update to is invalid!";
    return new HttpResponse(message, 403, "text/html");
  }

  private HttpResponse successResponse(Integer personId) {
    Person person = personService.getPerson(personId).get();
    JSONObject personAsJson = person.toJSON();
    return new HttpResponse(personAsJson.toString(), 200, "application/json");
  }
}
