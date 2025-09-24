package io.github.jason13official.telecir.impl.common.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractTeleportCircle extends Entity {

  public AbstractTeleportCircle(EntityType<?> entityType,
      Level level) {
    super(entityType, level);
  }

  @Override
  protected boolean canRide(Entity vehicle) {
    return false;
  }

  @Override
  protected boolean canAddPassenger(Entity passenger) {
    return false;
  }

  @Override
  public boolean isPickable() {
    return !this.isRemoved();
  }

  //SECTION movement overrides

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
}
