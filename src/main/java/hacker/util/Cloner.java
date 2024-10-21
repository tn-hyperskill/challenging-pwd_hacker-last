package hacker.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class Cloner {

  // CRUD-C

  private Cloner() {
  }

  // CRUD-R

  public static InetAddress clone(InetAddress ipAddr) {
    try {
      return InetAddress.getByAddress(ipAddr.getAddress());
    } catch (UnknownHostException e) {
      throw new RuntimeException(e);
    }
  }
}
