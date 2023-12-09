package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.api.event.FoodRegistryInitEvent;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class IMCHandler {

    public static final Logger logger = LogManager.getLogger();

    private static final NonNullList<ItemStack> imcNonFoodRecipes = NonNullList.create();

    @SuppressWarnings("unchecked")
    private static <T> T getMessageData(InterModComms.IMCMessage message) {
        Supplier<T> supplier = (Supplier<T>) message.messageSupplier();
        return supplier.get();
    }

    @SuppressWarnings("unchecked")
    public static void processIMC(InterModProcessEvent event) {
        event.getIMCStream().forEach(message -> {
            ItemStack itemStack;
            ItemStack inputItem;
            ItemStack outputItem;
            CompoundTag tagCompound;
            switch (message.method()) {
                case "RegisterTool" -> {
                    itemStack = getMessageData(message);
                    CookingForBlockheadsAPI.addToolItem(itemStack);
                }
                case "RegisterWaterItem" -> {
                    itemStack = getMessageData(message);
                    CookingForBlockheadsAPI.addWaterItem(itemStack);
                }
                case "RegisterMilkItem" -> {
                    itemStack = getMessageData(message);
                    CookingForBlockheadsAPI.addMilkItem(itemStack);
                }
                case "RegisterToast" -> {
                    tagCompound = getMessageData(message);
                    inputItem = ItemStack.of(tagCompound.getCompound("Input"));
                    final ItemStack toastOutputItem = ItemStack.of(tagCompound.getCompound("Output"));
                    if (!inputItem.isEmpty() && !toastOutputItem.isEmpty()) {
                        CookingForBlockheadsAPI.addToasterHandler(inputItem, it -> toastOutputItem);
                    } else {
                        CookingForBlockheads.logger.error("IMC API Error: RegisterToast expected message of type NBT with structure {Input : ItemStack, Output : ItemStack}");
                    }
                }
                case "RegisterOvenFuel" -> {
                    tagCompound = getMessageData(message);
                    inputItem = ItemStack.of(tagCompound.getCompound("Input"));
                    if (!inputItem.isEmpty() && tagCompound.contains("FuelValue", Tag.TAG_ANY_NUMERIC)) {
                        CookingForBlockheadsAPI.addOvenFuel(inputItem, tagCompound.getInt("FuelValue"));
                    } else {
                        CookingForBlockheads.logger.error("IMC API Error: RegisterOvenFuel expected message of type NBT with structure {Input : ItemStack, FuelValue : numeric}");
                    }
                }
                case "RegisterOvenRecipe" -> {
                    tagCompound = getMessageData(message);
                    inputItem = ItemStack.of(tagCompound.getCompound("Input"));
                    outputItem = ItemStack.of(tagCompound.getCompound("Output"));
                    if (!inputItem.isEmpty() && !outputItem.isEmpty()) {
                        CookingForBlockheadsAPI.addOvenRecipe(inputItem, outputItem);
                    } else {
                        CookingForBlockheads.logger.error("IMC API Error: RegisterOvenRecipe expected message of type NBT with structure {Input : ItemStack, Output : ItemStack}");
                    }
                }
                case "RegisterNonFoodRecipe" -> {
                    itemStack = getMessageData(message);
                    imcNonFoodRecipes.add(itemStack);
                }
                case "RegisterCowClass" -> {
                    String cowClassName = getMessageData(message);
                    try {
                        Class<?> clazz = Class.forName(cowClassName);
                        CookingForBlockheadsAPI.addCowClass(((Class<? extends LivingEntity>) clazz));
                    } catch (ClassNotFoundException e) {
                        CookingForBlockheads.logger.error("Could not register cow class " + cowClassName + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void onFoodRegistryInit(FoodRegistryInitEvent event) {
        for (ItemStack itemStack : imcNonFoodRecipes) {
            event.registerNonFoodRecipe(itemStack);
        }
    }
}
