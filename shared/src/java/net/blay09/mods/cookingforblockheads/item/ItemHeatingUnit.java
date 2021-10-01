package net.blay09.mods.cookingforblockheads.item;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.item.BalmItem;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.network.message.SyncedEffectMessage;
import net.blay09.mods.cookingforblockheads.tile.OvenBlockEntity;
import net.blay09.mods.cookingforblockheads.util.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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

public class ItemHeatingUnit extends BalmItem {

    public static final String name = "heating_unit";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    public ItemHeatingUnit() {
        super(new Item.Properties().tab(ModItems.creativeModeTab).stacksTo(1));
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
        if (blockEntity instanceof OvenBlockEntity && !((OvenBlockEntity) blockEntity).hasPowerUpgrade()) {
            if (!player.getAbilities().instabuild) {
                player.getItemInHand(context.getHand()).shrink(1);
            }

            ((OvenBlockEntity) blockEntity).setHasPowerUpgrade(true);
            if (!level.isClientSide) {
                Balm.getNetworking().sendToTracking(((ServerLevel) level), pos, new SyncedEffectMessage(pos, SyncedEffectMessage.Type.OVEN_UPGRADE));
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(itemStack, level, tooltip, flag);

        tooltip.add(TextUtils.coloredTextComponent("tooltip.cookingforblockheads:oven_upgrade", ChatFormatting.YELLOW));
        for (String s : I18n.get("tooltip.cookingforblockheads:heating_unit.description").split("\\\\n")) {
            tooltip.add(TextUtils.coloredTextComponent(s, ChatFormatting.GRAY));
        }
    }

}
