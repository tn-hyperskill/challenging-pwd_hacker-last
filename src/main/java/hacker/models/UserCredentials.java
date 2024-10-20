package hacker.models;

public record UserCredentials(String login, String password) {

  public String toJsonString() {
    return """
        {
          "login": "%s",
          "password": "%s"
        }
        """.formatted(login, password);
  }

  // CRUD-C: Builder Lite Pattern

  public UserCredentials withLogin(String login) {
    return new UserCredentials(login, this.password);
  }

  public UserCredentials withPassword(String password) {
    return new UserCredentials(this.login, password);
  }
}
