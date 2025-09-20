package io.github.jason13official.telecir;

import io.github.jason13official.telecir.impl.server.logic.CircleManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class TeleCirServer {

  private static TeleCirServer instance;
  private static boolean initialized;

  private final MinecraftServer server;

  public TeleCirServer(final MinecraftServer server) {

    this.server = server;

    ServerLevel level = this.server.getLevel(Level.OVERWORLD);

    if (level == null) {
      throw new IllegalStateException(
          this.getClass().getSimpleName() + " was unable to retrieve Level.OVERWORLD");
    }

    Constants.LOG.info("Retrieving data storage from level {}: ", level.dimension());
    // Constants.LOG.info(level.getDataStorage().toString());
    CircleManager manager = CircleManager.getState(server);
    Constants.LOG.info(manager.toString());
  }

  public static TeleCirServer getInstance() {
    return instance;
  }

  public static MinecraftServer getServer() {
    return getInstance().server;
  }

  public static void init(final MinecraftServer server) {
    if (!initialized) {
      Constants.LOG.info("Began server initialization.");
      TeleCirServer.instance = new TeleCirServer(server);
      initialized = true;
      Constants.LOG.info("Ended server initialization.");
    }
  }

  public static void dereference(final MinecraftServer server) {
    if (initialized) {
      Constants.LOG.info("Began server dereferencing.");
      TeleCirServer.instance = null;
      initialized = false;
      Constants.LOG.info("Ended server dereferencing.");
    }
  }
}
