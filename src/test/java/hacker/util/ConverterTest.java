package hacker.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

final class ConverterTest {

  @Test
  @DisplayName("`.iterToStream(it)` represents same sequence as `it")
  void iterToStreamTc1() {
    var list = List.of(1, 2, 3, 4, 5);
    var iter = list.iterator();
    var stream = Converter.iterToStream(list.iterator());

    stream.forEachOrdered(el -> assertEquals(iter.next(), el));
  }

  @Test
  @DisplayName("`.iterToStream(it).iterator()` is identity (mathematically)")
  void iterToStreamTc2() {
    var list = List.of(1, 2, 3, 4, 5);
    var iter = list.iterator();
    var stream_derived_iter =
        Converter.iterToStream(list.iterator()).iterator();

    assertIterableEquals(Converter.iterToIterable(iter),
        Converter.iterToIterable(stream_derived_iter));
  }

  @ParameterizedTest @MethodSource("provideDiverseLists")
  @DisplayName("`.iterToIterable(it).iterator()` is identity (mathematically)")
  void iterToIterable(List<Integer> list) {
    var conversions_count = (int) (Math.random() * 20.0);

    Iterable<Integer> iterable = list;
    Iterator<Integer> iter;

    for (int i = 0; i < conversions_count; i++) {
      iter = iterable.iterator();
      iterable = Converter.iterToIterable(iter);
    }

    assertIterableEquals(list, iterable);
  }

  private static Stream<List<Integer>> provideDiverseLists() {
    return TestingUtil.provideDiverseLists();
  }
 }