package io.github.jason13official.telecir.impl.server.network.handler;

import io.github.jason13official.telecir.impl.common.entity.TeleportCircle;
import io.github.jason13official.telecir.impl.common.network.packet.EntityRenameC2SPacket;
import io.github.jason13official.telecir.impl.common.network.packet.ManagerSyncS2CPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;

public class EntityRenameServerHandler {

  public static void handle(EntityRenameC2SPacket packet, ServerPlayNetworking.Context context) {

    context.server().getAllLevels().forEach(level -> {

      if (level.getEntity(packet.uuid()) instanceof TeleportCircle circle) {
        circle.setCustomName(Component.literal(packet.name()));

        ManagerSyncS2CPacket.createAndSend(context.player(), context.player().serverLevel());
      }
    });
  }

}
