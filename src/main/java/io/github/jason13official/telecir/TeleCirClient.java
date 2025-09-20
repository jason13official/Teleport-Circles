package io.github.jason13official.telecir;

import io.github.jason13official.telecir.impl.client.renderer.TeleportCircleModel;
import io.github.jason13official.telecir.impl.client.renderer.TeleportCircleRenderer;
import io.github.jason13official.telecir.impl.common.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class TeleCirClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    Constants.LOG.info("Began client initialization.");
    EntityRendererRegistry.register(ModEntities.CIRCLE, TeleportCircleRenderer::new);
    EntityModelLayerRegistry.registerModelLayer(TeleportCircleRenderer.LAYER_LOCATION,
        TeleportCircleModel::createBodyLayer);
    Constants.LOG.info("Ended client initialization.");
  }
}