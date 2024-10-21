package hacker.auth;

import hacker.models.AuthResp;

public record AuthResult(AuthResp response, long durationInNano) {
  public boolean wasTheUsedPwdAPrefixOfTheCorrectPwd() {
    return (this.durationInNano() / 1_000_000) > 0;
  }
}
