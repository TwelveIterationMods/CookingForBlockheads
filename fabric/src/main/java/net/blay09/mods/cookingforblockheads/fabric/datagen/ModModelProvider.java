package net.blay09.mods.cookingforblockheads.fabric.datagen;

import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        blockStateModelGenerator.skipAutoItemBlock(ModBlocks.cabinet);
        blockStateModelGenerator.skipAutoItemBlock(ModBlocks.counter);
        blockStateModelGenerator.skipAutoItemBlock(ModBlocks.cowJar);
        blockStateModelGenerator.skipAutoItemBlock(ModBlocks.fridge);
        blockStateModelGenerator.skipAutoItemBlock(ModBlocks.oven);

        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.oven);
        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.toaster);
        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.milkJar);
        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.cowJar);
        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.fruitBasket);
        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.cuttingBoard);
        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.cookingTable);
        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.fridge);
        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.sink);
        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.counter);
        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.cabinet);
        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.corner);
        blockStateModelGenerator.createNonTemplateModelBlock(ModBlocks.hangingCorner);
        for (final var kitchenFloor : ModBlocks.kitchenFloors) {
            blockStateModelGenerator.createNonTemplateModelBlock(kitchenFloor);
        }

        blockStateModelGenerator.createNonTemplateHorizontalBlock(ModBlocks.toolRack);
        blockStateModelGenerator.createNonTemplateHorizontalBlock(ModBlocks.spiceRack);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(ModItems.recipeBook, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.craftingBook, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.noFilterBook, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.heatingUnit, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.iceUnit, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.preservationChamber, ModelTemplates.FLAT_ITEM);
    }

}
