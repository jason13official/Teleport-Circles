package io.github.jason13official.telecir.impl.client.model;

import io.github.jason13official.telecir.impl.common.world.entity.TeleportCircle;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

public class CircleModel extends HierarchicalModel<TeleportCircle> {

  private final ModelPart root;
  private final ModelPart body;

  public CircleModel(final ModelPart origin) {
    super(RenderType::entityCutout);

    this.root = origin.getChild("root");
    this.body = this.root.getChild("body");
  }

  @Override
  public ModelPart root() {
    return this.root;
  }

  @Override
  public void setupAnim(TeleportCircle entity, float limbSwing, float limbSwingAmount,
      float ageInTicks, float netHeadYaw, float headPitch) {
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();

    PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0, 0, 0));

    // offset for living entity
//    PartDefinition body = root.addOrReplaceChild("body",
//        CubeListBuilder.create().texOffs(0, 0)
//            .addBox(-24.0F, 0.0F, -24.0F, 48.0F, 1.0F, 48.0F, new CubeDeformation(0.0F)),
//        PartPose.offset(0.0F, 24.0F, 0.0F));

    // no offset for raw entity
    PartDefinition body = root.addOrReplaceChild("body",
        CubeListBuilder.create().texOffs(0, 0)
            .addBox(-24.0F, 0.0F, -24.0F, 48.0F, 1.0F, 48.0F, new CubeDeformation(0.0F)),
        PartPose.offset(0.0F, 0.0F, 0.0F));

    return LayerDefinition.create(meshdefinition, 192, 192);
  }
}
