package hacker.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hacker.models.AuthResp;
import hacker.models.UserCredentials;
import hacker.util.FlushableDataOutput;
import hacker.util.iter.AutoClosableIterator;
import java.io.DataInput;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

final class LoginTest {

  @Test
  void crackByBruteForce() throws Exception {
    // Constants
    final String correctLogin = "admin1";
    // State
    final var isCracked = new Object() {
      boolean value = false;
    };
    // Creating mocked data input
    var dataIn = Mockito.mock(DataInput.class);
    Mockito.when(dataIn.readUTF()).thenAnswer(invocation ->
        new AuthResp(isCracked.value ? "Connection success!" : "Wrong login!")
            .toJsonString()
    );
    // Creating mocked data output
    var dataOut = Mockito.mock(FlushableDataOutput.class);
    Mockito.doAnswer(invocation -> {
      String userInput = invocation.getArgument(0);
      String inputLogin = UserCredentials.fromJson(userInput).login();
      isCracked.value = correctLogin.equals(inputLogin);
      return null;
    }).when(dataOut).writeUTF(Mockito.anyString());
    // Checking correctness
    String crackedLogin = Login.crackByBruteForce(dataIn, dataOut);
    assertEquals(correctLogin, crackedLogin);
  }

  @ParameterizedTest
  @MethodSource("wasCorrect_testParams")
  void wasCorrect(boolean expected, String message) {
    var authResp = new AuthResp(message);
    assertEquals(expected, Login.wasCorrect(authResp));
  }

  private static Object[][] wasCorrect_testParams() {
    return new Object[][]{
        {false, "Wrong login!"},
        {true, "Wrong password!"},
        {false, "Bad request!"},
        {true, "Exception happened during login"},
        {true, "Connection success!"}
    };
  }


  @Nested @TestInstance(Lifecycle.PER_CLASS)
  class TypicalPwdIter {

    private AutoClosableIterator<String> theIter;

    @BeforeAll
    void setup() {
      this.theIter = Login.typicalLoginIter();
    }

    @AfterAll
    void teardown() throws Exception {
      this.theIter.close();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/hacker/auth/TypicalLogins.csv")
    void next(String expectedLogin) {
      assertEquals(expectedLogin, theIter.next());
    }
  }
}