package io.github.jason13official.telecir.impl.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class TeleportCircle extends AbstractTeleportCircle {

  public TeleportCircle(
      EntityType<? extends LivingEntity> entityType,
      Level level) {
    super(entityType, level);
  }

  public boolean activated() {
    return false;
  }
}
