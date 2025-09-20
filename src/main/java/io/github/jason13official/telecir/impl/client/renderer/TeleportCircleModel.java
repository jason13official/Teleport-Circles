package io.github.jason13official.telecir.impl.client.renderer;

import io.github.jason13official.telecir.impl.common.entity.TeleportCircle;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

public class TeleportCircleModel extends HierarchicalModel<TeleportCircle> {

  private final ModelPart root;
  private final ModelPart body;

  public TeleportCircleModel(final ModelPart origin) {
    super(RenderType::entityCutout);
    this.root = origin.getChild("root");

    this.body = this.root.getChild("body");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(),
        PartPose.offset(0, 0, 0));

    PartDefinition body = root.addOrReplaceChild("body",
        CubeListBuilder.create().texOffs(0, 0)
            .addBox(-24.0F, 0.0F, -24.0F, 48.0F, 1.0F, 48.0F, new CubeDeformation(0.0F)),
        PartPose.offset(0.0F, 24.0F, 0.0F));

    return LayerDefinition.create(meshdefinition, 192, 192);
  }

  @Override
  public ModelPart root() {
    return this.root;
  }

  @Override
  public void setupAnim(TeleportCircle circle, float v, float v1, float v2, float v3, float v4) {
    // no-op
  }
}
