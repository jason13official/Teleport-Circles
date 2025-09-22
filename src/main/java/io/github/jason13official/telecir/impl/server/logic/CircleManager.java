package io.github.jason13official.telecir.impl.server.logic;

import io.github.jason13official.telecir.Constants;
import io.github.jason13official.telecir.impl.server.data.CircleRecord;
import io.github.jason13official.telecir.impl.server.util.CircleManagerSerialization;
import io.github.jason13official.telecir.impl.server.util.CircleNameGenerator;
import java.util.LinkedHashMap;
import java.util.UUID;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

/**
 * Must be synchronized to the client on updates/players joining level
 */
public class CircleManager extends SavedData {

  private static final Factory<CircleManager> FACTORY = new Factory<CircleManager>(
      CircleManager::new, CircleManager::build, null);

  public CircleNameGenerator generator = null;
  private LinkedHashMap<UUID, CircleRecord> records;

  public CircleManager() {

    this.records = new LinkedHashMap<>();
  }

  public CircleManager(LinkedHashMap<UUID, CircleRecord> records) {

    this.records = new LinkedHashMap<>(records);
  }

  public static CircleManager build(CompoundTag compoundTag, Provider provider) {

    return new CircleManager(CircleManagerSerialization.loadRecords(compoundTag));
  }

  /**
   * Has the side effect of initializing our name generator, and blacklisting used names.
   */
  public static CircleManager getState(final MinecraftServer server) {

    ServerLevel overworld = server.getLevel(ServerLevel.OVERWORLD);

    if (overworld == null) {
      throw new IllegalStateException(
          CircleManager.class.getSimpleName() + " was unable to retrieve Level.OVERWORLD");
    }

    CircleManager manager = overworld.getDataStorage()
        .computeIfAbsent(FACTORY, Constants.MOD_ID + "_data");

    long seed = overworld.getSeed();
    Constants.debug("Constructing new name generator instance with overworld seed {}", seed);
    manager.generator = new CircleNameGenerator(seed);

    Constants.debug("Adding used names from records to name generator blacklist");
    manager.getRecords().forEach((id, record) -> manager.generator.addName(record.name()));

    manager.setDirty();

    return manager;
  }

  @Override
  public CompoundTag save(CompoundTag tag, Provider registries) {
    return CircleManagerSerialization.storeRecords(this.records);
  }

  public LinkedHashMap<UUID, CircleRecord> getRecords() {
    return records;
  }

  /**
   * Called when handling sync packet on client to start with a fresh map
   */
  public CircleManager newMap() {
    this.records = new LinkedHashMap<>();
    return this;
  }

  public void addRecord(UUID uuid, CircleRecord record) {
    records.put(uuid, record);
  }

  @Override
  public String toString() {
    return "CircleManager{" +
        "records=" + records +
        '}';
  }

  public void dereference(final UUID uuid, String name) {
    this.records.remove(uuid);
    this.generator.releaseName(name);
  }
}
