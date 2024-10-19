package hacker.auth;

import static org.junit.jupiter.api.Assertions.*;

import hacker.util.iter.AutoClosableIterator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

final class LoginTest {
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