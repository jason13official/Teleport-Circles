package io.github.jason13official.telecir.impl.server.data;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import oshi.util.tuples.Triplet;

/**
 * All data required to recreate a named and activated circle in any dimension at a given position.
 *
 * @param name      the name of the circle
 * @param dimension the dimension the circle is located at
 * @param position  the specific position the circle is centered on
 * @param activated whether the circle has been activated or not
 */
public record CircleRecord(String name, String dimension, Triplet<Double, Double, Double> position,
                           Boolean activated) {

  /**
   * An alternative to the canonical constructor, which accepts the original values
   * ({@link ResourceKey} and {@link Vec3}) to be encoded into a new record.
   */
  public CircleRecord(String name, ResourceKey<Level> dimension, Vec3 position, Boolean activated) {
    this(name, dimension.location().toString(), new Triplet<>(position.x, position.y, position.z),
        activated);
  }
}
