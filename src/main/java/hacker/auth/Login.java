package hacker.auth;

import hacker.app.Log;
import hacker.models.AuthResp;
import hacker.models.UserCredentials;
import hacker.util.iter.AutoClosableIterator;
import hacker.util.iter.AutoClosableLinesIterator;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public final class Login {

  // CRUD-C

  private Login() {
  }

  // CRUD-R

  public static AutoClosableIterator<String> typicalLoginIter() {
    final Scanner pwdSource = new Scanner(
        Password.class.getResourceAsStream("TypicalLogins.csv"),
        StandardCharsets.UTF_8
    );

    return new AutoClosableLinesIterator(pwdSource);
  }

  public static String crackByBruteForce(DataInputStream dataIn, DataOutputStream dataOut) throws Exception {
    try (var loginIter = Login.typicalLoginIter()) {
      while (true) {
        // Login must be found before an exhaustion of the iterator.
        assert loginIter.hasNext();
        // The loop state.
        var curLogin = loginIter.next();
        // Brute-try the current login.
        var authResp = Client.requestAuthentication(dataIn, dataOut,
            new UserCredentials(curLogin, null));
        // The loop's stop condition
        if (Login.wasCorrect(authResp)) {
          return curLogin;
        }
      }
    }
  }

  private static boolean wasCorrect(AuthResp authResp) {
    return !authResp.message().equals("Wrong login!");
  }
}
