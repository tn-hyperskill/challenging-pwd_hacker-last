package hacker.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import hacker.models.AuthResp;
import hacker.models.UserCredentials;
import hacker.util.Converter;
import hacker.util.FlushableDataOutput;
import hacker.util.iter.AutoClosableIterator;
import java.io.DataInput;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
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
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

final class PasswordTest {

  public static final int PWD_COMBS_ARBITRARY_COUNT_LIMIT = 50;
  private static final Pattern ACCEPTED_CHARS_LIST_PATTERN =
      Pattern.compile("^[A-Za-z]{52}0[1-8]{8}9$");

  @Test
  void crackUsingTimeVulnerability() throws IOException {
    final String correctPwd = "0saADf1Alsd";
    final var isCracked = new Object() {
      boolean value = false;
    };

    var dataIn = Mockito.mock(DataInput.class);
    Mockito.when(dataIn.readUTF()).thenAnswer(invocation ->
        new AuthResp(
            isCracked.value ? "Connection success!" : "Wrong password!")
            .toJsonString()
    );
    var dataOut = Mockito.mock(FlushableDataOutput.class);
    Mockito.doAnswer(invocation -> {
      String userInput = invocation.getArgument(0);
      String usedPwd = UserCredentials.fromJson(userInput).password();
      if (correctPwd.startsWith(usedPwd)) {
        // Should be small to not elongate the testing duration.
        final int randDurationMultiplier = ThreadLocalRandom.current()
            .nextInt(1, 4);
        final long sleepDuration = AuthResult.MIN_EXCEPTIONAL_DURATION_IN_MILIS
            * randDurationMultiplier;
        Thread.currentThread().sleep(sleepDuration);
      }
      isCracked.value = correctPwd.equals(usedPwd);
      return null;
    }).when(dataOut).writeUTF(Mockito.anyString());

    String crackedLogin =
        Password.crackUsingTimeVulnerability(dataIn, dataOut, null);
    assertEquals(correctPwd, crackedLogin);
  }

  @Test
  void acceptedLetters() {
    String acceptedCharsInStr = Password.ACCEPTED_CHARS.stream()
        .map(Object::toString).collect(Collectors.joining());
    Assertions.assertTrue(
        ACCEPTED_CHARS_LIST_PATTERN.matcher(acceptedCharsInStr).matches(),
        acceptedCharsInStr + " doesn't match the expected pattern"
    );
  }

  @Nested class AllCombsStream {

    @Test
    void firstProducesSingleChars() {
      var singleChars = Password.ACCEPTED_CHARS.stream().map(Object::toString);
      var firstChars =
          Password.allCombsStream().limit(Password.ACCEPTED_CHARS.size());

      assertIterableEquals(Converter.toIterable(singleChars),
          Converter.toIterable(firstChars));
    }

    @Test
    void producesDoubleCharsAfterSingles() {

      var firstChars =
          Password.allCombsStream().skip(Password.ACCEPTED_CHARS.size())
              .limit(3);

      assertIterableEquals(List.of("aa", "ab", "ac"),
          Converter.toIterable(firstChars));
    }
  }

  @Nested @TestInstance(Lifecycle.PER_CLASS)
  class CasefulTypicalPwdIter {

    private AutoClosableIterator<String> theIter;

    @BeforeAll
    void setup() {
      this.theIter = Password.casefulTypicalPwdsIter();
    }

    @AfterAll
    void teardown() throws Exception {
      this.theIter.close();
    }

    @ParameterizedTest @ValueSource(strings = {
        "123456", "123456", "123456", "123456", "123456", "123456",
        "123456", "123456", "123456", "123456", "123456", "123456",
        "123456", "123456", "123456", "123456", "123456", "123456",
        "123456", "123456", "123456", "123456", "123456", "123456",
        "123456", "123456", "123456", "123456", "123456", "123456",
        "123456", "123456", "123456", "123456", "123456", "123456",
        "123456", "123456", "123456", "123456", "123456", "123456",
        "123456", "123456", "123456", "123456", "123456", "123456",
        "123456", "123456", "123456", "123456", "123456", "123456",
        "123456", "123456", "123456", "123456", "123456", "123456",
        "123456", "123456", "123456", "123456",
        "password", "Password", "pAssword", "PAssword", "paSsword",
        "PaSsword"})
    void next(String expectedPwd) {
      assertEquals(expectedPwd, theIter.next());
    }
  }

  @Nested @TestInstance(Lifecycle.PER_CLASS)
  class TypicalPwdIter {

    private AutoClosableIterator<String> theIter;

    @BeforeAll
    void setup() {
      this.theIter = Password.typicalPwdsIter();
    }

    @AfterAll
    void teardown() throws Exception {
      this.theIter.close();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/hacker/auth/TypicalPwds.csv")
    void next(String expectedPwd) {
      assertEquals(expectedPwd, theIter.next());
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