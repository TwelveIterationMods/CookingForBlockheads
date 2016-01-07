package net.blay09.mods.cookingbook.addon;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.blay09.mods.cookingbook.CookingForBlockheads;
import net.blay09.mods.cookingbook.api.CookingAPI;
import net.blay09.mods.cookingbook.api.FoodStatsProvider;
import net.blay09.mods.cookingbook.api.ToastHandler;
import net.blay09.mods.cookingbook.api.event.FoodRegistryInitEvent;
import net.blay09.mods.cookingbook.api.SinkHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

public class VanillaAddon implements FoodStatsProvider {

    public VanillaAddon() {
        SinkHandler simpleHandler = new SinkHandler() {
            @Override
            public ItemStack getSinkOutput(ItemStack itemStack) {
                ItemStack result = itemStack.copy();
                result.setItemDamage(0);
                return result;
            }
        };
        CookingAPI.addSinkHandler(new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE), simpleHandler);
        CookingAPI.addSinkHandler(new ItemStack(Blocks.carpet, 1, OreDictionary.WILDCARD_VALUE), simpleHandler);
        SinkHandler armorHandler = new SinkHandler() {
            @Override
            public ItemStack getSinkOutput(ItemStack itemStack) {
                if(itemStack.getItem() instanceof ItemArmor) {
                    ((ItemArmor) itemStack.getItem()).removeColor(itemStack);
                }
                return itemStack;
            }
        };
        CookingAPI.addSinkHandler(new ItemStack(Items.leather_boots, 1, OreDictionary.WILDCARD_VALUE), armorHandler);
        CookingAPI.addSinkHandler(new ItemStack(Items.leather_chestplate, 1, OreDictionary.WILDCARD_VALUE), armorHandler);
        CookingAPI.addSinkHandler(new ItemStack(Items.leather_helmet, 1, OreDictionary.WILDCARD_VALUE), armorHandler);
        CookingAPI.addSinkHandler(new ItemStack(Items.leather_leggings, 1, OreDictionary.WILDCARD_VALUE), armorHandler);

        CookingAPI.addSinkHandler(new ItemStack(Items.milk_bucket), new SinkHandler() {
            @Override
            public ItemStack getSinkOutput(ItemStack itemStack) {
                return new ItemStack(Items.bucket, 1);
            }
        });

        CookingAPI.addSinkHandler(new ItemStack(Items.potionitem, 1, OreDictionary.WILDCARD_VALUE), new SinkHandler() {
            @Override
            public ItemStack getSinkOutput(ItemStack itemStack) {
                return new ItemStack(Items.glass_bottle, 1);
            }
        });

        CookingAPI.addToastHandler(new ItemStack(Items.bread), new ToastHandler() {
            @Override
            public ItemStack getToasterOutput(ItemStack itemStack) {
                return new ItemStack(CookingForBlockheads.itemToast);
            }
        });

        MinecraftForge.EVENT_BUS.register(this);

        CookingAPI.setFoodStatsProvider(this);
    }

    @SubscribeEvent
    public void onFoodRegistryInit(FoodRegistryInitEvent event) {
        event.registerNonFoodRecipe(new ItemStack(Items.cake));
        event.registerNonFoodRecipe(new ItemStack(Items.sugar));
    }

    @Override
    public float getSaturation(ItemStack itemStack, EntityPlayer entityPlayer) {
        ItemFood item = (ItemFood) itemStack.getItem();
        return item.func_150906_h(itemStack);
    }

    @Override
    public int getFoodLevel(ItemStack itemStack, EntityPlayer entityPlayer) {
        ItemFood item = (ItemFood) itemStack.getItem();
        return item.func_150905_g(itemStack);
    }
}
