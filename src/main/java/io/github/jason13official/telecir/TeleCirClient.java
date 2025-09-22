package io.github.jason13official.telecir;

import io.github.jason13official.telecir.impl.client.renderer.TeleportCircleModel;
import io.github.jason13official.telecir.impl.client.renderer.TeleportCircleRenderer;
import io.github.jason13official.telecir.impl.common.network.packet.ManagerSyncS2CPacket;
import io.github.jason13official.telecir.impl.common.registry.ModEntities;
import io.github.jason13official.telecir.impl.server.data.CircleRecord;
import java.util.LinkedHashMap;
import java.util.UUID;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class TeleCirClient implements ClientModInitializer {

  public static LinkedHashMap<UUID, CircleRecord> SYNCED_CLIENT_MAP = new LinkedHashMap<>();

  @Override
  public void onInitializeClient() {
    long start = System.currentTimeMillis();
    Constants.debug("Began client initialization.");

    EntityRendererRegistry.register(ModEntities.CIRCLE, TeleportCircleRenderer::new);
    EntityModelLayerRegistry.registerModelLayer(TeleportCircleRenderer.LAYER_LOCATION,
        TeleportCircleModel::createBodyLayer);

    ClientPlayNetworking.registerGlobalReceiver(ManagerSyncS2CPacket.TYPE,
        ManagerSyncS2CPacket::handleOnClient);

    Constants.debug("Ended client initialization.");
    long total = System.currentTimeMillis() - start;
    Constants.debug("Client initialized in {}ms", total);
  }
}