package hacker.models;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

final class UserCredentialsTest {


  @Test
  @DisplayName("`fromJson(toJsonString())` produces a clone")
  void fromJson() {
    var value = new AuthResp("Wrong login!");
    var json = value.toJsonString();
    var deserializedJson = AuthResp.fromJson(json);
    assertEquals(value, deserializedJson);
  }

  @Test
  void toJsonString() {
    var expectedLogin = "tomek";
    var expectedPassword = "hard_pwd";
    var req = new UserCredentials(expectedLogin, expectedPassword);

    var matcher = UserCredentials.JSON_RE_PATTERN.matcher(req.toJsonString());
    assertTrue(matcher.matches());
    assertAll(
        () -> assertEquals(expectedLogin, matcher.group("login")),
        () -> assertEquals(expectedPassword, matcher.group("password"))
    );
  }
}