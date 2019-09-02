package net.blay09.mods.cookingforblockheads.item;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.network.NetworkHandler;
import net.blay09.mods.cookingforblockheads.network.message.MessageSyncedEffect;
import net.blay09.mods.cookingforblockheads.tile.TileFridge;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPreservationChamber extends Item {

    public static final String name = "preservation_chamber";
    public static final ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, name);

    public ItemPreservationChamber() {
        super(new Item.Properties().group(CookingForBlockheads.itemGroup).maxStackSize(1));
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileFridge && !((TileFridge) tileEntity).getBaseFridge().hasPreservationUpgrade()) {
            if (!player.abilities.isCreativeMode) {
                player.getHeldItem(hand).shrink(1);
            }

            ((TileFridge) tileEntity).getBaseFridge().setHasPreservationUpgrade(true);
            if (!world.isRemote) {
                NetworkHandler.instance.sendToAllAround(new MessageSyncedEffect(pos, MessageSyncedEffect.Type.FRIDGE_UPGRADE), new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 32));
            }

            return ActionResultType.SUCCESS;
        }

        return ActionResultType.PASS;
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.addInformation(itemStack, world, tooltip, flag);

        tooltip.add(TextFormatting.YELLOW + I18n.format("tooltip.cookingforblockheads:fridge_upgrade"));
        for (String s : I18n.format("tooltip.cookingforblockheads:preservation_chamber.description").split("\\\\n")) {
            tooltip.add(TextFormatting.GRAY + s);
        }
    }

}
