package hacker.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

final class AuthResultTest {

  @ParameterizedTest
  @CsvSource({
      "false, 0",
      "false, 1",
      "false, 1024",
      "true, 1000000", // NANOS_IN_MILIS
      "true, 5000000", // 5 * NANOS_IN_MILIS
      "true, 10000000000", // 10_000 * NANOS_IN_MILIS
      "false, 100000" // NANOS_IN_MILIS / 10
  })
  void wasTheUsedPwdAPrefixOfTheCorrectPwd(boolean expected, long reqDurationInNanos) {
      var authResult = new AuthResult(null, reqDurationInNanos);
      assertEquals(expected, authResult.wasTheUsedPwdAPrefixOfTheCorrectPwd());
  }
}