package io.github.jason13official.telecir.impl.server.logic;

import io.github.jason13official.telecir.Constants;
import io.github.jason13official.telecir.TeleCirServer;
import io.github.jason13official.telecir.impl.server.data.CircleRecord;
import io.github.jason13official.telecir.impl.server.util.CircleRecordStorageHelper;
import java.util.LinkedHashMap;
import java.util.UUID;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

/**
 * CircleManager instances should be directly linked to the server instance of the mod
 */
public class CircleManager extends UniqueNameGenerator {

  private static final Factory<CircleManager> FACTORY = new Factory<CircleManager>(
      CircleManager::new, CircleManager::build, null);

  private LinkedHashMap<UUID, CircleRecord> records;

  private CircleManager() {
    this(TeleCirServer.getSeed());

    Constants.debug("Created new CircleManager instance with server seed");
  }

  public CircleManager(long seed) {
    this(seed, new LinkedHashMap<>());

    Constants.debug("Created new CircleManager instance with server seed and empty map");
  }

  public CircleManager(long seed, LinkedHashMap<UUID, CircleRecord> records) {
    super(seed);
    this.records = records;

    Constants.debug("Created new CircleManager instance with server seed and filled map");
  }

  public static CircleManager build(CompoundTag compoundTag, Provider provider) {

    Constants.debug("Building new CircleManager instance from server seed and CompoundTag");

    LinkedHashMap<UUID, CircleRecord> map = CircleRecordStorageHelper.loadCircles(compoundTag);

    Constants.debug("empty map in memory????" + map.isEmpty());
    Constants.debug("Final Re-Initialized Map: ", map);

//    map.forEach((id, record) -> {
//      this.blacklistNameForGenerator(record.name());
//    });

    var manager = new CircleManager(TeleCirServer.getSeed(), map);

    map.forEach((id, record) -> {
      if (!manager.usedNames.contains(record.name())) manager.blacklistNameForGenerator(record.name());
    });

    return manager;
  }

  public static CircleManager getState(final MinecraftServer server) {

    ServerLevel overworld = server.getLevel(ServerLevel.OVERWORLD);

    assert overworld != null;
    CircleManager state = overworld.getDataStorage().computeIfAbsent(FACTORY, Constants.MOD_ID);
    state.setDirty();

    return state;
  }

  public LinkedHashMap<UUID, CircleRecord> getRecords() {
    return records;
  }

  public void setMapping(UUID id, CircleRecord record) {
    blacklistNameForGenerator(record.name());
    records.put(id, record);

    Constants.debug("mapped {} {}", id, record);
  }

  public CircleManager dereference() {
    this.records = new LinkedHashMap<>();
    return this;
  }

  public void dereference(UUID id) {
    if (this.records.containsKey(id)) {

      // remove returns the last mapped value. we checked that the record existed,
      // so we should expect a non-null return
      CircleRecord record = this.records.remove(id);

      this.releaseName(record.name());

      Constants.debug("dereferenced {} {}", id, record);
    }
  }

  @Override
  public CompoundTag save(CompoundTag tag, Provider registries) {
    return CircleRecordStorageHelper.storeCircles(this.records);
  }
}
