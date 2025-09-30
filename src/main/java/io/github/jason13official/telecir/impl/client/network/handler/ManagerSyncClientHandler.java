package io.github.jason13official.telecir.impl.client.network.handler;

import io.github.jason13official.telecir.Constants;
import io.github.jason13official.telecir.TeleCirClient;
import io.github.jason13official.telecir.impl.common.network.packet.ManagerSyncS2CPacket;
import io.github.jason13official.telecir.impl.server.data.CircleRecord;
import java.util.Arrays;
import java.util.LinkedHashMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import io.github.jason13official.telecir.impl.client.screen.LocationTeleportScreen;
import net.minecraft.client.Minecraft;

public class ManagerSyncClientHandler {

  public static void handle(ManagerSyncS2CPacket packet) {

    TeleCirClient.synchronizedRecords = new LinkedHashMap<>();
    for (int i = 0; i < packet.size(); i++) {

      String dimensionString = packet.dimensions()[i];

      String[] parts = splitResourceLocationSafe(dimensionString);

      // ResourceKey#create is supported by a ConcurrentMap, and utilizes #computeIfAbsent.
      ResourceKey<Level> levelResourceKey = ResourceKey.create(Registries.DIMENSION,
          new ResourceLocation(parts[0], parts[1]));

      TeleCirClient.synchronizedRecords.put(packet.uuids()[i],
          new CircleRecord(packet.names()[i], levelResourceKey,
              new Vec3(packet.positions()[i].getA(), packet.positions()[i].getB(),
                  packet.positions()[i].getC()), packet.activated()[i]));
    }

    Constants.debug("Final Synced Map {}", TeleCirClient.synchronizedRecords);

    // Notify any open LocationTeleportScreen to refresh their LocationList
    refreshLocationListIfOpen();
  }

  private static void refreshLocationListIfOpen() {
    try {
      if (Minecraft.getInstance().screen instanceof LocationTeleportScreen screen) {
        Constants.debug("Refreshing LocationList after sync");
        screen.getLocationList().refreshEntries();
      }
    } catch (Exception e) {
      Constants.debug("Error refreshing LocationList: {}", e.getMessage());
    }
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
