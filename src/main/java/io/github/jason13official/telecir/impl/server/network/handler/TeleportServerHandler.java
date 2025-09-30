package io.github.jason13official.telecir.impl.server.network.handler;

import io.github.jason13official.telecir.impl.common.network.packet.TeleportC2SPacket;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class TeleportServerHandler {

  public static void handle(FabricPacket fabricPacket, ServerPlayer player,
      PacketSender packetSender) {

    if (!(fabricPacket instanceof TeleportC2SPacket packet)) {
      return;
    }

    Vec3 pos = new Vec3(packet.position().getA(), packet.position().getB(), packet.position().getC());
    player.teleportTo(pos.x(), pos.y(), pos.z());
  }
}
