package hacker.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public interface PeekableIterator<T> extends Iterator<T> {
  // CRUD-R

  T peek();

  // CRUD-U

  @SuppressWarnings("PMD.EmptyCatchBlock")
  default void discardNext() {
    try {
      this.next();
    } catch (NoSuchElementException $) {
    }
  }
}
