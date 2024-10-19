package hacker.util.iter;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Scanner;

public final class AutoClosableLinesIterator
    implements AutoClosableIterator<String> {

  private final Scanner pwdSource;

  @SuppressFBWarnings("EI")
  public AutoClosableLinesIterator(final Scanner linesSource) {
    this.pwdSource = linesSource;
  }

  @Override public void close() {
    pwdSource.close();
  }

  @Override public boolean hasNext() {
    return pwdSource.hasNextLine();
  }

  @Override public String next() {
    return pwdSource.nextLine();
  }
}
