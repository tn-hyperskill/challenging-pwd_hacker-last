package hacker.padlock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import hacker.util.Converter;
import hacker.util.PeekingIterator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

final class RotDialTest {

  @Test @DisplayName("`.construct`ed dial produces accepted chars")
  void construct() {
    var rotDial = RotDial.construct();
    checkProducedSeqByConsumingDial(rotDial);
  }

  @ParameterizedTest @ValueSource(ints = {1, 7, 90})
  void constructDialsAsm(int wantedSize) {
    var dialsAsm = RotDial.constructDialsAsm(wantedSize);
    // First assertion
    assertEquals(wantedSize, dialsAsm.size());
    var randDialIdx = (int) (Math.random() * wantedSize);
    var chosenRandDial = dialsAsm.get(randDialIdx);
    // Second assertion
    checkProducedSeqByConsumingDial(chosenRandDial);
  }

  private static void checkProducedSeqByConsumingDial(PeekingIterator<Character> rotDial) {
    assertIterableEquals(Password.ACCEPTED_CHARS, Converter.toStream(rotDial).toList());
  }
}