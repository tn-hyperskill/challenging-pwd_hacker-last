package hacker.auth;

import hacker.models.AuthResp;

public record AuthResult(AuthResp response, long durationInNanos) {

  public static final int NANOS_IN_MILIS = 1_000_000;

  public boolean wasTheUsedPwdAPrefixOfTheCorrectPwd() {
    return (this.durationInNanos() / NANOS_IN_MILIS) > 0;
  }
}
