package http;

import http.support.HttpRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

class TestUtilities {

  static void sendRequest(String port, HttpRequest request) throws IOException {
    URLConnection urlConnection = new URL("http://localhost:" + port + request.getRequestPath()).openConnection();
    HttpURLConnection connectionToServer = (HttpURLConnection) urlConnection;
    connectionToServer.setRequestMethod(request.getRequestMethod());
    if (!request.getRequestMethod().equals("GET")) {
      connectionToServer.setDoOutput(true);
      connectionToServer.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      OutputStream outputStream = connectionToServer.getOutputStream();
      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
      outputStreamWriter.flush();
      outputStreamWriter.close();
    }
    connectionToServer.getInputStream();
    connectionToServer.connect();
  }

  static String getPage(URLConnection con) throws IOException {
    return new BufferedReader(new InputStreamReader(con.getInputStream())).lines().collect(Collectors.joining("\n"));
  }
}
