package io.github.jason13official.telecir.impl.common.network.packet;

import io.github.jason13official.telecir.TeleCir;
import io.github.jason13official.telecir.TeleCirServer;
import io.github.jason13official.telecir.impl.client.network.handler.ManagerSyncClientHandler;
import io.github.jason13official.telecir.impl.server.data.CircleRecord;
import io.github.jason13official.telecir.impl.server.logic.CircleManager;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public record ManagerSyncS2CPacket(int size, UUID[] uuids, String[] names,
                                   Long[] positions, Boolean[] activated, String[] dimensions) implements CustomPacketPayload {

  public static final Type<ManagerSyncS2CPacket> TYPE = new Type<>(
      TeleCir.identifier("manager"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ManagerSyncS2CPacket> CODEC = StreamCodec.ofMember(
      ManagerSyncS2CPacket::write, ManagerSyncS2CPacket::read);

  public static ManagerSyncS2CPacket read(RegistryFriendlyByteBuf data) {

    int size = data.readVarInt();

    List<UUID> uuidList = new ArrayList<>();
    List<String> nameList = new ArrayList<>();
    List<Long> positionList = new ArrayList<>();
    List<Boolean> activatedList = new ArrayList<>();
    List<String> dimensionList = new ArrayList<>();

    for (int i = 0; i < size; i++) {
      uuidList.add(data.readUUID());
      int nameLength = data.readVarInt();
      nameList.add(String.valueOf(data.readCharSequence(nameLength, StandardCharsets.UTF_8)));
      positionList.add(data.readLong());
      activatedList.add(data.readBoolean());
      int dimensionLength = data.readVarInt();
      dimensionList.add(String.valueOf(data.readCharSequence(dimensionLength, StandardCharsets.UTF_8)));
    }

    return new ManagerSyncS2CPacket(size, uuidList.toArray(new UUID[0]),
        nameList.toArray(new String[0]), positionList.toArray(new Long[0]), activatedList.toArray(new Boolean[0]), dimensionList.toArray(new String[0]));
  }

  public static void createAndSend(Entity entity, ServerLevel level) {

    if (!(entity instanceof ServerPlayer player)) {
      return;
    }

    // when the logical side is synced to client, get the persistent state / saved data and populate
    // the circle manager before sending.
    MinecraftServer server = level.getServer();
    // SavedCircleManager.getState(server).records.forEach((id, record) -> TeleCir.getManager().setMapping(id, record));
    CircleManager.getState(server).getRecords().forEach((id, record) -> TeleCirServer.getManager().addRecord(id, record));

    CircleManager manager = TeleCirServer.getManager();
    LinkedHashMap<UUID, CircleRecord> map = manager.getRecords();

    int entries = map.size();

    UUID[] packIDs = map.keySet().toArray(new UUID[0]);

    List<String> names = new LinkedList<>();
    List<Long> positions = new LinkedList<>();
    List<Boolean> activated = new LinkedList<>();
    List<String> dimensions = new LinkedList<>();

    map.values().forEach(record -> {

      names.add(record.name());
      positions.add(record.position());
      activated.add(record.activated());
      dimensions.add(record.dimension());
    });

    ManagerSyncS2CPacket packet = new ManagerSyncS2CPacket(entries, packIDs,
        names.toArray(new String[0]), positions.toArray(new Long[0]), activated.toArray(new Boolean[0]), dimensions.toArray(new String[0]));

    ServerPlayNetworking.send(player, packet);
  }

  public void write(RegistryFriendlyByteBuf data) {

    data.writeVarInt(size());

    for (int i = 0; i < size(); i++) {
      data.writeUUID(uuids[i]);
      data.writeVarInt(names[i].length());
      data.writeCharSequence(names[i], StandardCharsets.UTF_8);
      data.writeLong(positions[i]);
      data.writeBoolean(activated[i]);
      data.writeVarInt(dimensions[i].length());
      data.writeCharSequence(dimensions[i], StandardCharsets.UTF_8);
    }
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public void handleOnClient(ClientPlayNetworking.Context context) {
    ManagerSyncClientHandler.handle(this, context);
  }
}
