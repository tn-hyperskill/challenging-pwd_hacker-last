package hacker.app;

import hacker.auth.Client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public final class App {

  // CRUD-C

  private App() {
  }

  // CRUD-R

  public static void main(String[] rawArgs) throws Exception {
    depMgr().initAppCfg(Args.parse(rawArgs));

    try (var sock = establishConnectionWithServer();
        var dataIn = new DataInputStream(sock.getInputStream());
        var dataOut = new DataOutputStream(sock.getOutputStream())) {
      var crackedCredentials = Client.crackUserCredentials(dataIn, dataOut);
      System.out.print(crackedCredentials.toJsonString());
    }
  }

  private static Socket establishConnectionWithServer () throws IOException {
      System.err.println("Connecting to server...");
      var sock = new Socket();
      // Connect to a host and a port using the socket
      sock.connect(depMgr().appCfg().socketAddr());
      sock.setSoTimeout(
          Client.SO_TIMEOUT);  // Set a timeout to prevent indefinite waiting
      System.err.println("Connected successfully.");
      return sock;
    }

    public static DependencyManager depMgr () {
      return DependencyManager.INSTANCE;
    }
  }
