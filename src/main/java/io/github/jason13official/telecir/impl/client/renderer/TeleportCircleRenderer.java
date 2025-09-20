package io.github.jason13official.telecir.impl.client.renderer;

import io.github.jason13official.telecir.TeleCir;
import io.github.jason13official.telecir.impl.common.entity.TeleportCircle;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class TeleportCircleRenderer extends
    LivingEntityRenderer<TeleportCircle, TeleportCircleModel> {

  public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
      TeleCir.identifier("circle"),
      "main");

  private static final ResourceLocation DEBUG_LOCATION = TeleCir.identifier(
      "textures/entity/debug.png");

  private static final ResourceLocation SMALL_CIRCLE_LOCATION = TeleCir.identifier(
      "textures/entity/small_circle.png");

  private static final ResourceLocation CIRCLE_LOCATION = TeleCir.identifier(
      "textures/entity/circle.png");

  public TeleportCircleRenderer(Context context) {
    super(context, new TeleportCircleModel(context.bakeLayer(LAYER_LOCATION)), 0.0f);
  }

  @Override
  public ResourceLocation getTextureLocation(TeleportCircle circle) {
    if (!TeleCir.DEBUG) {
      return circle.activated() ? CIRCLE_LOCATION : SMALL_CIRCLE_LOCATION;
    } else {
      return DEBUG_LOCATION;
    }
  }

  @Override
  protected boolean shouldShowName(TeleportCircle entity) {
    return false;
  }

//  @Override
//  protected int getBlockLightLevel(TeleportCircle entity, BlockPos pos) {
//    return 15;
//  }
}
