package io.github.jason13official.telecir.impl.server.logic;

import io.github.jason13official.telecir.impl.server.data.CircleRecord;
import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * Must be synchronized to the client on updates/players joining level
 */
public class CircleManager {

  private LinkedHashMap<UUID, CircleRecord> records;

  public CircleManager() {
    this.records = new LinkedHashMap<>();
  }

  public LinkedHashMap<UUID, CircleRecord> getRecords() {
    return records;
  }

  /** Called when handling sync packet on client to start with a fresh map */
  public CircleManager newMap() {
    this.records = new LinkedHashMap<>();
    return this;
  }

  public void setMapping(UUID uuid, CircleRecord record) {
    records.put(uuid, record);
  }
}
