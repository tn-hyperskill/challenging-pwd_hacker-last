package hacker.app;

import hacker.auth.Password;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public final class App {

  public static final int SO_TIMEOUT = 5000;

  // CRUD-C

  private App() {
  }

  // CRUD-R

  public static void main(String[] rawArgs) throws UnknownHostException {
    var args = Args.parse(rawArgs);

    // Create a new socket
    try (var sock = new Socket()) {
      // Connect to a host and a port using the socket
      System.err.println("Connecting to server...");
      sock.connect(args.socketAddr());
      sock.setSoTimeout(
          SO_TIMEOUT);  // Set a timeout to prevent indefinite waiting

      System.err.println("Connected successfully. Sending result...");

      // Send a result from the third command line argument to the host
      // using the socket
      try (var dataOut = new DataOutputStream(sock.getOutputStream());
          var dataIn = new DataInputStream(sock.getInputStream());
          var pwdsIter = Password.casefulTypicalPwdsIter()) {
        while (pwdsIter.hasNext()) {
          try {
            String curPwd = pwdsIter.next();
            dataOut.writeUTF(curPwd);
            dataOut.flush();

            String response = dataIn.readUTF();
            if (response.equals("Connection success!")) {
              System.out.println(curPwd);
              break;
            }
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
      } catch (Exception e) {
        System.err.println("Error during communication: " + e.getMessage());
      }

    } catch (
        IOException e) {
      System.err.println("Connection error: " + e.getMessage());
    }
  }
}
