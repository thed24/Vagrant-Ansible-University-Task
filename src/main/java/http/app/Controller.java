package http.app;

import http.support.HttpRequest;
import http.support.HttpResponse;
import java.io.IOException;

public interface Controller {

  HttpResponse handle(HttpRequest request) throws IOException;
}
