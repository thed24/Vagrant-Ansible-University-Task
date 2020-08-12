package http.app;

import domain.PersonService;
import http.support.HttpRequest;
import http.support.HttpResponse;
import java.io.IOException;
import java.util.Map;

public class PersonController implements Controller {

  private final Map<String, Controller> requestController;

  public PersonController(PersonService personService) {
    requestController = Map
        .of("PUT", new PersonUpdateController(personService),
            "GET", new PersonReadController(personService),
            "POST", new PersonCreateController(personService),
            "DELETE", new PersonDeleteController(personService));
  }

  @Override
  public HttpResponse handle(HttpRequest request) throws IOException {
    if (requestController.containsKey(request.getRequestMethod())) {
      return requestController.get(request.getRequestMethod()).handle(request);
    }
    return new HttpResponse("Unsupported request type!", 405, "text/html");
  }
}
