package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.cookingforblockheads.block.entity.CowJarBlockEntity;
import net.blay09.mods.cookingforblockheads.block.entity.MilkJarBlockEntity;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class CowJarRenderer extends MilkJarRenderer<CowJarBlockEntity> {

    private static Cow entity;

    public CowJarRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void extractRenderState(CowJarBlockEntity blockEntity, MilkJarRenderState renderState, float delta, Vec3 vec, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        super.extractRenderState(blockEntity, renderState, delta, vec, crumblingOverlay);

        if (entity == null) {
            entity = new Cow(EntityType.COW, blockEntity.getLevel());
        }
    }

    @Override
    public void submit(MilkJarRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState);

        float shrinkage = 0.2f;
        poseStack.pushPose();
        RenderUtils.applyBlockAngle(poseStack, renderState.blockState, 0f);
        poseStack.translate(0, 0, 0);
        poseStack.scale(shrinkage, shrinkage, shrinkage);

        // TODO Minecraft.getInstance().getEntityRenderDispatcher().render(entity, 0, 0, 0, 0f, poseStack, buffer, combinedLight);
        poseStack.popPose();
    }

    @Override
    protected BlockStateModel getLiquidModel() {
        return ModModels.cowJarLiquid.get();
    }
}
