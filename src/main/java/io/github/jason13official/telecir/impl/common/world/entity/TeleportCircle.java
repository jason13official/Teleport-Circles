package io.github.jason13official.telecir.impl.common.world.entity;

import io.github.jason13official.telecir.Constants;
import io.github.jason13official.telecir.TeleCirServer;
import io.github.jason13official.telecir.impl.client.screen.LocationTeleportScreen;
import io.github.jason13official.telecir.impl.common.network.packet.ManagerSyncS2CPacket;
import io.github.jason13official.telecir.impl.server.data.CircleRecord;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class TeleportCircle extends AbstractTeleportCircle {

  public static final EntityDataAccessor<Boolean> ACTIVATED = SynchedEntityData.defineId(
      TeleportCircle.class, EntityDataSerializers.BOOLEAN);

  public TeleportCircle(EntityType<?> entityType,
      Level level) {
    super(entityType, level);
    this.blocksBuilding = true;
  }

  @Override
  protected void defineSynchedData(Builder builder) {
    builder.define(ACTIVATED, false);
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag compound) {
    this.activated(compound.getBoolean("activated"));
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag compound) {
    compound.putBoolean("activated", this.activated());
  }

  /// getter
  public final boolean activated() {
    return this.entityData.get(ACTIVATED);
  }

  /// setter
  public final void activated(boolean value) {
    this.entityData.set(ACTIVATED, value);
  }

  @Override
  public void kill() {

    if (!this.level().isClientSide()) {
      TeleCirServer.getManager().dereference(this.getUUID());
    }

    super.kill();

    if (this.level() instanceof ServerLevel serverLevel) {
      serverLevel.getServer().getAllLevels().forEach(level -> {
        level.players().forEach(serverPlayer -> {
          ManagerSyncS2CPacket.createAndSend(serverPlayer, level);
        });
      });
    }
  }

  @Override
  public InteractionResult interact(Player player, InteractionHand hand) {

    if (!this.level().isClientSide) {


      if (!this.activated()) {
        this.activated(true);

        String name = TeleCirServer.getManager().generateUniqueName();

        // also sets the mapping on the logical side
        this.setCustomName(Component.literal(name));

        if (this.level() instanceof ServerLevel serverLevel) {
          serverLevel.getServer().getAllLevels().forEach(level -> {
            level.players().forEach(serverPlayer -> {
              ManagerSyncS2CPacket.createAndSend(serverPlayer, level);
            });
          });
        }
      }

      Constants.debug("interacted with teleport circle on logical side successfully");
      return InteractionResult.PASS;
    } else {

      Minecraft.getInstance().setScreen(new LocationTeleportScreen(this.getUUID(), this.getName().getString()));

      return InteractionResult.SUCCESS;
    }
  }

  @Override
  public void setCustomName(@Nullable Component name) {
    super.setCustomName(name);

    if (name == null || this.level().isClientSide()) {
      return;
    }

    if (TeleCirServer.getInstance() == null) {
      TeleCirServer.PRELOAD.put(this.getUUID(), new CircleRecord(name.getString(), this.level().dimension(), this.position(), this.activated()));
      Constants.debug("Added circle to preload map");
    } else {
      TeleCirServer.getManager().setMapping(this.getUUID(), new CircleRecord(name.getString(), this.level().dimension(), this.position(), this.activated()));
    }
  }

  @Override
  public void tick() {
    super.tick();
    if (activated()) {
      this.setYRot((this.getYRot() + 1.0f) % 360.0f);
    }
  }
}
