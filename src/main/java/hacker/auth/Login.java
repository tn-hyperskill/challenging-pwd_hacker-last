package hacker.auth;

import hacker.util.iter.AutoClosableIterator;
import hacker.util.iter.AutoClosableLinesIterator;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public final class Login {

  // CRUD-C

  private Login() {
  }

  public static AutoClosableIterator<String> typicalLoginIter() {
    final Scanner pwdSource = new Scanner(
        Password.class.getResourceAsStream("TypicalLogins.csv"),
        StandardCharsets.UTF_8
    );

    return new AutoClosableLinesIterator(pwdSource);
  }

  // CRUD-R

}
