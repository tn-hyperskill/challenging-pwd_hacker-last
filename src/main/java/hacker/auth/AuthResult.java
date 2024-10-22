package hacker.auth;

import hacker.models.AuthResp;

public record AuthResult(AuthResp response, long durationInNanos) {

  public static final long NANOS_IN_MILIS = 1_000_000;
  public static final long MIN_EXCEPTIONAL_DURATION_IN_MILIS = 20;
  public static final long MIN_EXCEPTIONAL_DURATION_IN_NANOS =
      MIN_EXCEPTIONAL_DURATION_IN_MILIS * NANOS_IN_MILIS;

  public boolean wasTheUsedPwdAPrefixOfTheCorrectPwd() {
    return this.durationInNanos() >= MIN_EXCEPTIONAL_DURATION_IN_NANOS;
  }
}
