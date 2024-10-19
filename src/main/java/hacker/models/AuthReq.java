package hacker.models;

public record AuthReq(String login, String password) {

  public String toJsonString() {
    return """
        {
          "login": "%s",
          "password": "%s"
        }
        """.formatted(login, password);
  }
}
