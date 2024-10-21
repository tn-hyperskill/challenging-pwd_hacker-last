package hacker.util.exceptions;

public final class UninitPropException extends RuntimeException {
  public UninitPropException(String propName) {
    super(
        "Attempt to access uninitialized property \"%s\".".formatted(propName)
    );
  }
}
