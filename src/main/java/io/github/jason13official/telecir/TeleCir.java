package io.github.jason13official.telecir;

import io.github.jason13official.telecir.impl.common.registry.ModEntities;
import io.github.jason13official.telecir.impl.common.util.ModConfiguration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

public class TeleCir implements ModInitializer {

  public static boolean DEBUG = false;

  public static ResourceLocation identifier(String path) {
    return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
  }

  private static void registerResourceManagerReloadListeners() {

    ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
        .registerReloadListener(new SimpleSynchronousResourceReloadListener() {

          @Override
          public ResourceLocation getFabricId() {
            return identifier("client_reload");
          }

          @Override
          public void onResourceManagerReload(ResourceManager resourceManager) {

            Constants.LOG.info("Reading or creating mod configuration on client side.");
            ModConfiguration.readOrCreateConfig();
          }
        });

    ResourceManagerHelper.get(PackType.SERVER_DATA)
        .registerReloadListener(new SimpleSynchronousResourceReloadListener() {
          
          @Override
          public ResourceLocation getFabricId() {
            return identifier("server_reload");
          }

          @Override
          public void onResourceManagerReload(ResourceManager resourceManager) {

            Constants.LOG.info("Reading or create mod configuration on logical side.");
            ModConfiguration.readOrCreateConfig();
          }
        });
  }

  @Override
  public void onInitialize() {
    long start = System.currentTimeMillis();
    Constants.debug("Began common initialization.");

    TeleCir.registerResourceManagerReloadListeners();

    Registry.register(BuiltInRegistries.ENTITY_TYPE, identifier("circle"), ModEntities.CIRCLE);

    ServerTickEvents.START_SERVER_TICK.register(TeleCirServer::initializeOnFirstTick);
    ServerLifecycleEvents.SERVER_STOPPING.register(server -> TeleCirServer.dereference());

    Constants.debug("Ended common initialization.");
    long total = System.currentTimeMillis() - start;
    Constants.debug("Common initialized in {}ms", total);
  }
}