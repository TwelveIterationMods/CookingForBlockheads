package net.blay09.mods.cookingforblockheads.item;

import net.blay09.mods.balm.world.item.BalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.world.item.BalmItemRegistrar;
import net.blay09.mods.balm.world.item.DeferredItem;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

public class ModItems {

    public static DeferredItem recipeBook;
    public static DeferredItem noFilterBook;
    public static DeferredItem craftingBook;
    public static DeferredItem heatingUnit;
    public static DeferredItem iceUnit;
    public static DeferredItem preservationChamber;

    public static void initialize(BalmItemRegistrar items) {
        recipeBook = items.register("recipe_book", ItemRecipeBook::recipeBook).asDeferredItem();
        noFilterBook = items.register("no_filter_edition", ItemRecipeBook::noFilterBook).asDeferredItem();
        craftingBook = items.register("crafting_book", ItemRecipeBook::craftingBook).asDeferredItem();
        heatingUnit = items.register("heating_unit", ItemHeatingUnit::new).asDeferredItem();
        iceUnit = items.register("ice_unit", ItemIceUnit::new).asDeferredItem();
        preservationChamber = items.register("preservation_chamber", ItemPreservationChamber::new).asDeferredItem();
    }

    public static void initialize(BalmCreativeModeTabRegistrar creativeModeTabs) {
        creativeModeTabs.register(CookingForBlockheads.MOD_ID, builder ->
                builder.title(Component.translatable("itemGroup.cookingforblockheads.cookingforblockheads"))
                        .icon(() -> new ItemStack(ModBlocks.cowJar))
                        .displayItems((parameters, output) -> {
                            output.accept(recipeBook);
                            output.accept(craftingBook);
                            output.accept(ModBlocks.cookingTables.getUndiscriminated());
                            output.accept(ModBlocks.fridges.get(DyeColor.WHITE));
                            output.accept(ModBlocks.ovens.get(DyeColor.WHITE));
                            output.accept(ModBlocks.sinks.getUndiscriminated());
                            output.accept(ModBlocks.counters.getUndiscriminated());
                            output.accept(ModBlocks.cabinets.getUndiscriminated());
                            output.accept(ModBlocks.connectors.getUndiscriminated());
                            output.accept(ModBlocks.kitchenFloors.get(DyeColor.WHITE));
                            output.accept(ModBlocks.milkJar);
                            output.accept(ModBlocks.cowJar);
                            output.accept(ModBlocks.toaster);
                            output.accept(ModBlocks.toolRack);
                            output.accept(ModBlocks.spiceRack);
                            output.accept(ModBlocks.fruitBasket);
                            output.accept(ModBlocks.cuttingBoard);
                            output.accept(iceUnit);
                            output.accept(preservationChamber);
                            output.accept(heatingUnit);
                            output.accept(noFilterBook);

                            ModBlocks.cookingTables.getDiscriminated().forEach(output::accept);

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

                            ModBlocks.sinks.getDiscriminated().forEach(output::accept);
                            ModBlocks.counters.getDiscriminated().forEach(output::accept);
                            ModBlocks.cabinets.getDiscriminated().forEach(output::accept);

                            ModBlocks.kitchenFloors.forEach((color, block) -> {
                                if (color != DyeColor.WHITE) {
                                    output.accept(block);
                                }
                            });

                            ModBlocks.connectors.getDiscriminated().forEach(output::accept);
                        }));
    }

}
