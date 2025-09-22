package io.github.jason13official.telecir;

import io.github.jason13official.telecir.impl.server.data.CircleRecord;
import io.github.jason13official.telecir.impl.server.logic.CircleManager;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

public class TeleCirServer {

  public static LinkedHashMap<UUID, CircleRecord> PRELOAD = new LinkedHashMap<>();
  private static boolean initialized;
  private static TeleCirServer instance;

  private final long seed;
  private final MinecraftServer server;
  private final Function<MinecraftServer, CircleManager> manager = CircleManager::getState;

  public TeleCirServer(final MinecraftServer server) {

    this.server = server;
    this.seed = Objects.requireNonNull(server.getLevel(Level.OVERWORLD)).getSeed();

    Constants.debug("before preloading " + PRELOAD.toString());
    if (!PRELOAD.isEmpty()) {
      PRELOAD.forEach((id, record) -> manager.apply(this.server).setMapping(id, record));
      PRELOAD.clear();
    }
    Constants.debug("after preloading " + PRELOAD);
  }

  public static TeleCirServer getInstance() {

    return instance;
  }

  public static MinecraftServer getServer() {

    return getInstance().server;
  }

  public static long getSeed() {

    return getInstance().seed;
  }

  public static CircleManager getManager() {
    return getInstance().manager.apply(getServer());
  }

  public static void initializeOnFirstTick(final MinecraftServer server) {
    if (!initialized) {
      long start = System.currentTimeMillis();
      Constants.debug("Began server initialization.");

      TeleCirServer.instance = new TeleCirServer(server); // keep setup logic in constructor
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

      TeleCirServer.PRELOAD = new LinkedHashMap<>();
      TeleCirServer.instance = null;
      initialized = false;

      Constants.debug("Ended server dereferencing.");
      long total = System.currentTimeMillis() - start;
      Constants.debug("Server dereferenced in {}ms", total);
    }
  }
}
