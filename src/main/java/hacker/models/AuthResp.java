package hacker.models;

import java.util.regex.Pattern;

public record AuthResp(String result) {

  private static final Pattern jsonRePattern =
      Pattern.compile(
          "^\\s*\\{\\s*\"result\"\\s*:\\s*\"(?<result>[^\"]*)\"\\s*\\}\\s*$");

  public static AuthResp fromJson(String json) {
    var matcher = jsonRePattern.matcher(json);
    if (matcher.matches()) {
      return new AuthResp(matcher.group("result"));
    }else{
      throw new IllegalArgumentException("Invalid JSON: " + json);
    }
  }
}
