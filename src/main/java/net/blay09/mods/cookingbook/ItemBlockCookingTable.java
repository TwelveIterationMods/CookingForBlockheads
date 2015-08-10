package net.blay09.mods.cookingbook;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemBlockCookingTable extends ItemBlock {

    public ItemBlockCookingTable(Block block) {
        super(block);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
        super.addInformation(itemStack, player, list, flag);

        for (String s : I18n.format("cookingbook:recipebook_tier2.tooltipDesc").split("\\\\n")) {
            list.add("\u00a77" + s);
        }
    }

}
