package net.blay09.mods.cookingbook.addon;

import net.blay09.mods.cookingbook.api.CookingAPI;
import net.blay09.mods.cookingbook.api.SinkHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import org.omg.IOP.TaggedComponent;

public class VanillaAddon {

    public VanillaAddon() {
        SinkHandler simpleHandler = new SinkHandler() {
            @Override
            public ItemStack getSinkOutput(ItemStack itemStack) {
                ItemStack result = itemStack.copy();
                result.setItemDamage(0);
                return result;
            }
        };
        CookingAPI.addSinkHandler(new ItemStack(Items.potionitem, 1, OreDictionary.WILDCARD_VALUE), simpleHandler);
        CookingAPI.addSinkHandler(new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE), simpleHandler);
        CookingAPI.addSinkHandler(new ItemStack(Blocks.carpet, 1, OreDictionary.WILDCARD_VALUE), simpleHandler);
        SinkHandler armorHandler = new SinkHandler() {
            @Override
            public ItemStack getSinkOutput(ItemStack itemStack) {
                ItemStack result = itemStack.copy();
                NBTTagCompound tagCompound = result.getTagCompound();
                if(tagCompound != null) {
                    tagCompound.getCompoundTag("display").removeTag("color");
                }
                return result;
            }
        };
        CookingAPI.addSinkHandler(new ItemStack(Items.leather_boots, 1, OreDictionary.WILDCARD_VALUE), armorHandler);
        CookingAPI.addSinkHandler(new ItemStack(Items.leather_chestplate, 1, OreDictionary.WILDCARD_VALUE), armorHandler);
        CookingAPI.addSinkHandler(new ItemStack(Items.leather_helmet, 1, OreDictionary.WILDCARD_VALUE), armorHandler);
        CookingAPI.addSinkHandler(new ItemStack(Items.leather_leggings, 1, OreDictionary.WILDCARD_VALUE), armorHandler);
    }

}
