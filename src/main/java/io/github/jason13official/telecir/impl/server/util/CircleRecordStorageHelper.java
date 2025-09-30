package io.github.jason13official.telecir.impl.server.util;

import io.github.jason13official.telecir.impl.server.data.CircleRecord;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import oshi.util.tuples.Triplet;

public class CircleRecordStorageHelper {

  private static CompoundTag toCompoundTag(CircleRecord record) {
    CompoundTag tag = new CompoundTag();
    tag.putString("name", record.name());
    tag.putDouble("x", record.position().getA());
    tag.putDouble("y", record.position().getB());
    tag.putDouble("z", record.position().getC());
    tag.putBoolean("activated", record.activated());
    tag.putString("dimension", record.dimension());
    return tag;
  }

  private static CircleRecord fromCompoundTag(CompoundTag tag) {
    String name = tag.getString("name");
    String dimension = tag.getString("dimension");
    Triplet<Double, Double, Double> position = new Triplet<>(tag.getDouble("x"), tag.getDouble("y"),
        tag.getDouble("z"));
    Boolean activated = tag.getBoolean("activated");
    return new CircleRecord(name, dimension, position, activated);
  }

  public static CompoundTag storeCircles(LinkedHashMap<UUID, CircleRecord> circleMap) {
    CompoundTag rootTag = new CompoundTag();
    CompoundTag dataTag = new CompoundTag();

    int index = 0;
    for (Map.Entry<UUID, CircleRecord> entry : circleMap.entrySet()) {
      CompoundTag entryTag = new CompoundTag();
      entryTag.putUUID("uuid", entry.getKey());
      entryTag.put("circle", toCompoundTag(entry.getValue()));

      dataTag.put(String.valueOf(index), entryTag);
      index++;
    }

    rootTag.put("circles", dataTag);
    rootTag.putInt("count", circleMap.size());
    return rootTag;
  }

  public static LinkedHashMap<UUID, CircleRecord> loadCircles(CompoundTag rootTag) {
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
        CircleRecord record = fromCompoundTag(circleTag);
        circleMap.put(uuid, record);
      }
    }

    return circleMap;
  }
}