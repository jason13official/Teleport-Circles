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


public class CircleManager extends UniqueNameGenerator {

  private static final Factory<CircleManager> FACTORY = new Factory<CircleManager>(
      CircleManager::new, CircleManager::build, null);

  private LinkedHashMap<UUID, CircleRecord> records;

  private CircleManager() {
    this(TeleCirServer.getSeed());
  }

  public CircleManager(long seed) {
    this(seed, new LinkedHashMap<>());
  }

  public CircleManager(long seed, LinkedHashMap<UUID, CircleRecord> records) {
    super(seed);
    this.records = records;
  }

  public static CircleManager build(CompoundTag compoundTag, Provider provider) {
    return new CircleManager(TeleCirServer.getSeed(),
        CircleRecordStorageHelper.loadCircles(compoundTag));
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
    blacklistName(record.name());
    records.put(id, record);
  }

  public CircleManager dereference() {
    this.records = new LinkedHashMap<>();
    return this;
  }

  @Override
  public CompoundTag save(CompoundTag tag, Provider registries) {
    return CircleRecordStorageHelper.storeCircles(this.records);
  }
}
