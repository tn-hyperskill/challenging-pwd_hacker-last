package hacker.app;

import java.net.InetAddress;
import java.net.SocketAddress;

public interface AppCfg {

  SocketAddress socketAddr();

  InetAddress ipAddr();

  int port();
}
