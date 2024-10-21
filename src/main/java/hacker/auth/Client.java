package hacker.auth;

import hacker.app.Log;
import hacker.models.AuthResp;
import hacker.models.UserCredentials;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.Flushable;
import java.io.IOException;

public final class Client {

  public static final int SO_TIMEOUT = 5000;

  // CRUD-C

  private Client() {
  }

  // CRUD-R

  public static <O extends Flushable & DataOutput> AuthResult authenticate(
      DataInput dataIn, O dataOut,
      UserCredentials credentials) throws IOException {
    var start = System.nanoTime();
    var response = Client.requestAuthentication(dataIn, dataOut, credentials);
    var reqNanoDuration = System.nanoTime() - start;
    return new AuthResult(response, reqNanoDuration);
  }

  public static <O extends Flushable & DataOutput> AuthResp requestAuthentication(
      DataInput dataIn, O dataOut,
      UserCredentials credentials) throws IOException {
    dataOut.writeUTF(credentials.toJsonString());
    dataOut.flush();

    return AuthResp.fromJson(dataIn.readUTF());
  }

  public static <O extends Flushable & DataOutput> UserCredentials crackUserCredentials(
      DataInput dataIn, O dataOut) throws Exception {
    var crackedLogin = Login.crackByBruteForce(dataIn, dataOut);
    Log.login(crackedLogin);
    var crackedPassword =
        Password.crackUsingTimeVulnerability(dataIn, dataOut, crackedLogin);
    Log.password(crackedPassword);
    var crackedCredentials = new UserCredentials(crackedLogin, crackedPassword);
    return crackedCredentials;
  }
}
