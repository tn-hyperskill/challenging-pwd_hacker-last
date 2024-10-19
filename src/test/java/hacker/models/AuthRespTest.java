package hacker.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

final class AuthRespTest {

  @Test
  void fromJson() {
    var authResp = AuthResp.fromJson("""
        {
            "result": "Wrong login!"
        }
        """);
    assertEquals("Wrong login!", authResp.result());
  }
}