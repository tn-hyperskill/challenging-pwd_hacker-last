package hacker.util;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

public final class PeekingIterator<T> implements PeekableIterator<T> {

  // Instance fields

  private final Iterator<T> inner;
  private Optional<T> peeked;

  // CRUD-C

  @SuppressFBWarnings("EI")
  public PeekingIterator(Iterator<T> inner) {
    this.inner = inner;
    this.peeked = Optional.empty();
    this.discardCurrentPeeked();
  }

  // CRUD-R

  @Override public T peek() throws NoSuchElementException {
    return this.peeked.orElseThrow(NoSuchElementException::new);
  }

  @Override public boolean hasNext() {
    return this.peeked.isPresent();
  }

  // CRUD-U

  @Override
  public void discardNext() {
    this.discardCurrentPeeked();
  }

  @Override public T next() throws NoSuchElementException {
    var ret = this.peek();
    this.discardCurrentPeeked();
    return ret;
  }

  private void discardCurrentPeeked() {
    try {
      this.peeked = Optional.of(this.inner.next());
    } catch (NoSuchElementException ignored) {
      this.peeked = Optional.empty();
    }
  }
}
