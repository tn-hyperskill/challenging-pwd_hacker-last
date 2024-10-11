package hacker.padlock;

import hacker.util.Converter;
import hacker.util.PeekableIterator;
import hacker.util.PeekingIterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Password {

  // Constants

  public static final List<Character> ACCEPTED_CHARS =
      constructAcceptedLetters();

  // CRUD-C

  protected Password() {
  }

  // CRUD-R

  public static PeekableIterator<String> allCombinations() {
    return new PeekableIterator<String>() {
      // Instance fields

      private List<PeekingIterator<Character>> rotatingDials =
          RotDial.constructDialsAsm(1);

      // CRUD-R

      @Override public String peek() {
        return this.rotatingDials.stream()
            .map(rotDial -> rotDial.peek().toString())
            .collect(Collectors.joining(""));
      }

      @Override public boolean hasNext() {
        return true;
      }

      // CRUD-U

      @Override public String next() {
        var ret = this.peek();

        for (int firstDialToRotIdx = this.rotatingDials.size() - 1;
            0 <= firstDialToRotIdx; --firstDialToRotIdx) {
          var firstDialToRot = this.rotatingDials.get(firstDialToRotIdx);
          try {
            firstDialToRot.next(); // Rotating
            // On successful rotation reset all trailing dials
            for (int dialToResetIdx = firstDialToRotIdx + 1;
                dialToResetIdx < this.rotatingDials.size();
                dialToResetIdx++) {
              this.rotatingDials.set(dialToResetIdx, RotDial.construct());
            }
            // Since rotation has been performed we can return a peeked value.
            return ret;
          } catch (NoSuchElementException $) {
            continue;
          }
        }

        this.rotatingDials =
            RotDial.constructDialsAsm(this.rotatingDials.size() + 1);
        return ret;
      }
    };
  }

  public static Stream<String> allCombsStream() {
    return Converter.iterToSteam(allCombinations());
  }

  private static List<Character> constructAcceptedLetters() {
    return Stream.concat(IntStream.rangeClosed('a', 'z').mapToObj(i -> i),
            IntStream.rangeClosed('0', '9').mapToObj(i -> i))
        .map(i -> (char) (int) i).toList();
  }
}
