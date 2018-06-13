package net.blay09.mods.cookingforblockheads.item;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.network.handler.GuiHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class ItemRecipeBook extends Item implements IRegisterableItem {

    public enum BookType {
        RECIPE_BOOK,
        CRAFTING_BOOK,
        NO_FILTER_BOOK
    }

    public final BookType type;

    public ItemRecipeBook(BookType type) {
        this.type = type;

        setCreativeTab(CookingForBlockheads.creativeTab);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        player.openGui(CookingForBlockheads.instance, GuiHandler.ITEM_RECIPE_BOOK, world, hand.ordinal(), 0, 0);
        return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(itemStack, world, tooltip, flag);

        tooltip.add(TextFormatting.YELLOW + I18n.format("tooltip." + createRegistryName()));

        addDefaultTooltipDescription(tooltip);
    }

    @Override
    public String getIdentifier() {
        return type.name().toLowerCase(Locale.ENGLISH);
    }



}
