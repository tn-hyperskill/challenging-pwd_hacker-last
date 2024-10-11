package hacker.util;

import java.util.Iterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class Converter {

  public static <T> Stream<T> iterToSteam(Iterator<T> iterator) {
    return StreamSupport.stream(
        Spliterators.spliteratorUnknownSize(iterator, 0),
    false);
  }
}
