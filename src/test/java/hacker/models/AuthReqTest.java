package hacker.models;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

final class AuthReqTest {

  @Test
  void toJsonString() {
    var expectedLogin = "tomek";
    var expectedPassword = "hard_pwd";
    var req = new AuthReq(expectedLogin, expectedPassword);

    final var expectedJsonStr = Pattern.compile(
        "\\s*\\{\\s*\"login\"\\s*:\\s*\"(?<login>[^\"]*)\"\\s*,"
            + "\\s*\"password\"\\s*:\\s*\"(?<password>[^\"]*)\"\\s*\\}\\s*");
    var matcher = expectedJsonStr.matcher(req.toJsonString());
    assertTrue(matcher.matches());
    assertAll(
        () -> assertEquals(expectedLogin, matcher.group("login")),
        () -> assertEquals(expectedPassword, matcher.group("password"))
    );
  }
}