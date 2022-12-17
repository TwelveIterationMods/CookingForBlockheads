package net.blay09.mods.cookingforblockheads.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.blay09.mods.cookingforblockheads.client.ModModels;
import net.blay09.mods.cookingforblockheads.tile.CounterBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CounterRenderer<T extends CounterBlockEntity> implements BlockEntityRenderer<T> {

    private static final RandomSource random = RandomSource.create();

    private static final float doorOriginX = 0.84375f;

    private static final float doorOriginZ = 0.09375f;

    public CounterRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected float getDoorOriginX() {
        return doorOriginX;
    }

    protected float getDoorOriginZ() {
        return doorOriginZ;
    }

    protected float getBottomShelfOffsetY() {
        return -0.85f;
    }

    protected float getTopShelfOffsetY() {
        return 0.35f;
    }

    protected BakedModel getDoorModel(@Nullable DyeColor blockColor, boolean isFlipped) {
        int colorIndex = blockColor != null ? blockColor.getId() + 1 : 0;
        return isFlipped ? ModModels.counterDoorsFlipped.get(colorIndex).get() : ModModels.counterDoors.get(colorIndex).get();
    }

    @Override
    public void render(T blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Level level = blockEntity.getLevel();
        if (level == null) {
            return;
        }

        BlockState state = blockEntity.getBlockState();
        boolean hasColor = state.getValue(BlockKitchen.HAS_COLOR);
        DyeColor blockColor = hasColor ? state.getValue(BlockKitchen.COLOR) : null;
        float blockAngle = blockEntity.getFacing().toYRot();
        float doorAngle = blockEntity.getDoorAnimator().getRenderAngle(partialTicks);
        boolean isFlipped = blockEntity.isFlipped();

        poseStack.pushPose();
        float doorOriginX = getDoorOriginX();
        float doorOriginZ = getDoorOriginZ();
        float doorDirection = -1f;
        if (isFlipped) {
            doorOriginX = 1 - doorOriginX;
            doorDirection = 1f;
        }

        RenderUtils.applyBlockAngle(poseStack, blockEntity.getBlockState());
        poseStack.translate(-0.5f, 0f, -0.5f);

        poseStack.translate(doorOriginX, 0f, doorOriginZ);
        poseStack.mulPose(Axis.YP.rotationDegrees(doorDirection * (float) Math.toDegrees(doorAngle)));
        poseStack.translate(-doorOriginX, 0f, -doorOriginZ);

        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        BakedModel model = getDoorModel(blockColor, isFlipped);
        dispatcher.getModelRenderer().tesselateBlock(level, model, blockEntity.getBlockState(), blockEntity.getBlockPos(), poseStack, buffer.getBuffer(RenderType.solid()), false, random, 0, 0);
        poseStack.popPose();

        // Render the content if the door is open
        if (doorAngle > 0f) {
            poseStack.pushPose();
            poseStack.translate(0, 0.5, 0);
            RenderUtils.applyBlockAngle(poseStack, blockEntity.getBlockState());
            poseStack.scale(0.3f, 0.3f, 0.3f);
            Container itemHandler = blockEntity.getContainer();
            int itemsPerShelf = itemHandler.getContainerSize() / 2;
            int itemsPerRow = itemsPerShelf / 2;
            for (int i = itemHandler.getContainerSize() - 1; i >= 0; i--) {
                ItemStack itemStack = itemHandler.getItem(i);
                if (!itemStack.isEmpty()) {
                    float offsetX, offsetY, offsetZ;
                    int shelfIndex = i % itemsPerShelf;
                    int rowIndex = i % itemsPerRow;
                    float spacing = 2f / (float) itemsPerRow;
                    offsetX = (rowIndex - itemsPerRow / 2f) * -spacing + (shelfIndex >= itemsPerRow ? -0.2f : 0f);
                    offsetY = i < itemsPerShelf ? getTopShelfOffsetY() : getBottomShelfOffsetY();
                    offsetZ = shelfIndex < itemsPerRow ? 0.5f : -0.5f;
                    poseStack.pushPose();
                    poseStack.translate(offsetX, offsetY, offsetZ);
                    poseStack.mulPose(Axis.YP.rotationDegrees(45f));
                    RenderUtils.renderItem(itemStack, combinedLight, poseStack, buffer);
                    poseStack.popPose();
                }
            }

            poseStack.popPose();
        }
    }

}
