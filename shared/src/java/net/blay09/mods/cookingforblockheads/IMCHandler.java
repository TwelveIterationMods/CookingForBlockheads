//package net.blay09.mods.cookingforblockheads; TODO Forge
//
//import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
//import net.blay09.mods.cookingforblockheads.api.event.FoodRegistryInitEvent;
//import net.minecraft.core.NonNullList;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.Tag;
//import net.minecraft.world.item.ItemStack;
//
//import java.util.function.Supplier;
//
//public class IMCHandler {
//
//    private final NonNullList<ItemStack> imcNonFoodRecipes = NonNullList.create();
//
//    private <T> T getMessageData(InterModComms.IMCMessage message) {
//        Supplier<T> supplier = message.getMessageSupplier();
//        return supplier.get();
//    }
//
//    @SubscribeEvent
//    public void handleIMCMessage(InterModProcessEvent event) {
//        event.getIMCStream().forEach(message -> {
//            ItemStack itemStack;
//            ItemStack inputItem;
//            ItemStack outputItem;
//            CompoundTag tagCompound;
//            switch (message.getMethod()) {
//                case "RegisterTool":
//                    itemStack = getMessageData(message);
//                    CookingForBlockheadsAPI.addToolItem(itemStack);
//                    break;
//                case "RegisterWaterItem":
//                    itemStack = getMessageData(message);
//                    CookingForBlockheadsAPI.addWaterItem(itemStack);
//                    break;
//                case "RegisterMilkItem":
//                    itemStack = getMessageData(message);
//                    CookingForBlockheadsAPI.addMilkItem(itemStack);
//                    break;
//                case "RegisterToast":
//                    tagCompound = getMessageData(message);
//                    inputItem = ItemStack.of(tagCompound.getCompound("Input"));
//                    final ItemStack toastOutputItem = ItemStack.of(tagCompound.getCompound("Output"));
//                    if (!inputItem.isEmpty() && !toastOutputItem.isEmpty()) {
//                        CookingForBlockheadsAPI.addToasterHandler(inputItem, it -> toastOutputItem);
//                    } else {
//                        CookingForBlockheads.logger.error("IMC API Error: RegisterToast expected message of type NBT with structure {Input : ItemStack, Output : ItemStack}");
//                    }
//                    break;
//                case "RegisterOvenFuel":
//                    tagCompound = getMessageData(message);
//                    inputItem = ItemStack.of(tagCompound.getCompound("Input"));
//                    if (!inputItem.isEmpty() && tagCompound.contains("FuelValue", Tag.TAG_ANY_NUMERIC)) {
//                        CookingForBlockheadsAPI.addOvenFuel(inputItem, tagCompound.getInt("FuelValue"));
//                    } else {
//                        CookingForBlockheads.logger.error("IMC API Error: RegisterOvenFuel expected message of type NBT with structure {Input : ItemStack, FuelValue : numeric}");
//                    }
//                    break;
//                case "RegisterOvenRecipe":
//                    tagCompound = getMessageData(message);
//                    inputItem = ItemStack.of(tagCompound.getCompound("Input"));
//                    outputItem = ItemStack.of(tagCompound.getCompound("Output"));
//                    if (!inputItem.isEmpty() && !outputItem.isEmpty()) {
//                        CookingForBlockheadsAPI.addOvenRecipe(inputItem, outputItem);
//                    } else {
//                        CookingForBlockheads.logger.error("IMC API Error: RegisterOvenRecipe expected message of type NBT with structure {Input : ItemStack, Output : ItemStack}");
//                    }
//                    break;
//                case "RegisterNonFoodRecipe":
//                    itemStack = getMessageData(message);
//                    imcNonFoodRecipes.add(itemStack);
//                    break;
//                case "RegisterCowClass":
//                    String cowClassName = getMessageData(message);
//                    try {
//                        Class<?> clazz = Class.forName(cowClassName);
//                        CowJarHandler.registerCowClass(clazz);
//                    } catch (ClassNotFoundException e) {
//                        CookingForBlockheads.logger.error("Could not register cow class " + cowClassName + ": " + e.getMessage());
//                        e.printStackTrace();
//                    }
//                    break;
//            }
//        });
//    }
//
//    @SubscribeEvent
//    public void onFoodRegistryInit(FoodRegistryInitEvent event) {
//        for (ItemStack itemStack : imcNonFoodRecipes) {
//            event.registerNonFoodRecipe(itemStack);
//        }
//    }
//
//}
