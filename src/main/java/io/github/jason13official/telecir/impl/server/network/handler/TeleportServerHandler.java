package io.github.jason13official.telecir.impl.server.network.handler;

import io.github.jason13official.telecir.impl.common.network.packet.TeleportC2SPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class TeleportServerHandler {

  public static void handle(TeleportC2SPacket packet, ServerPlayNetworking.Context context) {
    Vec3 pos = new Vec3(packet.position().getA(), packet.position().getB(), packet.position().getC());
    context.player().teleportTo(pos.x(), pos.y(), pos.z());
  }
}
