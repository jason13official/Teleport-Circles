package io.github.jason13official.telecir;

import net.minecraft.server.MinecraftServer;

public class TeleCirServer {

  private static TeleCirServer instance;
  private static boolean initialized;

  private final MinecraftServer server;

  public TeleCirServer(final MinecraftServer server) {

    this.server = server;
  }

  public static TeleCirServer getInstance() {

    return instance;
  }

  public static MinecraftServer getServer() {

    return getInstance().server;
  }

  public static void initializeOnFirstTick(final MinecraftServer server) {
    if (!initialized) {
      long start = System.currentTimeMillis();
      Constants.debug("Began server initialization.");

      TeleCirServer.instance = new TeleCirServer(server);
      initialized = true;

      Constants.debug("Ended server initialization.");
      long total = System.currentTimeMillis() - start;
      Constants.debug("Server initialized in {}ms", total);
    }
  }

  public static void dereference() {
    if (initialized) {
      long start = System.currentTimeMillis();
      Constants.debug("Began server dereferencing.");

      TeleCirServer.instance = null;
      initialized = false;

      Constants.debug("Ended server dereferencing.");
      long total = System.currentTimeMillis() - start;
      Constants.debug("Server dereferenced in {}ms", total);
    }
  }
}
