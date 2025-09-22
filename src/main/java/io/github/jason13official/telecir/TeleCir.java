package io.github.jason13official.telecir;

import io.github.jason13official.telecir.impl.common.network.packet.EntityRenameC2SPacket;
import io.github.jason13official.telecir.impl.common.network.packet.ManagerSyncS2CPacket;
import io.github.jason13official.telecir.impl.common.network.packet.TeleportC2SPacket;
import io.github.jason13official.telecir.impl.common.registry.ModCommands;
import io.github.jason13official.telecir.impl.common.registry.ModEntities;
import io.github.jason13official.telecir.impl.common.registry.ModParticles;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class TeleCir implements ModInitializer {

  // TODO update before publishing
  public static boolean DEBUG = true;

  public static ResourceLocation identifier(String path) {
    return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
  }

  @Override
  public void onInitialize() {
    long start = System.currentTimeMillis();
    Constants.debug("Began common initialization.");

    Registry.register(BuiltInRegistries.PARTICLE_TYPE, identifier("circle"), ModParticles.CIRCLE);
    Registry.register(BuiltInRegistries.ENTITY_TYPE, identifier("circle"), ModEntities.CIRCLE);

    FabricDefaultAttributeRegistry.register(ModEntities.CIRCLE,
        LivingEntity.createLivingAttributes().build());

    CommandRegistrationCallback.EVENT.register(ModCommands::register);

    ServerTickEvents.START_SERVER_TICK.register(TeleCirServer::init);
    ServerLifecycleEvents.SERVER_STOPPING.register(TeleCirServer::dereference);

    PayloadTypeRegistry.playS2C().register(ManagerSyncS2CPacket.TYPE, ManagerSyncS2CPacket.CODEC);
    PayloadTypeRegistry.playC2S().register(EntityRenameC2SPacket.TYPE, EntityRenameC2SPacket.CODEC);
    PayloadTypeRegistry.playC2S().register(TeleportC2SPacket.TYPE, TeleportC2SPacket.CODEC);

    ServerPlayNetworking.registerGlobalReceiver(EntityRenameC2SPacket.TYPE,
        EntityRenameC2SPacket::handleOnServer);

    ServerPlayNetworking.registerGlobalReceiver(TeleportC2SPacket.TYPE,
        TeleportC2SPacket::handleOnServer);

    Constants.debug("Ended common initialization.");
    long total = System.currentTimeMillis() - start;
    Constants.debug("Common initialized in {}ms", total);
  }
}