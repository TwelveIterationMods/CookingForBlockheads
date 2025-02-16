package net.blay09.mods.cookingforblockheads.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class OvenDoorModel extends Model {

    private final ModelPart root;

    public OvenDoorModel(ModelPart root) {
        super(RenderType::entitySolid);
        this.root = root;
    }

    public static LayerDefinition createLayer(CubeDeformation deformation) {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(0f, -10f, 0f, 12, 11, 1, deformation), PartPose.offset(-6f, 23f, -7f));
        partDefinition.addOrReplaceChild("bop_left", CubeListBuilder.create().texOffs(0, 0).addBox(2f, -10f, -1f, 1, 1, 1, deformation), PartPose.offset(-6f, 23f, -7f));
        partDefinition.addOrReplaceChild("bop_right", CubeListBuilder.create().texOffs(0, 0).addBox(9f, -10f, -1f, 1, 1, 1, deformation), PartPose.offset(-6f, 23f, -7f));
        partDefinition.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(0, 0).addBox(0f, -10f, -2f, 12, 1, 1, deformation), PartPose.offset(-6f, 23f, -7f));
        return LayerDefinition.create(meshDefinition, 64, 16);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

}
