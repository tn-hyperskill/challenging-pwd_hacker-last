package hacker.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

final class ConverterTest {

  @Test
  @DisplayName("`.toStream(iter)` represents same sequence as `iter")
  void toStreamTc1() {
    var list = List.of(1, 2, 3, 4, 5);
    var iter = list.iterator();
    var stream = Converter.toStream(list.iterator());

    stream.forEachOrdered(el -> assertEquals(iter.next(), el));
  }

  @Test
  @DisplayName("`.toStream(iter).iterator()` is identity (mathematically)")
  void toStreamTc2() {
    var list = List.of(1, 2, 3, 4, 5);
    var iter = list.iterator();
    var stream_derived_iter =
        Converter.toStream(list.iterator()).iterator();

    assertIterableEquals(Converter.toIterable(iter),
        Converter.toIterable(stream_derived_iter));
  }

  @ParameterizedTest @MethodSource("provideDiverseLists")
  @DisplayName(
      "`.toIterable(iterable.iterator())` is identity (mathematically)")
  void toIterable_Iterable$iterator(List<Integer> list) {
    var conversions_count = (int) (Math.random() * 20.0);

    Iterable<Integer> iterable = list;

    for (int i = 0; i < conversions_count; i++) {
      iterable = Converter.toIterable(iterable.iterator());
    }

    assertIterableEquals(list, iterable);
  }

  @ParameterizedTest @MethodSource("provideDiverseLists")
  @DisplayName("`toIterable(toStream(iter))` is identity (mathematically)")
  void toIterable_toStream(List<Integer> list) {
    var conversions_count = (int) (Math.random() * 20.0);

    Iterable<Integer> iterable = list;

    for (int i = 0; i < conversions_count; i++) {
      iterable = Converter.toIterable(Converter.toStream(iterable));
    }

    assertIterableEquals(list, iterable);
  }

  private static Stream<List<Integer>> provideDiverseLists() {
    return TestingUtil.provideDiverseLists();
  }
}