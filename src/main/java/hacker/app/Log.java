package hacker.app;

import hacker.auth.AuthResult;
import java.io.PrintStream;

public final class Log {

  // Static fields

  private static final PrintStream outStream = System.err;

  // CRUD-C

  private Log() {
  }

  // CRUD-R

  public static void property(String name, Object value) {
    outStream.printf("`%s` = %s\n", name, value);
  }

  public static void password(String value) {
    property("password", value);
  }

  public static void login(String value) {
    property("login", value);
  }

  public static void passwordPrefix(String value) {
    property("password prefix", value);
  }

  public static void authResult(AuthResult authResult) {
    outStream.printf("Authentication took: %d. Password prefix was %s.\n",
        authResult.durationInNanos(),
        authResult.wasTheUsedPwdAPrefixOfTheCorrectPwd()
            ? "correct" : "incorrect"
    );
  }
}
