package hacker.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.jupiter.api.Test;

final class ClonerTest {

  @Test
  void testCloneInetAddr() throws UnknownHostException {
    InetAddress $old = InetAddress.getByName("google.com");
    InetAddress $new = Cloner.clone($old);

    assertEquals($old, $new);
  }

}