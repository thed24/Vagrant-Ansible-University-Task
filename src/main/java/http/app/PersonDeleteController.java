package http.app;

import domain.Person;
import domain.PersonService;
import http.support.HttpRequest;
import http.support.HttpResponse;
import java.util.Optional;
import org.json.JSONObject;

public class PersonDeleteController implements Controller {

  private final PersonService personService;

  public PersonDeleteController(PersonService personService) {
    this.personService = personService;
  }

  @Override
  public HttpResponse handle(HttpRequest request) {
    if (HttpRequest.isPathEmpty(request) || request.isPathValueInvalid()) {
      return failResponse();
    }

    Integer personID = Integer.valueOf(request.getIdFromPath());
    Optional<Person> person = personService.getPerson(personID);
    if (person.isEmpty()) {
      return failResponse();
    }

    return personService.deletePerson(personID)
        ? successResponse(person.get())
        : failResponse();
  }

  private HttpResponse failResponse() {
    String message = "Error, the person you are attempting to delete is invalid!";
    return new HttpResponse(message, 403, "text/html");
  }

  private HttpResponse successResponse(Person person) {
    JSONObject personAsJson = person.toJSON();
    return new HttpResponse(personAsJson.toString(), 200, "application/json");
  }
}
