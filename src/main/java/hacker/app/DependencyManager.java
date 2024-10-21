package hacker.app;

import hacker.util.exceptions.SecondInitException;
import hacker.util.exceptions.UninitPropException;
import java.util.Optional;

public final class DependencyManager {

  // Static fields

  static final DependencyManager INSTANCE = new DependencyManager();

  // Instance fields

  private Optional<AppCfg> appCfg = Optional.empty();


  // CRUD-C

  private DependencyManager() {
  }

  // CRUD-R

  public AppCfg appCfg() throws UninitPropException {
    return this.appCfg.orElseThrow(() -> new UninitPropException("`appCfg`"));
  }

  // CRUD-U

  public void initAppCfg(AppCfg appCfg) throws SecondInitException {
    if (this.appCfg.isPresent()) {
      throw new SecondInitException();
    }
    this.appCfg = Optional.of(appCfg);
  }
}
