package hacker.auth;

import hacker.models.AuthResp;
import hacker.models.UserCredentials;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class Req {

  // CRUD-C

  private Req() {
  }

  // CRUD-R

  public static AuthResp authenticate(
      DataInputStream dataIn, DataOutputStream dataOut,
      UserCredentials credentials) throws IOException {
    dataOut.writeUTF(credentials.toJsonString());
    dataOut.flush();

    return AuthResp.fromJson(dataIn.readUTF());
  }
}
