package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blay09.mods.cookingforblockheads.block.BaseKitchenBlock;
import net.minecraft.world.level.block.state.BlockState;

public class RenderUtils {

    public static void applyBlockAngle(PoseStack poseStack, BlockState state) {
        applyBlockAngle(poseStack, state, 180f);
    }

    public static void applyBlockAngle(PoseStack poseStack, BlockState state, float angleOffset) {
        float angle = state.getValue(BaseKitchenBlock.FACING).toYRot();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(angleOffset - angle));
    }

}
