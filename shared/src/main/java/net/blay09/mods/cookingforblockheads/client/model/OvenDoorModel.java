package net.blay09.mods.cookingforblockheads.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class OvenDoorModel extends Model {

    private final ModelRenderer main;
    private final ModelRenderer bopLeft;
    private final ModelRenderer bopRight;
    private final ModelRenderer handle;

    public OvenDoorModel() {
        super(RenderType::getEntitySolid);

        textureWidth = 64;
        textureHeight = 16;

        main = new ModelRenderer(this, 0, 0);
        main.addBox(0f, -10f, 0f, 12, 11, 1);
        main.setRotationPoint(-6f, 23f, -7f);
        main.setTextureSize(64, 16);

        bopLeft = new ModelRenderer(this, 0, 0);
        bopLeft.addBox(2f, -10f, -1f, 1, 1, 1);
        bopLeft.setRotationPoint(-6f, 23f, -7f);
        bopLeft.setTextureSize(64, 16);

        bopRight = new ModelRenderer(this, 0, 0);
        bopRight.addBox(9f, -10f, -1f, 1, 1, 1);
        bopRight.setRotationPoint(-6f, 23f, -7f);
        bopRight.setTextureSize(64, 16);

        handle = new ModelRenderer(this, 0, 0);
        handle.addBox(0f, -10f, -2f, 12, 1, 1);
        handle.setRotationPoint(-6f, 23f, -7f);
        handle.setTextureSize(64, 16);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        main.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        bopLeft.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        bopRight.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        handle.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

}
