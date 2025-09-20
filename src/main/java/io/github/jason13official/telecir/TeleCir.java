package io.github.jason13official.telecir;

import io.github.jason13official.telecir.impl.common.registry.ModEntities;
import io.github.jason13official.telecir.impl.common.registry.ModParticles;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class TeleCir implements ModInitializer {

  public static boolean DEBUG = false;

  public static ResourceLocation identifier(String path) {
    return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
  }

  @Override
  @SuppressWarnings("all")
  public void onInitialize() {
    Constants.LOG.info("Began common initialization.");

    Registry.register(BuiltInRegistries.PARTICLE_TYPE, identifier("circle"), ModParticles.CIRCLE);
    Registry.register(BuiltInRegistries.ENTITY_TYPE, identifier("circle"), ModEntities.CIRCLE);

    FabricDefaultAttributeRegistry.register(ModEntities.CIRCLE,
        LivingEntity.createLivingAttributes().build());

    ServerTickEvents.START_SERVER_TICK.register(TeleCirServer::init);
    ServerLifecycleEvents.SERVER_STOPPING.register(TeleCirServer::dereference);

    Constants.LOG.info("Ended common initialization.");
  }
}