package hacker.util;

import java.util.Iterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class Converter {

  // CRUD-C

  private Converter() {
  }

  // CRUD-R

  public static <T> Stream<T> toStream(Iterator<T> iterator) {
    return StreamSupport.stream(
        Spliterators.spliteratorUnknownSize(iterator, 0),
        false);
  }
  public static <T> Stream<T> toStream(Iterable<T> iterable) {
    return toStream(iterable.iterator());
  }

  public static <T> Iterable<T> toIterable(Iterator<T> iterator) {
    return () -> iterator;
  }
  public static <T> Iterable<T> toIterable(Stream<T> stream) {
    return stream::iterator;
  }
}
