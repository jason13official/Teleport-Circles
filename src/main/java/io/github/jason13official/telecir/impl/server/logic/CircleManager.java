package io.github.jason13official.telecir.impl.server.logic;

import io.github.jason13official.telecir.Constants;
import io.github.jason13official.telecir.impl.server.data.CircleRecord;
import io.github.jason13official.telecir.impl.server.util.CircleManagerSerialization;
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

  private LinkedHashMap<UUID, CircleRecord> records;

  public CircleManager() {

    this.records = new LinkedHashMap<>();
  }

  public CircleManager(LinkedHashMap<UUID, CircleRecord> records) {

    this.records = new LinkedHashMap<>(records);
  }

  public static CircleManager build(CompoundTag compoundTag, Provider provider) {
    return new CircleManager(CircleManagerSerialization.loadCircleMapWithUuidTags(compoundTag));
  }

  public static CircleManager getState(final MinecraftServer server) {

    ServerLevel overworld = server.getLevel(ServerLevel.OVERWORLD);

    if (overworld == null) {
      throw new IllegalStateException(
          CircleManager.class.getSimpleName() + " was unable to retrieve Level.OVERWORLD");
    }

    CircleManager state = overworld.getDataStorage()
        .computeIfAbsent(FACTORY, Constants.MOD_ID + "_data");

    state.setDirty();

    return state;
  }

  @Override
  public CompoundTag save(CompoundTag tag, Provider registries) {
    return CircleManagerSerialization.storeCircleMapWithUuidTags(this.records);
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

  public void setMapping(UUID uuid, CircleRecord record) {
    records.put(uuid, record);
  }

  @Override
  public String toString() {
    return "CircleManager{" +
        "records=" + records +
        '}';
  }
}
