package hacker.util;

import java.util.List;
import java.util.stream.Stream;

final class TestingUtil {

  public static Stream<List<Integer>> provideDiverseLists() {
    return Stream.of(List.of(), List.of(4), List.of(7, 8, 9),
        List.of(7, 8, 9, 1, 0));
  }
}
