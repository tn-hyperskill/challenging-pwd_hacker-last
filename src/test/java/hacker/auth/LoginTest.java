package hacker.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hacker.models.AuthResp;
import hacker.models.UserCredentials;
import hacker.util.FlushableDataOutput;
import hacker.util.iter.AutoClosableIterator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    final String correctLogin = "admin1";
    final var isCracked = new Object() {
      boolean value = false;
    };

    var dataIn = Mockito.mock(DataInput.class);
    Mockito.when(dataIn.readUTF()).thenAnswer(invocation ->
        new AuthResp(isCracked.value ? "Connection success!" : "Wrong login!")
            .toJsonString()
    );
    var dataOut = Mockito.mock(FlushableDataOutput.class);
    Mockito.doAnswer(invocation -> {
      String str = invocation.getArgument(0);
      isCracked.value = str.contains(correctLogin);
      return null;
    }).when(dataOut).writeUTF(Mockito.anyString());
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