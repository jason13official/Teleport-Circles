package io.github.jason13official.telecir.impl.common.network.packet;

import io.github.jason13official.telecir.TeleCir;
import io.github.jason13official.telecir.impl.server.network.handler.EntityRenameServerHandler;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerPacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public record EntityRenameC2SPacket(UUID uuid, String name) implements FabricPacket {

//  public static final Type<EntityRenameC2SPacket> TYPE = new Type<>(
//      TeleCir.identifier("rename"));
//  public static final StreamCodec<FriendlyByteBuf, EntityRenameC2SPacket> CODEC = StreamCodec.ofMember(
//      EntityRenameC2SPacket::write, EntityRenameC2SPacket::read);

  public static EntityRenameC2SPacket read(FriendlyByteBuf data) {

    UUID id = data.readUUID();

    int len = data.readVarInt();
    String name = String.valueOf(data.readCharSequence(len, StandardCharsets.UTF_8));

    return new EntityRenameC2SPacket(id, name);
  }

  public static void createAndSend(UUID uuid, String name) {
    ClientPlayNetworking.send(new EntityRenameC2SPacket(uuid, name));
  }

  public void write(FriendlyByteBuf data) {

    data.writeUUID(this.uuid());

    int len = this.name().length();
    data.writeVarInt(len);
    data.writeCharSequence(this.name(), StandardCharsets.UTF_8);
  }

  @Override
  public PacketType<?> getType() {
    return null;
  }

  public static void handleOnServer(FabricPacket fabricPacket, ServerPlayer player,
      PacketSender packetSender) {
    EntityRenameServerHandler.handle(fabricPacket, player, packetSender);
  }

  //  @Override
//  public Type<? extends CustomPacketPayload> type() {
//    return TYPE;
//  }

//  public void handleOnServer(MinecraftServer server) {
//    EntityRenameServerHandler.handle(this, server);
//  }
}
