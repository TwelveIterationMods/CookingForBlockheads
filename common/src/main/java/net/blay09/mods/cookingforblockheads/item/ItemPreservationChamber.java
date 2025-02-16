package net.blay09.mods.cookingforblockheads.item;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.cookingforblockheads.api.UpgradeablePreservation;
import net.blay09.mods.cookingforblockheads.network.message.SyncedEffectMessage;
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

import java.util.List;

public class ItemPreservationChamber extends Item {

    public ItemPreservationChamber() {
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

        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof UpgradeablePreservation upgradeable && !upgradeable.hasPreservationUpgrade()) {
            if (!player.getAbilities().instabuild) {
                player.getItemInHand(context.getHand()).shrink(1);
            }

            upgradeable.setHasPreservationUpgrade(true);

            if (!level.isClientSide) {
                Balm.getNetworking().sendToTracking(((ServerLevel) level), pos, new SyncedEffectMessage(pos, SyncedEffectMessage.Type.KITCHEN_UPGRADE));
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(itemStack, context, tooltip, flag);

        tooltip.add(Component.translatable("tooltip.cookingforblockheads.kitchen_upgrade").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.translatable("tooltip.cookingforblockheads.preservation_chamber.description").withStyle(ChatFormatting.GRAY));
    }

}
