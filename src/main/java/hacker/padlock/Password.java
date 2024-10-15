package hacker.padlock;

import hacker.util.Converter;
import hacker.util.PeekableIterator;
import hacker.util.PeekingIterator;
import java.util.List;
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

  public static PeekableIterator<String> allCombsPIter() {
    return new PeekableIterator<>() {
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
          firstDialToRot.discardNext(); // Rotating
          // Making sure there is sth
          if (firstDialToRot.hasNext()) {
            // On successful/meaningful rotation reset all trailing dials
            for (int dialToResetIdx = firstDialToRotIdx + 1;
                dialToResetIdx < this.rotatingDials.size();
                dialToResetIdx++) {
              this.rotatingDials.set(dialToResetIdx, RotDial.construct());
            }
            // Since rotation has been performed we can return a peeked value.
            return ret;
          }
        }

        this.rotatingDials =
            RotDial.constructDialsAsm(this.rotatingDials.size() + 1);
        return ret;
      }
    };
  }

  public static Stream<String> allCombsStream() {
    return Converter.toStream(allCombsPIter());
  }

  private static List<Character> constructAcceptedLetters() {
    return IntStream.concat(
            IntStream.rangeClosed('a', 'z'),
            IntStream.rangeClosed('0', '9')
        ).mapToObj(i -> (char) i)
        .toList();
  }
}
