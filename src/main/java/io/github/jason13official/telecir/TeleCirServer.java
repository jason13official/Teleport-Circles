package io.github.jason13official.telecir;

import io.github.jason13official.telecir.impl.common.util.ModConfiguration;
import io.github.jason13official.telecir.impl.server.data.CircleRecord;
import io.github.jason13official.telecir.impl.server.logic.CircleManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

    instance = this; // allows early references during initialization

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
    if (!initialized && TeleCirServer.instance == null) {
      long start = System.currentTimeMillis();
      Constants.debug("Began server initialization.");

      TeleCirServer.instance = new TeleCirServer(server); // keep setup logic in constructor
      initialized = true;

      Constants.debug("Ended server initialization.");
      Constants.debug("Server instance " + instance);
      long total = System.currentTimeMillis() - start;
      Constants.debug("Server initialized in {}ms", total);
    }
  }

  public static void dereference() {
    if (initialized && TeleCirServer.instance != null) {
      long start = System.currentTimeMillis();
      Constants.debug("Began server dereferencing.");

      Constants.debug("records in memory before dereference " + getManager().getRecords());

      TeleCirServer.PRELOAD = new LinkedHashMap<>();
      TeleCirServer.instance = null;
      initialized = false;

      Constants.debug("Ended server dereferencing.");
      Constants.debug("Server instance " + instance);
      long total = System.currentTimeMillis() - start;
      Constants.debug("Server dereferenced in {}ms", total);
    }
  }
}
