package net.blay09.mods.cookingforblockheads.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class CounterDoorModel extends Model {

    private final ModelPart root;

    public CounterDoorModel(ModelPart root) {
        super(RenderType::entitySolid);
        this.root = root;
    }

    public static LayerDefinition createLayer(CubeDeformation deformation) {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(4, 0).addBox(0f, 0f, 0f, 12, 14, 1, deformation), PartPose.offset(-6f, 10f, -6f));
        partDefinition.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(0, 0).addBox(9f, 7f, -1f, 1, 2, 1, deformation), PartPose.offset(-6f, 8f, -6f));
        return LayerDefinition.create(meshDefinition, 64, 16);
    }

    public static LayerDefinition createLayerFlipped(CubeDeformation deformation) {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(4, 0).addBox(-12f, 0f, 0f, 12, 14, 1, deformation), PartPose.offset(6f, 10f, -6f));
        partDefinition.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(0, 0).addBox(-10f, 7f, -1f, 1, 2, 1, deformation), PartPose.offset(6f, 8f, -6f));
        return LayerDefinition.create(meshDefinition, 64, 16);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

}
