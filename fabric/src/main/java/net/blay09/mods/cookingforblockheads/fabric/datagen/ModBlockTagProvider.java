package net.blay09.mods.cookingforblockheads.fabric.datagen;

import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.tag.ModBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider<Block> {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BLOCK, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        final var mineablePickaxeTag = TagKey.create(Registries.BLOCK, new ResourceLocation("minecraft", "mineable/pickaxe"));
        final var mineablePickaxeBuilder = getOrCreateTagBuilder(mineablePickaxeTag);
        mineablePickaxeBuilder.add(ModBlocks.cookingTable,
                ModBlocks.fridge,
                ModBlocks.sink,
                ModBlocks.counter,
                ModBlocks.cabinet,
                ModBlocks.corner,
                ModBlocks.oven,
                ModBlocks.toaster,
                ModBlocks.milkJar,
                ModBlocks.cowJar,
                ModBlocks.hangingCorner);
        for (final var kitchenFloor : ModBlocks.kitchenFloors) {
            mineablePickaxeBuilder.add(kitchenFloor);
        }

        final var mineableAxeTag = TagKey.create(Registries.BLOCK, new ResourceLocation("minecraft", "mineable/axe"));
        final var mineableAxeBuilder = getOrCreateTagBuilder(mineableAxeTag);
        mineableAxeBuilder.add(ModBlocks.toolRack, ModBlocks.spiceRack, ModBlocks.fruitBasket, ModBlocks.cuttingBoard);

        getOrCreateTagBuilder(ModBlockTags.DYEABLE).add(ModBlocks.cookingTable,
                ModBlocks.fridge,
                ModBlocks.sink,
                ModBlocks.counter,
                ModBlocks.cabinet,
                ModBlocks.corner,
                ModBlocks.hangingCorner);

        final var kitchenConnectors = getOrCreateTagBuilder(ModBlockTags.KITCHEN_CONNECTORS);
        kitchenConnectors.add(ModBlocks.corner, ModBlocks.hangingCorner);
        for (final var kitchenFloor : ModBlocks.kitchenFloors) {
            kitchenConnectors.add(kitchenFloor);
        }
    }

}
