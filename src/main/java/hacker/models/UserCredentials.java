package hacker.models;

import java.util.regex.Pattern;

public record UserCredentials(String login, String password) {

  // Static fields

  public static final Pattern JSON_RE_PATTERN = Pattern.compile(
      "\\s*\\{\\s*\"login\"\\s*:\\s*\"(?<login>[^\"]*)\"\\s*,"
          + "\\s*\"password\"\\s*:\\s*\"(?<password>[^\"]*)\"\\s*\\}\\s*");

  // CRUD-C

  public static UserCredentials fromJson(String json) {
    var matcher = JSON_RE_PATTERN.matcher(json);
    if (matcher.matches()) {
      return new UserCredentials(matcher.group("login"),
          matcher.group("password"));
    } else {
      throw new IllegalArgumentException("Invalid JSON: " + json);
    }
  }

  // CRUD-C: Builder Lite Pattern

  public UserCredentials withLogin(String login) {
    return new UserCredentials(login, this.password);
  }

  public UserCredentials withPassword(String password) {
    return new UserCredentials(this.login, password);
  }

  // CRUD-R

  public String toJsonString() {
    return """
        {
          "login": "%s",
          "password": "%s"
        }
        """.formatted(login, password);
  }
}
