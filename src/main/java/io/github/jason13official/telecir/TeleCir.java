package io.github.jason13official.telecir;

import io.github.jason13official.telecir.impl.common.registry.ModEntities;
import io.github.jason13official.telecir.impl.common.world.entity.TeleportCircle;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;

public class TeleCir implements ModInitializer {

  public static boolean DEBUG = FabricLoader.getInstance().isDevelopmentEnvironment();

  public static ResourceLocation identifier(String path) {
    return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
  }

  @Override
  public void onInitialize() {
    long start = System.currentTimeMillis();
    Constants.debug("Began common initialization.");

    Registry.register(BuiltInRegistries.ENTITY_TYPE, identifier("circle"), ModEntities.CIRCLE);

    ServerTickEvents.START_SERVER_TICK.register(TeleCirServer::initializeOnFirstTick);
    ServerLifecycleEvents.SERVER_STOPPING.register(server -> TeleCirServer.dereference());

    Constants.debug("Ended common initialization.");
    long total = System.currentTimeMillis() - start;
    Constants.debug("Common initialized in {}ms", total);
  }
}