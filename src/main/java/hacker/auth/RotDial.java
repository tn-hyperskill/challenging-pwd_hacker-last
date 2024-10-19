package hacker.auth;

import hacker.util.iter.PeekingIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class RotDial {

  // CRUD-C

  private RotDial() {
  }

  // CRUD-R

  public static PeekingIterator<Character> construct() {
    return new PeekingIterator<>(Password.ACCEPTED_CHARS.iterator());
  }

  public static List<PeekingIterator<Character>> constructDialsAsm(int count) {
    return IntStream.range(0, count)
        .mapToObj(_idx -> construct())
        .collect(Collectors.toCollection(ArrayList::new));
  }
}
