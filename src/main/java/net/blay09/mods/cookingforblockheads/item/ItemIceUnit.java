package net.blay09.mods.cookingforblockheads.item;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.network.NetworkHandler;
import net.blay09.mods.cookingforblockheads.network.message.MessageSyncedEffect;
import net.blay09.mods.cookingforblockheads.tile.TileFridge;
import net.blay09.mods.cookingforblockheads.util.TextUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemIceUnit extends Item {

    public static final String name = "ice_unit";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    public ItemIceUnit() {
        super(new Item.Properties().group(CookingForBlockheads.itemGroup).maxStackSize(1));
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        PlayerEntity player = context.getPlayer();
        if (player == null) {
            return ActionResultType.PASS;
        }

        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileFridge && !((TileFridge) tileEntity).getBaseFridge().hasIceUpgrade) {
            if (!player.abilities.isCreativeMode) {
                player.getHeldItem(context.getHand()).shrink(1);
            }

            ((TileFridge) tileEntity).getBaseFridge().setHasIceUpgrade(true);
            if (!world.isRemote) {
                NetworkHandler.sendToAllTracking(new MessageSyncedEffect(pos, MessageSyncedEffect.Type.FRIDGE_UPGRADE), world, pos);
            }

            return ActionResultType.SUCCESS;
        }

        return ActionResultType.PASS;
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.addInformation(itemStack, world, tooltip, flag);

        tooltip.add(TextUtils.coloredTextComponent("tooltip.cookingforblockheads:fridge_upgrade", TextFormatting.YELLOW));
        for (String s : I18n.format("tooltip.cookingforblockheads:ice_unit.description").split("\\\\n")) {
            tooltip.add(TextUtils.coloredTextComponent(s, TextFormatting.GRAY));
        }
    }

}
