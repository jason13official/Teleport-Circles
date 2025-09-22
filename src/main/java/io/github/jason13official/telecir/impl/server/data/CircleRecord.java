package io.github.jason13official.telecir.impl.server.data;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public record CircleRecord(String name, String dimension, Long position, Boolean activated) {

  // alternative to canonical constructor
  public CircleRecord(String name, ResourceKey<Level> dimension, BlockPos position,
      Boolean activated) {
    this(name, dimension.location().toString(), position.asLong(), activated);
  }

//  public CircleRecord {
//    if (List.of(this.name(), this.dimension(), this.position(), this.activated()).contains(null)) {
//      throw new IllegalStateException("Cannot accept null for any parameter of CircleRecord");
//    }
//  }

  // alternative to canonical constructor
  public static CircleRecord of(String name, ResourceKey<Level> dimension, BlockPos position,
      Boolean activated) {
    return new CircleRecord(name, dimension.location().toString(), position.asLong(), activated);
  }
}
