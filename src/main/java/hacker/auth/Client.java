package hacker.auth;

import hacker.app.Log;
import hacker.models.AuthResp;
import hacker.models.UserCredentials;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class Client {

  public static final int SO_TIMEOUT = 5000;

  // CRUD-C

  private Client() {
  }

  // CRUD-R

  public static AuthResult authenticate(
      DataInputStream dataIn, DataOutputStream dataOut,
      UserCredentials credentials) throws IOException {
    var start = System.nanoTime();
    var response = Client.requestAuthentication(dataIn, dataOut, credentials);
    var reqNanoDuration = System.nanoTime() - start;
    return new AuthResult(response, reqNanoDuration);
  }

  public static AuthResp requestAuthentication(
      DataInputStream dataIn, DataOutputStream dataOut,
      UserCredentials credentials) throws IOException {
    dataOut.writeUTF(credentials.toJsonString());
    dataOut.flush();

    return AuthResp.fromJson(dataIn.readUTF());
  }

  public static UserCredentials crackUserCredentials(
      DataInputStream dataIn, DataOutputStream dataOut) throws Exception {
    var crackedLogin = Login.crackByBruteForce(dataIn, dataOut);
    Log.login(crackedLogin);
    var crackedPassword =
        Password.crackUsingTimeVulnerability(dataIn, dataOut, crackedLogin);
    Log.password(crackedPassword);
    var crackedCredentials = new UserCredentials(
        crackedLogin,
        crackedPassword
    );
    return crackedCredentials;
  }
}
