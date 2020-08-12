package http.app;

import domain.Person;
import domain.PersonService;
import http.support.HttpRequest;
import http.support.HttpResponse;
import java.util.Optional;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

public class PersonReadController implements Controller {

  private final PersonService personService;

  public PersonReadController(PersonService personService) {
    this.personService = personService;
  }

  @Override
  public HttpResponse handle(HttpRequest request) {
    if (HttpRequest.isPathEmpty(request)) {
      return successResponse();
    }

    if (request.isPathValueInvalid()){
      return failResponse();
    }

    Optional<Person> person = personService.getPerson(Integer.valueOf(request.getIdFromPath()));
    return person.map(value -> successResponse(value.getId())).orElseGet(this::failResponse);
  }

  private HttpResponse failResponse() { //TODO: responses on interface
    String message = "Error, the person you are attempting to get is invalid!";
    return new HttpResponse(message, 404, "text/html");
  }

  private HttpResponse successResponse() {
    JSONArray allPeople = new JSONArray();
    Set<Person> namesOfPeople = personService.getPeople();
    for (Person namesOfPerson : namesOfPeople) {
      allPeople.put(namesOfPerson.toJSON());
    }
    return new HttpResponse(allPeople.toString(), 200, "application/json");
  }

  private HttpResponse successResponse(Integer id) {
    Person person = personService.getPerson(id).get();
    JSONObject personAsJson = person.toJSON();
    return new HttpResponse(personAsJson.toString(), 200, "application/json");
  }
}
