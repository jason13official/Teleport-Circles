package io.github.jason13official.telecir.impl.common.entity;

import io.github.jason13official.telecir.Constants;
import io.github.jason13official.telecir.TeleCirClient;
import io.github.jason13official.telecir.TeleCirServer;
import io.github.jason13official.telecir.impl.client.screen.LocationTeleportScreen;
import io.github.jason13official.telecir.impl.common.network.packet.ManagerSyncS2CPacket;
import io.github.jason13official.telecir.impl.common.registry.ModEntities;
import io.github.jason13official.telecir.impl.server.data.CircleRecord;
import io.github.jason13official.telecir.impl.server.logic.CircleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class TeleportCircle extends AbstractTeleportCircle {

  public static final EntityDataAccessor<Boolean> ACTIVATED = SynchedEntityData.defineId(
      TeleportCircle.class, EntityDataSerializers.BOOLEAN);

  public TeleportCircle(final Level level) {
    this(ModEntities.CIRCLE, level);
  }

  public TeleportCircle(
      EntityType<? extends LivingEntity> entityType,
      Level level) {
    super(entityType, level);
  }

  @Override
  protected void defineSynchedData(Builder builder) {
    super.defineSynchedData(builder);
    builder.define(ACTIVATED, false);
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);
    compound.putBoolean("activated", this.activated());
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    this.setActivated(compound.getBoolean("activated"));
  }

  @Override
  public InteractionResult interact(Player aPlayer, InteractionHand hand) {

    if (activated() && aPlayer instanceof LocalPlayer player) {
      String name = TeleCirClient.SYNCED_CLIENT_MAP.get(this.getUUID()).name();
      Minecraft.getInstance().setScreen(new LocationTeleportScreen(this.getUUID(), name));
    }

    if (!activated() && aPlayer instanceof ServerPlayer player) {

      this.setActivated(true);

      CircleManager manager = TeleCirServer.getManager();

      String name = manager.generator.generateName();
      this.setCustomName(Component.literal(name));

      Constants.debug("attempting to create and send sync packet");
      Constants.debug(name);
      Constants.debug(String.valueOf(this.level().dimension()));
      Constants.debug(String.valueOf(this.blockPosition()));
      Constants.debug(String.valueOf(this.activated()));

      CircleRecord record = new CircleRecord(name, this.level().dimension(), this.blockPosition(), this.activated());
      manager.addRecord(this.getUUID(), record);

      // synch newly activated circle to all players
      player.serverLevel().getServer().getAllLevels().forEach(level -> level.players().forEach(otherPlayer -> {
        ManagerSyncS2CPacket.createAndSend(otherPlayer, level);
      }));
    }

    return InteractionResult.PASS;
  }

  public boolean activated() {
    return this.entityData.get(ACTIVATED);
  }

  public void setActivated(boolean value) {
    this.entityData.set(ACTIVATED, value);
  }
}
