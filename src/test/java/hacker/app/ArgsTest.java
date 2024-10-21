package hacker.app;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

final class ArgsTest {

  // Fields

  private static final String ipAddrStr = "127.0.0.1";
  private static final int socketPort = 8080;
  private static final InetAddress ipAddr = constructIpAddr();
  private static final SocketAddress socketAddr = constructSocketAddr();

  private static final Args args = constructArgs();

  // Test methods

  @Test
  void testParse() {
    var executables = Stream.<Executable>of(() -> args.parse(), () -> args.parse("127.0.0.1"));
    assertAll(executables.map(exec -> () -> assertThrows(Exception.class, exec)));
  }

  @Test
  @DisplayName("`.parse` takes exactly 2 args")
  void parse() {
    Args parsed = null;
    try {
      parsed = Args.parse(ipAddrStr, String.valueOf(socketPort));
    } catch (UnknownHostException e) {
      org.junit.jupiter.api.Assertions.fail(e);
    }
    assert parsed.socketAddr().equals(socketAddr);
  }

  @Test
  void socketAddr() {
    assertEquals(args.socketAddr(), socketAddr);
  }

  @Test
  void ipAddr() {
    assertEquals(args.ipAddr(), ipAddr);
  }

  @Test
  void port() {
    assertEquals(args.port(), socketPort);
  }

  // Field constructors

  private static InetAddress constructIpAddr() {
    try {
      return InetAddress.getByName(ipAddrStr);
    } catch (UnknownHostException e) {
      throw new RuntimeException(e);
    }
  }

  private static SocketAddress constructSocketAddr() {
    return new InetSocketAddress(ipAddr, socketPort);
  }

  private static Args constructArgs() {
    try {
      return new Args(ipAddr, socketPort);
    } catch (UnknownHostException e) {
      throw new RuntimeException(e);
    }
  }
}