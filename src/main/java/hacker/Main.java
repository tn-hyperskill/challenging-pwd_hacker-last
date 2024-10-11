package hacker;

import hacker.app.Args;
import hacker.padlock.Password;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {

  public static void main(String[] rawArgs) throws UnknownHostException {
    var args = Args.parse(rawArgs);

    // Create a new socket
    try (var sock = new Socket()) {
      // Connect to a host and a port using the socket
      System.err.println("Connecting to server...");
      sock.connect(args.socketAddr());
      sock.setSoTimeout(5000);  // Set a timeout to prevent indefinite waiting

      System.err.println("Connected successfully. Sending message...");

      // Send a message from the third command line argument to the host
      // using the socket
      try (var dataOut = new DataOutputStream(sock.getOutputStream());
          var dataIn = new DataInputStream(sock.getInputStream())) {

        for (var combIter = Password.allCombinations(); combIter.hasNext(); combIter.discardNext()){
              try {
                dataOut.writeUTF(combIter.peek());
                dataOut.flush();

                String response = dataIn.readUTF();
                if (response.equals("Connection success!")){
                  System.out.println(combIter.peek());
                  break;
                }
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            }


      } catch (IOException e) {
        System.err.println("Error during communication: " + e.getMessage());
      }

    } catch (IOException e) {
      System.err.println("Connection error: " + e.getMessage());
    }
  }
}
