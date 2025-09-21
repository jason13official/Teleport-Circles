package io.github.jason13official.telecir.impl.common.registry;

import io.github.jason13official.telecir.impl.common.entity.TeleportCircle;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {

  public static final EntityType<TeleportCircle> CIRCLE = EntityType.Builder.<TeleportCircle>of(
          TeleportCircle::new, MobCategory.MISC)
      .sized(3.0f, 0.0625f)
      .fireImmune()
      .updateInterval(Integer.MAX_VALUE)
      .build(null);
}
