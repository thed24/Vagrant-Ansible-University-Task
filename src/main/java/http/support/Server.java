package http.support;

import com.sun.net.httpserver.HttpServer;
import domain.PersonService;
import http.app.Controller;
import http.app.GreetingController;
import http.app.PersonController;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

public class Server {

  private HttpServer httpServer;
  //todo: look into passing in http server
  public Server(PersonService personDatabase, int port, String hostname) {
    try {
      httpServer = HttpServer.create(new InetSocketAddress(hostname, port), 0);
      Map<String, Controller> singletonMap = Map
          .of("/", new GreetingController(personDatabase),
              "/people", new PersonController(personDatabase));
      setupRoutes(singletonMap);
      httpServer.setExecutor(null);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void start() {
    this.httpServer.start();
  }

  public void stop() {
    this.httpServer.stop(0);
  }

  private void setupRoutes(Map<String, Controller> routes) {
    for (Map.Entry<String, Controller> route : routes.entrySet()) {
      httpServer.createContext(route.getKey(), new ControllerHandlerAdapter(route.getValue()));
    }
  }
}
