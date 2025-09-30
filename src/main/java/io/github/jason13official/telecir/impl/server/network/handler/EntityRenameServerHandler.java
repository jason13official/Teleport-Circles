package io.github.jason13official.telecir.impl.server.network.handler;

import io.github.jason13official.telecir.impl.common.network.packet.EntityRenameC2SPacket;
import io.github.jason13official.telecir.impl.common.network.packet.ManagerSyncS2CPacket;
import io.github.jason13official.telecir.impl.common.world.entity.TeleportCircle;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerPacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class EntityRenameServerHandler {

  public static void handle(FabricPacket packet, ServerPlayer player,
      PacketSender packetSender) {

    if (!(packet instanceof EntityRenameC2SPacket(java.util.UUID uuid, String name))) {
      return;
    }

    player.server.getAllLevels().forEach(level -> {

      if (level.getEntity(uuid) instanceof TeleportCircle circle) {
        circle.setCustomName(Component.literal(name));

        ManagerSyncS2CPacket.createAndSend(player, level);
      }
    });
  }

}
