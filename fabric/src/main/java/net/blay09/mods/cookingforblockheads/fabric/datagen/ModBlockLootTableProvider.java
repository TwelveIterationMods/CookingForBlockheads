package net.blay09.mods.cookingforblockheads.fabric.datagen;

import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {
    protected ModBlockLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        dropSelf(ModBlocks.cookingTable);
        dropSelf(ModBlocks.toaster);
        dropSelf(ModBlocks.milkJar);
        dropSelf(ModBlocks.cowJar);
        dropSelf(ModBlocks.cuttingBoard);
        dropSelf(ModBlocks.sink);
        dropSelf(ModBlocks.corner);
        dropSelf(ModBlocks.hangingCorner);
        for (final var oven : ModBlocks.ovens) {
            add(oven, this::createNameableBlockEntityTable);
        }
        add(ModBlocks.toolRack, this::createNameableBlockEntityTable);
        add(ModBlocks.spiceRack, this::createNameableBlockEntityTable);
        add(ModBlocks.fruitBasket, this::createNameableBlockEntityTable);
        add(ModBlocks.fridge, this::createNameableBlockEntityTable);
        add(ModBlocks.counter, this::createNameableBlockEntityTable);
        add(ModBlocks.cabinet, this::createNameableBlockEntityTable);
        add(ModBlocks.fridge, this::createNameableBlockEntityTable);
        for (final var kitchenFloor : ModBlocks.kitchenFloors) {
            dropSelf(kitchenFloor);
        }
    }
}
