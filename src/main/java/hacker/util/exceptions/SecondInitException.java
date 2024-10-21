package hacker.util.exceptions;

/**
 * An exception throw on attempt to reinitialize once-init object.
 * <br>
 * If you want to make you object not reinitializable you can use this exception
 * to block the 2nd init.
 */
public final class SecondInitException extends RuntimeException {

  public SecondInitException() {
    super("Attempted second initialization of once-init object.");
  }
}
