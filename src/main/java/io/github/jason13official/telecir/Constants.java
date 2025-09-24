package io.github.jason13official.telecir;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

  public static final String MOD_ID = "telecir";
  public static final String MOD_NAME = "Teleport Circles";
  public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

  public static final ModelLayerLocation CIRCLE_MODEL_LAYER = new ModelLayerLocation(
      TeleCir.identifier("circle"), "main");

  public static final ResourceLocation CIRCLE_DEBUG_TEXTURE = TeleCir.identifier(
      "textures/entity/debug.png");

  public static final ResourceLocation CIRCLE_INACTIVE_TEXTURE = TeleCir.identifier(
      "textures/entity/small_circle.png");

  public static final ResourceLocation CIRCLE_ACTIVE_TEXTURE = TeleCir.identifier(
      "textures/entity/circle.png");

  @SuppressWarnings("all")
  public static void debug(String message, Object obj1, Object obj2) {

    if (TeleCir.DEBUG) {
      LOG.info("[TeleCir] " + message, obj1, obj2);
    }
  }

  @SuppressWarnings("all")
  public static void debug(String message, Object... varargs) {

    if (TeleCir.DEBUG) {
      LOG.info("[TeleCir] " + message, varargs);
    }
  }

  @SuppressWarnings("all")
  public static void debug(String message) {

    if (TeleCir.DEBUG) {
      LOG.info("[TeleCir] " + message);
    }
  }
}
