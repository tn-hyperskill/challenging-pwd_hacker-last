package hacker.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

final class PeekingIteratorTest {

  @Test void forEachRemaining() {
    var list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    var pIter = new PeekingIterator<>(list.iterator());
    for (int i = 1; i <= 5; i++) {
      pIter.discardNext();
    }
    var ref = new Object() {
      int i = 5;
    };
    pIter.forEachRemaining(el -> assertEquals(el, ++ref.i));
  }

  @Test void peek() {
    var list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    for (var iter = new PeekingIterator<>(list.iterator()); iter.hasNext(); ) {
      assertEquals(iter.peek(), iter.next());
    }
  }

  @ParameterizedTest @MethodSource("provideDiverseLists")
  @DisplayName("`.hasNext` behaves like in base iter.")
  void hasNext(
      List<Integer> list) {
    var iter = list.iterator();
    var pIter = new PeekingIterator<>(list.iterator());
    assertEquals(iter.hasNext(), pIter.hasNext());
  }

  @Test @DisplayName("`.peek()` + `.discardNext() ≈ `.next()`")
  void discardNext() {
    var list = List.of(1, 2, 3, 4, 5);
    var pIter = new PeekingIterator<>(list.iterator());
    for (var el : list) {
      assertEquals(el, pIter.peek());
      pIter.discardNext();
    }
    assertFalse(pIter.hasNext());
  }

  @ParameterizedTest @MethodSource("provideDiverseLists")
  @DisplayName("`.discardNext` never throws `NoSuchElementException`")
  void discardNextTc1(List<Integer> list) {
    var pIter = new PeekingIterator<>(list.iterator());
    var discard_count = (int) (Math.random() * 20.0);
    IntStream.range(0, discard_count).forEach(ignored -> pIter.discardNext());
    assertDoesNotThrow(pIter::discardNext);
  }

  @ParameterizedTest @MethodSource("provideDiverseLists")
  @DisplayName("`.discardNext` moves the iterator by 1") void discardNextTc2(
      List<Integer> list) {
    var discard_count = (int) (Math.random() * 20.0);
    // Moved stream
    Stream<Integer> stream = list.stream().skip(discard_count);
    // Moved iter
    PeekableIterator<Integer> pIter = new PeekingIterator<>(list.iterator());
    IntStream.range(0, discard_count).forEach(ignored -> pIter.discardNext());
    // Compare
    assertIterableEquals(Converter.iterToIterable(stream.iterator()),
        Converter.iterToIterable(pIter));
  }

  @Test @DisplayName("`.next` throws on empty") void nextTc1() {
    assertThrows(NoSuchElementException.class,
        new PeekingIterator<>(Collections.emptyIterator())::next);
  }

  @Test @DisplayName("`.next` returns 1st el. on 1st call") void nextTc2() {
    assertEquals(1, new PeekingIterator<>(List.of(1).iterator()).next());
  }

  public static Stream<List<Integer>> provideDiverseLists() {
    return TestingUtil.provideDiverseLists();
  }
}