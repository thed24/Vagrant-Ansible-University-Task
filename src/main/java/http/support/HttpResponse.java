package http.support;

public class HttpResponse {

  private final int statusCode;
  private final String contentType;
  private String message;

  public HttpResponse(String message, int statusCode, String contentType) {
    this.message = message;
    this.statusCode = statusCode;
    this.contentType = contentType;
  }

  int getStatusCode() {
    return statusCode;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  String getContentType() {
    return contentType;
  }

  @Override
  public int hashCode() {
    return 31 * statusCode + message.hashCode();
  }

  @Override
  public boolean equals(Object givenResponse) {

    if (givenResponse == this) {
      return true;
    }

    if (!(givenResponse instanceof HttpResponse)) {
      return false;
    }

    HttpResponse httpResponse = (HttpResponse) givenResponse;
    return this.statusCode == httpResponse.statusCode &&
        this.message.split("-")[0].trim().equals(httpResponse.getMessage().split("-")[0].trim());
  }
}
