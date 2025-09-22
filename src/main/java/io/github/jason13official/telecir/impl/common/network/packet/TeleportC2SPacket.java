package io.github.jason13official.telecir.impl.common.network.packet;

import io.github.jason13official.telecir.TeleCir;
import io.github.jason13official.telecir.impl.server.network.handler.TeleportServerHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record TeleportC2SPacket(Long position) implements CustomPacketPayload {

  public static final Type<TeleportC2SPacket> TYPE = new Type<>(
      TeleCir.identifier("teleport"));
  public static final StreamCodec<RegistryFriendlyByteBuf, TeleportC2SPacket> CODEC = StreamCodec.ofMember(
      TeleportC2SPacket::write, TeleportC2SPacket::read);

  public static TeleportC2SPacket read(RegistryFriendlyByteBuf data) {
    return new TeleportC2SPacket(data.readLong());
  }

  public static void createAndSend(Long position) {
    ClientPlayNetworking.send(new TeleportC2SPacket(position));
  }

  public void write(RegistryFriendlyByteBuf data) {
    data.writeLong(this.position);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public void handleOnServer(ServerPlayNetworking.Context context) {
    TeleportServerHandler.handle(this, context);
  }
}
