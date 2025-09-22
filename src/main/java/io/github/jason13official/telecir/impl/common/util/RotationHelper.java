package io.github.jason13official.telecir.impl.common.util;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import oshi.util.tuples.Pair;

public class RotationHelper {

  /**
   * Rotates a bounding box around a center point using Euler angles
   *
   * @param minX,   minY, minZ - original bounding box minimum coordinates
   * @param maxX,   maxY, maxZ - original bounding box maximum coordinates
   * @param center  - center point to rotate around
   * @param angleX, angleY, angleZ - rotation angles in radians
   * @return new AABB that encompasses the rotated bounding box
   */
  public static AABB rotateBoundingBox(double minX, double minY, double minZ, double maxX,
      double maxY, double maxZ, Vec3 center, double angleX, double angleY, double angleZ) {

    // Rotate each corner around the center
    Vec3[] rotatedCorners = rotateAABBCorners(minX, minY, minZ, maxX, maxY, maxZ, center, angleX,
        angleY, angleZ);

    // Find new min/max values
    double newMinX = Double.MAX_VALUE;
    double newMinY = Double.MAX_VALUE;
    double newMinZ = Double.MAX_VALUE;
    double newMaxX = Double.MIN_VALUE;
    double newMaxY = Double.MIN_VALUE;
    double newMaxZ = Double.MIN_VALUE;

    for (Vec3 corner : rotatedCorners) {
      newMinX = Math.min(newMinX, corner.x);
      newMinY = Math.min(newMinY, corner.y);
      newMinZ = Math.min(newMinZ, corner.z);
      newMaxX = Math.max(newMaxX, corner.x);
      newMaxY = Math.max(newMaxY, corner.y);
      newMaxZ = Math.max(newMaxZ, corner.z);
    }

    return new AABB(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
  }

  /**
   * Gets the rotated vertices of a bounding box for particle rendering
   */
  public static Vec3[] getRotatedAABBVertices(double minX, double minY, double minZ, double maxX,
      double maxY, double maxZ, Vec3 center, double angleX, double angleY, double angleZ) {
    return rotateAABBCorners(minX, minY, minZ, maxX, maxY, maxZ, center, angleX, angleY, angleZ);
  }

  /**
   * Rotates a bounding box and returns both the AABB and vertices in a single operation This is
   * more efficient than calling both methods separately as it avoids duplicate calculations
   *
   * @return Pair containing the rotated AABB and the rotated vertices
   */
  public static Pair<AABB, Vec3[]> rotateBoundingBoxWithVertices(double minX, double minY,
      double minZ,
      double maxX, double maxY, double maxZ, Vec3 center, double angleX, double angleY,
      double angleZ) {

    Vec3[] rotatedCorners = rotateAABBCorners(minX, minY, minZ, maxX, maxY, maxZ, center, angleX,
        angleY, angleZ);

    // Find new min/max values from the rotated corners
    double newMinX = Double.MAX_VALUE;
    double newMinY = Double.MAX_VALUE;
    double newMinZ = Double.MAX_VALUE;
    double newMaxX = Double.MIN_VALUE;
    double newMaxY = Double.MIN_VALUE;
    double newMaxZ = Double.MIN_VALUE;

    for (Vec3 corner : rotatedCorners) {
      newMinX = Math.min(newMinX, corner.x);
      newMinY = Math.min(newMinY, corner.y);
      newMinZ = Math.min(newMinZ, corner.z);
      newMaxX = Math.max(newMaxX, corner.x);
      newMaxY = Math.max(newMaxY, corner.y);
      newMaxZ = Math.max(newMaxZ, corner.z);
    }

    AABB rotatedBoundingBox = new AABB(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    return new Pair<>(rotatedBoundingBox, rotatedCorners);
  }

  /**
   * Generates and rotates all 8 corner points of a bounding box
   */
  public static Vec3[] rotateAABBCorners(double minX, double minY, double minZ, double maxX,
      double maxY, double maxZ, Vec3 center, double angleX, double angleY, double angleZ) {
    // Generate all 8 corner points of the bounding box
    Vec3[] corners = {new Vec3(minX, minY, minZ), new Vec3(maxX, minY, minZ),
        new Vec3(minX, maxY, minZ), new Vec3(maxX, maxY, minZ), new Vec3(minX, minY, maxZ),
        new Vec3(maxX, minY, maxZ), new Vec3(minX, maxY, maxZ), new Vec3(maxX, maxY, maxZ)};

    // Rotate each corner around the center
    Vec3[] rotatedCorners = new Vec3[8];
    for (int i = 0; i < corners.length; i++) {
      rotatedCorners[i] = rotatePointAroundCenter(corners[i], center, angleX, angleY, angleZ);
    }

    return rotatedCorners;
  }

  /**
   * Rotate a point around a center using Euler angles (in radians) Order: X, then Y, then Z
   * rotation
   */
  public static Vec3 rotatePointAroundCenter(Vec3 point, Vec3 center, double angleX, double angleY,
      double angleZ) {
    // Translate point to origin (relative to center)
    Vec3 translated = point.subtract(center);

    // Apply rotations in order: X, Y, Z
    Vec3 rotated = rotateX(translated, angleX);
    rotated = rotateY(rotated, angleY);
    rotated = rotateZ(rotated, angleZ);

    // Translate back
    return rotated.add(center);
  }

  // Rotate around X-axis
  public static Vec3 rotateX(Vec3 point, double angle) {
    double cos = Math.cos(angle);
    double sin = Math.sin(angle);

    return new Vec3(point.x, point.y * cos - point.z * sin, point.y * sin + point.z * cos);
  }

  // Rotate around Y-axis
  public static Vec3 rotateY(Vec3 point, double angle) {
    double cos = Math.cos(angle);
    double sin = Math.sin(angle);

    return new Vec3(point.x * cos + point.z * sin, point.y, -point.x * sin + point.z * cos);
  }

  // Rotate around Z-axis
  public static Vec3 rotateZ(Vec3 point, double angle) {
    double cos = Math.cos(angle);
    double sin = Math.sin(angle);

    return new Vec3(point.x * cos - point.y * sin, point.x * sin + point.y * cos, point.z);
  }

}
