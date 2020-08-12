package http.support;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HttpRequest {

  private final String requestMethod;
  private final String requestPath;
  private final Map<String, String> requestParameters;

  public HttpRequest(String path, String method, Map<String, String> parameters) {
    requestMethod = method;
    requestPath = path;
    requestParameters = parameters;
  }

  HttpRequest(HttpExchange exchange) throws IOException {
    String requestKeyValuePair = new String(exchange.getRequestBody().readAllBytes());
    if (requestKeyValuePair.isEmpty()) {
      requestKeyValuePair = " = ";
    }

    String requestKey = requestKeyValuePair.split("=").length == 0 ? " " : requestKeyValuePair.split("=")[0];
    String requestValue = requestKeyValuePair.split("=").length == 1 ? " " : requestKeyValuePair.split("=")[1];

    requestPath = exchange.getRequestURI().getPath();
    requestMethod = exchange.getRequestMethod();
    requestParameters = Map.of(requestKey, requestValue);
  }

  public static boolean areParametersEmpty(HttpRequest request, int parameterAmount) {
    Iterator<Entry<String, String>> iterator = request.getRequestParameters().entrySet().iterator();
    for (int i = 0; i < parameterAmount; i++) {
      Entry<String, String> entry = iterator.hasNext() ? iterator.next() : null;
      if (entry == null || entry.getKey().isEmpty()) {
        return true;
      }
    }
    return false;
  }

  public static boolean isPathEmpty(HttpRequest request) {
    if (request.getRequestPath().chars().filter(character -> character == '/').count() == 2) {
      if (request.getRequestPath().split("/").length == 3) {
        return (request.getRequestPath().split("/").length == 0);
      }
      return true;
    }
    return true;
  }

  public Map<String, String> getRequestParameters() {
    return requestParameters;
  }

  public String getRequestMethod() {
    return requestMethod;
  }

  public String getRequestPath() {
    return requestPath;
  }

  public String getIdFromPath() {
    return getRequestPath().split("/")[2];
  }

  public boolean isPathValueInvalid() {
    return !getIdFromPath().matches("-?\\d+(\\.\\d+)?");
  }
}
