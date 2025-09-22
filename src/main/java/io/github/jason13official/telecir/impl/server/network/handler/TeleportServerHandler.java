package io.github.jason13official.telecir.impl.server.network.handler;

import io.github.jason13official.telecir.impl.common.network.packet.TeleportC2SPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;

public class TeleportServerHandler {

  public static void handle(TeleportC2SPacket packet, ServerPlayNetworking.Context context) {
    BlockPos pos = BlockPos.of(packet.position());
    context.player().teleportTo(pos.getX(), pos.getY(), pos.getZ());
  }
}
