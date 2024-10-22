package hacker.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

final class AuthRespTest {

  @Test
  void fromJson() {
    var authResp = AuthResp.fromJson("""
        {
            "result": "Wrong login!"
        }
        """);
    assertEquals("Wrong login!", authResp.message());
  }

  @Test
  @DisplayName("`fromJson(toJsonString())` produces a clone")
  void toJsonString() {
    var value = new AuthResp("Wrong login!");
    var json = value.toJsonString();
    var deserializedJson = AuthResp.fromJson(json);
    assertEquals(value, deserializedJson);
  }
}