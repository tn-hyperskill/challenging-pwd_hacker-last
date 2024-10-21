package hacker.models;

import java.util.regex.Pattern;

public record AuthResp(String message) {

  private static final Pattern JSON_RE_PATTERN =
      Pattern.compile(
          "^\\s*\\{\\s*\"result\"\\s*:\\s*\"(?<result>[^\"]*)\"\\s*\\}\\s*$");

  public static AuthResp fromJson(String json) {
    var matcher = JSON_RE_PATTERN.matcher(json);
    if (matcher.matches()) {
      return new AuthResp(matcher.group("result"));
    } else {
      throw new IllegalArgumentException("Invalid JSON: " + json);
    }
  }

  public String toJsonString() {
    return """
        {
          "result": "%s"
        }
        """.formatted(message);
  }
}
