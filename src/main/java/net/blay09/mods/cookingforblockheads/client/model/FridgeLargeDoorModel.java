package net.blay09.mods.cookingforblockheads.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class FridgeLargeDoorModel extends Model {

    private final ModelRenderer main;
    private final ModelRenderer handle;

    public FridgeLargeDoorModel(boolean flipped) {
        super(RenderType::getEntitySolid);

        textureWidth = 64;
        textureHeight = 32;
        if (flipped) {
            main = new ModelRenderer(this, 4, 0);
            main.addBox(-14f, 0f, 0f, 14, 29, 1);
            main.setRotationPoint(7f, -5f, -7f);
            main.setTextureSize(64, 32);

            handle = new ModelRenderer(this, 0, 0);
            handle.addBox(-13f, 3f, -1f, 1, 23, 1);
            handle.setRotationPoint(7f, -5f, -7f);
            handle.setTextureSize(64, 32);
        } else {
            main = new ModelRenderer(this, 4, 0);
            main.addBox(0f, 0f, 0f, 14, 29, 1);
            main.setRotationPoint(-7f, -5f, -7f);
            main.setTextureSize(64, 32);

            handle = new ModelRenderer(this, 0, 0);
            handle.addBox(12f, 3f, -1f, 1, 23, 1);
            handle.setRotationPoint(-7f, -5f, -7f);
            handle.setTextureSize(64, 32);
        }
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        main.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        handle.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
