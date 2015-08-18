package net.blay09.mods.cookingbook;

import net.blay09.mods.cookingbook.api.SinkHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;

public class SinkHandlers {

    private static final Map<String, SinkHandler> handlers = new HashMap<String, SinkHandler>();

    public static void addSinkHandler(ItemStack itemStackIn, SinkHandler sinkHandler) {
        handlers.put(itemStackIn.getItem().getUnlocalizedName() + "@" + itemStackIn.getItemDamage(), sinkHandler);
    }

    public static ItemStack getSinkOutput(ItemStack itemStack) {
        if(itemStack == null) {
            return null;
        }
        SinkHandler sinkHandler = handlers.get(itemStack.getItem().getUnlocalizedName() + "@" + itemStack.getItemDamage());
        if(sinkHandler == null) {
            sinkHandler = handlers.get(itemStack.getItem().getUnlocalizedName() + "@" + OreDictionary.WILDCARD_VALUE);
        }
        return sinkHandler != null ? sinkHandler.getSinkOutput(itemStack) : null;
    }

}
