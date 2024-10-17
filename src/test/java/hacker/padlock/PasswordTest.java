package hacker.padlock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import hacker.util.Converter;
import hacker.util.iter.AutoclosableIterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

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

    assertIterableEquals(Converter.toIterable(singleChars),
        Converter.toIterable(firstChars));
  }

  @Test
  void allCombsStream_producesDoubleCharsAfterSingles() {

    var firstChars =
        Password.allCombsStream().skip(Password.ACCEPTED_CHARS.size()).limit(3);

    assertIterableEquals(List.of("aa", "ab", "ac"),
        Converter.toIterable(firstChars));
  }

  @Nested @TestInstance(Lifecycle.PER_CLASS)
  public class TypicalPwdIter {

    private AutoclosableIterator<String> typicalPwdIter;

    @BeforeAll
    void setup() {
      this.typicalPwdIter = Password.typicalPwdIter();
    }

    @AfterAll
    void teardown() throws Exception {
      this.typicalPwdIter.close();
    }

    @ParameterizedTest @CsvFileSource(resources = "/hacker/padlock/TypicalPwds.csv")
    void typicalPwdIter(String expectedPwd) {
      assertEquals(expectedPwd, typicalPwdIter.next());
    }
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