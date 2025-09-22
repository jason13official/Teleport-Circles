package io.github.jason13official.telecir.impl.common.network.packet;

import io.github.jason13official.telecir.TeleCir;
import io.github.jason13official.telecir.impl.server.network.handler.EntityRenameServerHandler;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record EntityRenameC2SPacket(UUID uuid, String name) implements CustomPacketPayload {

  public static final Type<EntityRenameC2SPacket> TYPE = new Type<>(
      TeleCir.identifier("rename"));
  public static final StreamCodec<RegistryFriendlyByteBuf, EntityRenameC2SPacket> CODEC = StreamCodec.ofMember(
      EntityRenameC2SPacket::write, EntityRenameC2SPacket::read);

  public static EntityRenameC2SPacket read(RegistryFriendlyByteBuf data) {

    UUID id = data.readUUID();

    int len = data.readVarInt();
    String name = String.valueOf(data.readCharSequence(len, StandardCharsets.UTF_8));

    return new EntityRenameC2SPacket(id, name);
  }

  public static void createAndSend(UUID uuid, String name) {
    ClientPlayNetworking.send(new EntityRenameC2SPacket(uuid, name));
  }

  public void write(RegistryFriendlyByteBuf data) {

    data.writeUUID(this.uuid());

    int len = this.name().length();
    data.writeVarInt(len);
    data.writeCharSequence(this.name(), StandardCharsets.UTF_8);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public void handleOnServer(ServerPlayNetworking.Context context) {
    EntityRenameServerHandler.handle(this, context);
  }
}
