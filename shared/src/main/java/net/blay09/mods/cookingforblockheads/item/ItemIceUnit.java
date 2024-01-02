package net.blay09.mods.cookingforblockheads.item;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.cookingforblockheads.network.message.SyncedEffectMessage;
import net.blay09.mods.cookingforblockheads.block.entity.FridgeBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemIceUnit extends Item {

    public ItemIceUnit() {
        super(new Item.Properties());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof FridgeBlockEntity && !((FridgeBlockEntity) blockEntity).getBaseFridge().hasIceUpgrade) {
            if (!player.getAbilities().instabuild) {
                player.getItemInHand(context.getHand()).shrink(1);
            }

            ((FridgeBlockEntity) blockEntity).getBaseFridge().setHasIceUpgrade(true);
            if (!level.isClientSide) {
                Balm.getNetworking().sendToTracking(((ServerLevel) level), pos, new SyncedEffectMessage(pos, SyncedEffectMessage.Type.FRIDGE_UPGRADE));
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(itemStack, level, tooltip, flag);

        tooltip.add(Component.translatable("tooltip.cookingforblockheads.fridge_upgrade").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.translatable("tooltip.cookingforblockheads.ice_unit.description").withStyle(ChatFormatting.GRAY));
    }

}
