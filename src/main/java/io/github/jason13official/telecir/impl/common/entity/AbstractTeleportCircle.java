package io.github.jason13official.telecir.impl.common.entity;

import io.github.jason13official.telecir.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractTeleportCircle extends LivingEntity {

  public static final Iterable<ItemStack> ARMOR_ITEMS = NonNullList.withSize(4, ItemStack.EMPTY);

  public AbstractTeleportCircle(
      EntityType<? extends LivingEntity> entityType,
      Level level) {
    super(entityType, level);
  }

  @Override
  public final void kill() {

    // super.kill();
    this.remove(RemovalReason.DISCARDED);

    if (this.level() instanceof ServerLevel serverLevel) {
      Constants.debug("Began dereferencing circle {} {}", this.getDisplayName().getString(),
          this.getStringUUID());
      // TODO impl circle manager update and sync packet
      Constants.debug("Ended dereferencing circle {} {}", this.getDisplayName().getString(),
          this.getStringUUID());
    }
  }

  //SECTION damage and interaction overrides

  @Override
  public final boolean isAlwaysTicking() {
    return true;
  }

  @Override
  public final boolean shouldRender(double x, double y, double z) {
    return true;
  }

  @Override
  public final boolean shouldRenderAtSqrDistance(double distance) {
    return true;
  }

  @Override
  public final boolean isInvulnerable() {
    return false;
  }

  @Override
  public final boolean isInvulnerableTo(DamageSource source) {
    return true;
  }

  @Override
  public final boolean attackable() {
    return false;
  }

  @Override
  public final boolean skipAttackInteraction(Entity entity) {
    return true;
  }

  @Override
  public final boolean canBeHitByProjectile() {
    return false;
  }

  //END damage and interaction overrides

  //SECTION movement overrides

  @Override
  public final void travel(Vec3 travelVector) {

    // no-op
  }

  @Override
  public final void push(Entity entity) {

    // no-op
  }

  @Override
  public final void push(double x, double y, double z) {

    // no-op
  }

  @Override
  public final void push(Vec3 vector) {

    // no-op
  }

  @Override
  protected final void pushEntities() {

    // no-op
  }

  @Override
  public final boolean canBeCollidedWith() {

    return false;
  }

  @Override
  public final boolean canCollideWith(Entity entity) {

    return false;
  }

  @Override
  public final boolean isColliding(BlockPos pos, BlockState state) {

    return false;
  }

  @Override
  protected final boolean isHorizontalCollisionMinor(Vec3 deltaMovement) {

    return false;
  }

  //END movement overrides

  //SECTION LivingEntity @Override

  @Override
  public final Iterable<ItemStack> getArmorSlots() {

    return ARMOR_ITEMS;
  }

  @Override
  public final ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {

    return ItemStack.EMPTY;
  }

  @Override
  public final void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {

    // no-op
  }

  @Override
  public final HumanoidArm getMainArm() {

    return HumanoidArm.RIGHT;
  }

  //END LivingEntity @Override
}
