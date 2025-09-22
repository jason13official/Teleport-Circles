package io.github.jason13official.telecir;

import io.github.jason13official.telecir.impl.client.model.CircleModel;
import io.github.jason13official.telecir.impl.client.renderer.CircleRenderer;
import io.github.jason13official.telecir.impl.common.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class TeleCirClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    long start = System.currentTimeMillis();
    Constants.debug("Began client initialization.");

    EntityRendererRegistry.register(ModEntities.CIRCLE, CircleRenderer::new);

    EntityModelLayerRegistry.registerModelLayer(Constants.CIRCLE_MODEL_LAYER,
        CircleModel::createBodyLayer);

    Constants.debug("Ended client initialization.");
    long total = System.currentTimeMillis() - start;
    Constants.debug("Client initialized in {}ms", total);
  }
}