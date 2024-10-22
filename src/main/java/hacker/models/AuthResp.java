package hacker.models;

import java.util.regex.Pattern;

public record AuthResp(String message) {

  // Static fields

  public static final Pattern JSON_RE_PATTERN =
      Pattern.compile(
          "^\\s*\\{\\s*\"result\"\\s*:\\s*\"(?<result>[^\"]*)\"\\s*\\}\\s*$");

  // CRUD-C

  public static AuthResp fromJson(String json) {
    var matcher = JSON_RE_PATTERN.matcher(json);
    if (matcher.matches()) {
      return new AuthResp(matcher.group("result"));
    } else {
      throw new IllegalArgumentException("Invalid JSON: " + json);
    }
  }

  // CRUD-R

  public String toJsonString() {
    return """
        {
          "result": "%s"
        }
        """.formatted(message);
  }
}
