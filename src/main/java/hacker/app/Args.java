package hacker.app;

import hacker.util.Cloner;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public final class Args implements AppCfg {

  // Instance fields

  private final InetAddress ipAddr;
  private final int socketPort;

  // CRUD-C

  public static Args parse(final String... args) throws UnknownHostException {
    return new Args(args);
  }

  @SuppressWarnings("PMD.UseVarargs")
  private Args(final String[] args) throws UnknownHostException {
    this.ipAddr = InetAddress.getByName(args[0]);
    this.socketPort = Integer.parseInt(args[1]);
  }

  public Args(InetAddress ipAddr, int socketPort) throws UnknownHostException {
    this.ipAddr = Cloner.clone(ipAddr);
    this.socketPort = socketPort;
  }

  // CRUD-R: Properties

  @Override public SocketAddress socketAddr() {
    return new InetSocketAddress(this.ipAddr(), this.port());
  }

  @Override public InetAddress ipAddr() {
    return Cloner.clone(this.ipAddr);
  }

  @Override public int port() {
    return this.socketPort;
  }
}
