package io.github.jason13official.telecir.impl.client.network.handler;

import io.github.jason13official.telecir.TeleCirClient;
import io.github.jason13official.telecir.impl.common.network.packet.ManagerSyncS2CPacket;
import io.github.jason13official.telecir.impl.server.data.CircleRecord;
import java.util.Arrays;
import java.util.LinkedHashMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class ManagerSyncClientHandler {

  public static void handle(ManagerSyncS2CPacket packet, ClientPlayNetworking.Context context) {

    // CircleManager manager = TeleCir.getManager().newMap();
    TeleCirClient.SYNCED_CLIENT_MAP = new LinkedHashMap<>();
    for (int i = 0; i < packet.size(); i++) {

      String dimensionString = packet.dimensions()[i];

      String[] parts = splitResourceLocationSafe(dimensionString);

      // ResourceKey#create is supported by a ConcurrentMap, and utilizes #computeIfAbsent.
      ResourceKey<Level> levelResourceKey = ResourceKey.create(Registries.DIMENSION,
          ResourceLocation.fromNamespaceAndPath(parts[0], parts[1]));

      TeleCirClient.SYNCED_CLIENT_MAP.put(packet.uuids()[i],
          CircleRecord.of(packet.names()[i], levelResourceKey, BlockPos.of(packet.positions()[i]), packet.activated()[i]));
    }

//    if (Minecraft.getInstance().screen instanceof LocationTeleportScreen screen) {
//      screen.init(Minecraft.getInstance(), screen.width, screen.height);
//    }
  }

  public static String[] splitResourceLocationSafe(String resourceLocationString) {

    if (resourceLocationString == null || !resourceLocationString.contains(":")) {
      throw new IllegalArgumentException(
          "Invalid ResourceLocation format: " + resourceLocationString);
    }

    String[] parts = resourceLocationString.split(":",
        2); // Limit to 2 parts in case of multiple colons

    if (parts.length != 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
      throw new IllegalArgumentException(
          "Invalid ResourceLocation format: " + resourceLocationString);
    }

    return parts;
  }

}
