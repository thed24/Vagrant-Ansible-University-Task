package http.support;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.app.Controller;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

class ControllerHandlerAdapter implements HttpHandler {

  private final Controller controller;

  ControllerHandlerAdapter(Controller controller) {
    this.controller = controller;
  }

  private static void writeOutput(HttpExchange exchange, HttpResponse httpResponse) {
    try {
      exchange.getResponseHeaders().add("Content-Type", httpResponse.getContentType());
      exchange.sendResponseHeaders(httpResponse.getStatusCode(),
          httpResponse.getMessage().getBytes(Charset.defaultCharset()).length);
      OutputStream outputStream = exchange.getResponseBody();
      outputStream.write(httpResponse.getMessage().getBytes());
      outputStream.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    } //todo: close output stream in finally, use alternate exceptions
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    HttpRequest httpRequest = new HttpRequest(exchange);
    HttpResponse httpResponse = controller.handle(httpRequest);
    writeOutput(exchange, httpResponse);
  }
}
