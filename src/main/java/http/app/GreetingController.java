package http.app;

import static java.nio.file.Files.readString;

import domain.Person;
import domain.PersonService;
import http.support.HttpRequest;
import http.support.HttpResponse;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public class GreetingController implements Controller {

  private static final String MELBOURNE_TIMEZONE = "Australia/Sydney";
  private final PersonService personService;

  public GreetingController(PersonService personService) {
    this.personService = personService;
  }

  private static String[] getDateAndTime() {
    ZonedDateTime melbourneTime = ZonedDateTime.now(ZoneId.of(MELBOURNE_TIMEZONE));
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MM yyyy");
    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a");
    return new String[] {melbourneTime.format(dateFormat), melbourneTime.format(timeFormat)};
  }

  private static StringBuilder longGetStringBuilder(Set<Person> people) {
    Iterator<String> iterator = people.stream().map(Person::getName).collect(Collectors.toSet()).iterator();
    StringBuilder moreThanThreePeople = new StringBuilder();
    for (int i = 0; i < people.size() - 1; i++) {
      moreThanThreePeople.append(iterator.next()).append(", ");
    }
    moreThanThreePeople.append("and ").append(iterator.next());
    return moreThanThreePeople;
  }

  public static HttpResponse formattedGetOutput(Set<Person> people) {
    Iterator<String> iterator = people.stream().map(Person::getName).collect(Collectors.toSet()).iterator();
    String message = "Hello {{names}} - the time on the server is " + getDateAndTime()[0] + " on " + getDateAndTime()[1];
    String target = "{{names}}";
    switch (people.size()) {
      case 1:
        message = message.replace(target, iterator.next());
        break;
      case 2:
        message = message.replace(target, iterator.next() + " and " + iterator.next());
        break;
      default:
        message = message.replace(target, longGetStringBuilder(people).toString());
    }
    return new HttpResponse(message, 200, "text/html");
  }

  @Override
  public HttpResponse handle(HttpRequest request) throws IOException {
    HttpResponse httpResponse = translateRequestToQuery(request);
    String output = readString(Paths.get("src/Resources/Index.html"));
    output = output.replace("{{Title}}", "Hello World");
    output = output.replace("{{Body}}", httpResponse.getMessage());
    httpResponse.setMessage(output);
    return httpResponse;
  } //TODO: Seperate into two areas

  private HttpResponse translateRequestToQuery(HttpRequest request) {
    String requestMethod = request.getRequestMethod();
    if ("GET".equals(requestMethod)) {
      return formattedGetOutput(personService.getPeople());
    } else {
      return new HttpResponse("Unsupported request type!", 405, "text/html");
    }
  }
}
