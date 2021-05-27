package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.api.event.FoodRegistryInitEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;

import java.util.function.Supplier;

public class IMCHandler {

    private final NonNullList<ItemStack> imcNonFoodRecipes = NonNullList.create();

    private <T> T getMessageData(InterModComms.IMCMessage message) {
        Supplier<T> supplier = message.getMessageSupplier();
        return supplier.get();
    }

    @SubscribeEvent
    public void handleIMCMessage(InterModProcessEvent event) {
        event.getIMCStream().forEach(message -> {
            ItemStack itemStack;
            ItemStack inputItem;
            ItemStack outputItem;
            CompoundNBT tagCompound;
            switch (message.getMethod()) {
                case "RegisterTool":
                    itemStack = getMessageData(message);
                    CookingForBlockheadsAPI.addToolItem(itemStack);
                    break;
                case "RegisterWaterItem":
                    itemStack = getMessageData(message);
                    CookingForBlockheadsAPI.addWaterItem(itemStack);
                    break;
                case "RegisterMilkItem":
                    itemStack = getMessageData(message);
                    CookingForBlockheadsAPI.addMilkItem(itemStack);
                    break;
                case "RegisterToast":
                    tagCompound = getMessageData(message);
                    inputItem = ItemStack.read(tagCompound.getCompound("Input"));
                    final ItemStack toastOutputItem = ItemStack.read(tagCompound.getCompound("Output"));
                    if (!inputItem.isEmpty() && !toastOutputItem.isEmpty()) {
                        CookingForBlockheadsAPI.addToasterHandler(inputItem, it -> toastOutputItem);
                    } else {
                        CookingForBlockheads.logger.error("IMC API Error: RegisterToast expected message of type NBT with structure {Input : ItemStack, Output : ItemStack}");
                    }
                    break;
                case "RegisterOvenFuel":
                    tagCompound = getMessageData(message);
                    inputItem = ItemStack.read(tagCompound.getCompound("Input"));
                    if (!inputItem.isEmpty() && tagCompound.contains("FuelValue", Constants.NBT.TAG_ANY_NUMERIC)) {
                        CookingForBlockheadsAPI.addOvenFuel(inputItem, tagCompound.getInt("FuelValue"));
                    } else {
                        CookingForBlockheads.logger.error("IMC API Error: RegisterOvenFuel expected message of type NBT with structure {Input : ItemStack, FuelValue : numeric}");
                    }
                    break;
                case "RegisterOvenRecipe":
                    tagCompound = getMessageData(message);
                    inputItem = ItemStack.read(tagCompound.getCompound("Input"));
                    outputItem = ItemStack.read(tagCompound.getCompound("Output"));
                    if (!inputItem.isEmpty() && !outputItem.isEmpty()) {
                        CookingForBlockheadsAPI.addOvenRecipe(inputItem, outputItem);
                    } else {
                        CookingForBlockheads.logger.error("IMC API Error: RegisterOvenRecipe expected message of type NBT with structure {Input : ItemStack, Output : ItemStack}");
                    }
                    break;
                case "RegisterNonFoodRecipe":
                    itemStack = getMessageData(message);
                    imcNonFoodRecipes.add(itemStack);
                    break;
                case "RegisterCowClass":
                    String cowClassName = getMessageData(message);
                    try {
                        Class<?> clazz = Class.forName(cowClassName);
                        CowJarHandler.registerCowClass(clazz);
                    } catch (ClassNotFoundException e) {
                        CookingForBlockheads.logger.error("Could not register cow class " + cowClassName + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
            }
        });
    }

    @SubscribeEvent
    public void onFoodRegistryInit(FoodRegistryInitEvent event) {
        for (ItemStack itemStack : imcNonFoodRecipes) {
            event.registerNonFoodRecipe(itemStack);
        }
    }

}
