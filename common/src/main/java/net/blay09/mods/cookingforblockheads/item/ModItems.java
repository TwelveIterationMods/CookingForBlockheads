package net.blay09.mods.cookingforblockheads.item;

import net.blay09.mods.balm.world.item.BalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.world.item.BalmItemRegistrar;
import net.blay09.mods.balm.world.item.DeferredItem;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.sounds.SoundEvents;

import java.util.Map;

public class ModItems {

    public static DeferredItem recipeBook;
    public static DeferredItem noFilterBook;
    public static DeferredItem craftingBook;
    public static DeferredItem heatingUnit;
    public static DeferredItem iceUnit;
    public static DeferredItem saltFilter;
    public static DeferredItem iceCubes;
    public static DeferredItem preservationChamber;
    public static DeferredItem chefHat;

    public static void initialize(BalmItemRegistrar items) {
        recipeBook = items.register("recipe_book", ItemRecipeBook::recipeBook).asDeferredItem();
        noFilterBook = items.register("no_filter_edition", ItemRecipeBook::noFilterBook).asDeferredItem();
        craftingBook = items.register("crafting_book", ItemRecipeBook::craftingBook).asDeferredItem();
        heatingUnit = items.register("heating_unit", ItemHeatingUnit::new).asDeferredItem();
        iceUnit = items.register("ice_unit", ItemIceUnit::new).asDeferredItem();
        saltFilter = items.register("salt_filter", SaltFilterItem::new).asDeferredItem();
        iceCubes = items.register("ice_cubes", properties -> new Item(properties.stacksTo(16).food(new FoodProperties.Builder()
                .nutrition(0)
                .saturationModifier(0f)
                .alwaysEdible()
                .build()))).asDeferredItem();
        preservationChamber = items.register("preservation_chamber", ItemPreservationChamber::new).asDeferredItem();
        chefHat = items.register("chef_hat", properties -> new Item(properties.stacksTo(1).component(
                DataComponents.EQUIPPABLE,
                Equippable.builder(EquipmentSlot.HEAD)
                        .setEquipSound(SoundEvents.ARMOR_EQUIP_LEATHER)
                        .setAsset(ResourceKey.create(EquipmentAssets.ROOT_ID, CookingForBlockheads.id("chef_hat")))
                        .build()))).asDeferredItem();
    }

    public static void initialize(BalmCreativeModeTabRegistrar creativeModeTabs) {
        creativeModeTabs.register(CookingForBlockheads.MOD_ID, builder ->
                builder.title(Component.translatable("itemGroup.cookingforblockheads.cookingforblockheads"))
                        .icon(() -> new ItemStack(ModBlocks.cowJar))
                        .displayItems((parameters, output) -> {
                            output.accept(recipeBook);
                            output.accept(craftingBook);
                            output.accept(ModBlocks.cookingTables.get(null));
                            output.accept(ModBlocks.fridges.get(DyeColor.WHITE));
                            output.accept(ModBlocks.ovens.get(DyeColor.WHITE));
                            output.accept(ModBlocks.sinks.get(null));
                            output.accept(ModBlocks.chickenSinks.get(null));
                            output.accept(ModBlocks.counters.get(null));
                            output.accept(ModBlocks.cabinets.get(null));
                            output.accept(ModBlocks.connectors.get(null));
                            output.accept(ModBlocks.kitchenFloors.get(DyeColor.WHITE));
                            output.accept(ModBlocks.milkJar);
                            output.accept(ModBlocks.cowJar);
                            output.accept(ModBlocks.toaster);
                            output.accept(ModBlocks.cookieJar);
                            output.accept(ModBlocks.toolRack);
                            output.accept(ModBlocks.spiceRack);
                            output.accept(ModBlocks.fruitBasket);
                            output.accept(ModBlocks.cuttingBoard);
                            output.accept(iceUnit);
                            output.accept(saltFilter);
                            output.accept(iceCubes);
                            output.accept(preservationChamber);
                            output.accept(chefHat);
                            output.accept(heatingUnit);
                            output.accept(noFilterBook);

                            ModBlocks.cookingTables.sortedEntries().filter(it -> it.getKey() != null).map(Map.Entry::getValue).forEach(output::accept);

                            ModBlocks.fridges.forEach((color, block) -> {
                                if (color != DyeColor.WHITE) {
                                    output.accept(block);
                                }
                            });

                            ModBlocks.ovens.forEach((color, block) -> {
                                if (color != DyeColor.WHITE) {
                                    output.accept(block);
                                }
                            });

                            ModBlocks.sinks.sortedEntries().filter(it -> it.getKey() != null).map(Map.Entry::getValue).forEach(output::accept);
                            ModBlocks.chickenSinks.sortedEntries().filter(it -> it.getKey() != null).map(Map.Entry::getValue).forEach(output::accept);
                            ModBlocks.counters.sortedEntries().filter(it -> it.getKey() != null).map(Map.Entry::getValue).forEach(output::accept);
                            ModBlocks.cabinets.sortedEntries().filter(it -> it.getKey() != null).map(Map.Entry::getValue).forEach(output::accept);

                            ModBlocks.kitchenFloors.forEach((color, block) -> {
                                if (color != DyeColor.WHITE) {
                                    output.accept(block);
                                }
                            });

                            ModBlocks.connectors.sortedEntries().filter(it -> it.getKey() != null).map(Map.Entry::getValue).forEach(output::accept);
                        }));
    }

}
