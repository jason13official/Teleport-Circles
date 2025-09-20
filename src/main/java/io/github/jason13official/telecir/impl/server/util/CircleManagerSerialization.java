package io.github.jason13official.telecir.impl.server.util;

import io.github.jason13official.telecir.impl.server.data.CircleRecord;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;

public class CircleManagerSerialization {

  public static CompoundTag circleRecordToNbt(CircleRecord record) {
    CompoundTag tag = new CompoundTag();
    tag.putString("name", record.name());
    tag.putLong("position", record.position());
    tag.putBoolean("activated", record.activated());
    tag.putString("dimension", record.dimension());
    return tag;
  }

  public static CircleRecord circleRecordFromNbt(CompoundTag tag) {
    String name = tag.getString("name");
    Long position = tag.getLong("position");
    Boolean activated = tag.getBoolean("activated");
    String dimension = tag.getString("dimension");
    return new CircleRecord(name, dimension, position, activated);
  }

  public static CompoundTag storeRecords(
      LinkedHashMap<UUID, CircleRecord> circleMap) {
    CompoundTag rootTag = new CompoundTag();
    CompoundTag dataTag = new CompoundTag();

    int index = 0;
    for (Map.Entry<UUID, CircleRecord> entry : circleMap.entrySet()) {
      CompoundTag entryTag = new CompoundTag();
      entryTag.putUUID("uuid", entry.getKey());
      entryTag.put("circle", circleRecordToNbt(entry.getValue()));

      dataTag.put(String.valueOf(index), entryTag);
      index++;
    }

    rootTag.put("circles", dataTag);
    rootTag.putInt("count", circleMap.size());
    return rootTag;
  }

  public static LinkedHashMap<UUID, CircleRecord> loadRecords(CompoundTag rootTag) {
    LinkedHashMap<UUID, CircleRecord> circleMap = new LinkedHashMap<>();

    if (!rootTag.contains("circles")) {
      return circleMap;
    }

    CompoundTag dataTag = rootTag.getCompound("circles");
    int count = rootTag.getInt("count");

    for (int i = 0; i < count; i++) {
      String indexKey = String.valueOf(i);
      if (dataTag.contains(indexKey)) {
        CompoundTag entryTag = dataTag.getCompound(indexKey);
        UUID uuid = entryTag.getUUID("uuid");
        CompoundTag circleTag = entryTag.getCompound("circle");
        CircleRecord record = circleRecordFromNbt(circleTag);
        circleMap.put(uuid, record);
      }
    }

    return circleMap;
  }
}