package hacker.padlock;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import hacker.util.Converter;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

final class PasswordTest {

  public static final int PWD_COMBS_ARBITRARY_COUNT_LIMIT = 50;
  private static final Pattern ACCEPTED_CHARS_LIST_PATTERN =
      Pattern.compile("^a[b-y]{24}z0[1-8]{8}9$");

  @Test
  void acceptedLetters() {
    Assertions.assertTrue(
        ACCEPTED_CHARS_LIST_PATTERN.matcher(
            Password.ACCEPTED_CHARS.stream()
                .map(Object::toString).collect(Collectors.joining())
        ).matches()
    );
  }

  @Test
  void allCombsStream_1stProducesSingleChars() {
    var singleChars = Password.ACCEPTED_CHARS.stream().map(Object::toString);
    var firstChars =
        Password.allCombsStream().limit(Password.ACCEPTED_CHARS.size());

    assertIterableEquals(Converter.toIterable(singleChars), Converter.toIterable(firstChars));
  }
  @Test
  void allCombsStream_producesDoubleCharsAfterSingles() {

    var firstChars =
        Password.allCombsStream().skip(Password.ACCEPTED_CHARS.size()).limit(3);

    assertIterableEquals(List.of("aa", "ab", "ac"), Converter.toIterable(firstChars));
  }

  @Test @DisplayName("`allCombsStream()` seq. = `allCombsIter()` seq.")
  void allCombsStream_alike_allCombsIter() {
    var sequences = Stream.of(
            Stream.generate(Password.allCombsPIter()::next),
            Password.allCombsStream()
        ).map(stream -> stream.limit(PWD_COMBS_ARBITRARY_COUNT_LIMIT))
        .map(Converter::toIterable)
        .toList();

    assertIterableEquals(sequences.get(0), sequences.get(1));
  }

}