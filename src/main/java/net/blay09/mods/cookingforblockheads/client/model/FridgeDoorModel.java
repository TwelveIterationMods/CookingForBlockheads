package net.blay09.mods.cookingforblockheads.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class FridgeDoorModel extends Model {

    private final ModelRenderer main;
    private final ModelRenderer handle;

    public FridgeDoorModel(boolean flipped) {
        super(RenderType::getEntitySolid);

        textureWidth = 64;
        textureHeight = 16;

        if (flipped) {
            main = new ModelRenderer(this, 4, 0);
            main.addBox(-14f, 0f, 0f, 14, 15, 1);
            main.setRotationPoint(7f, 9f, -7f);
            main.setTextureSize(64, 16);

            handle = new ModelRenderer(this, 0, 0);
            handle.addBox(-13f, 6f, -1f, 1, 2, 1);
            handle.setRotationPoint(7f, 9f, -7f);
            handle.setTextureSize(64, 16);
        } else {
            main = new ModelRenderer(this, 4, 0);
            main.addBox(0f, 0f, 0f, 14, 15, 1);
            main.setRotationPoint(-7f, 9f, -7f);
            main.setTextureSize(64, 16);

            handle = new ModelRenderer(this, 0, 0);
            handle.addBox(12f, 6f, -1f, 1, 2, 1);
            handle.setRotationPoint(-7f, 9f, -7f);
            handle.setTextureSize(64, 16);
        }
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        main.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        handle.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

}
