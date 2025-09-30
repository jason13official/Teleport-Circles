package io.github.jason13official.telecir.impl.common.network.packet;

import io.github.jason13official.telecir.TeleCir;
import io.github.jason13official.telecir.impl.server.network.handler.TeleportServerHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import oshi.util.tuples.Triplet;

public record TeleportC2SPacket(Triplet<Double, Double, Double> position) implements FabricPacket {

//  public static final Type<TeleportC2SPacket> TYPE = new Type<>(
//      TeleCir.identifier("teleport"));
//  public static final StreamCodec<FriendlyByteBuf, TeleportC2SPacket> CODEC = StreamCodec.ofMember(
//      TeleportC2SPacket::write, TeleportC2SPacket::read);

  public static TeleportC2SPacket read(FriendlyByteBuf data) {
    return new TeleportC2SPacket(new Triplet<>(data.readDouble(), data.readDouble(), data.readDouble()));
  }

  public static void createAndSend(Triplet<Double, Double, Double> position) {
    ClientPlayNetworking.send(new TeleportC2SPacket(position));
  }

  public void write(FriendlyByteBuf data) {
    data.writeDouble(this.position.getA());
    data.writeDouble(this.position.getB());
    data.writeDouble(this.position.getC());
  }

  @Override
  public PacketType<?> getType() {
    return null;
  }

  public static void handleOnServer(FabricPacket fabricPacket, ServerPlayer player,
      PacketSender packetSender) {
    TeleportServerHandler.handle(fabricPacket, player, packetSender);
  }

  //  @Override
//  public Type<? extends CustomPacketPayload> type() {
//    return TYPE;
//  }

//  public void handleOnServer(ServerPlayNetworking.Context context) {
//    TeleportServerHandler.handle(this, context);
//  }
}
