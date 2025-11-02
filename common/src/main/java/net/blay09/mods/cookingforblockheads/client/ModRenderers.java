package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.block.entity.ModBlockEntities;
import net.blay09.mods.cookingforblockheads.client.render.*;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.level.block.Block;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

public class ModRenderers {

    public static void initialize(BalmRenderers renderers) {
        renderers.registerBlockEntityRenderer(id("tool_rack"), ModBlockEntities.toolRack::value, ToolRackRenderer::new);
        renderers.registerBlockEntityRenderer(id("milk_jar"), ModBlockEntities.milkJar::value, MilkJarRenderer::new);
        renderers.registerBlockEntityRenderer(id("cow_jar"), ModBlockEntities.cowJar::value, CowJarRenderer::new);
        renderers.registerBlockEntityRenderer(id("toaster"), ModBlockEntities.toaster::value, ToasterRenderer::new);
        renderers.registerBlockEntityRenderer(id("cooking_table"), ModBlockEntities.cookingTable::value, CookingTableRenderer::new);
        renderers.registerBlockEntityRenderer(id("oven"), ModBlockEntities.oven::value, OvenRenderer::new);
        renderers.registerBlockEntityRenderer(id("fridge"), ModBlockEntities.fridge::value, FridgeRenderer::new);
        renderers.registerBlockEntityRenderer(id("spice_rack"), ModBlockEntities.spiceRack::value, SpiceRackRenderer::new);
        renderers.registerBlockEntityRenderer(id("counter"), ModBlockEntities.counter::value, CounterRenderer::new);
        renderers.registerBlockEntityRenderer(id("cabinet"), ModBlockEntities.cabinet::value, CabinetRenderer::new);
        renderers.registerBlockEntityRenderer(id("sink"), ModBlockEntities.sink::value, SinkRenderer::new);
        renderers.registerBlockEntityRenderer(id("fruit_basket"), ModBlockEntities.fruitBasket::value, FruitBasketRenderer::new);

        renderers.registerBlockColorHandler(id("sink"), (state, world, pos, i) -> 0x3f76e4, () -> ModBlocks.sinks.getAll().toArray(Block[]::new));

        ModBlocks.ovens.forEachDeferred((color, block) -> renderers.setBlockRenderType(block::asBlock, ChunkSectionLayer.CUTOUT));
        ModBlocks.fridges.forEachDeferred((color, block) -> renderers.setBlockRenderType(block::asBlock, ChunkSectionLayer.CUTOUT));
        renderers.setBlockRenderType(() -> ModBlocks.milkJar.value(), ChunkSectionLayer.CUTOUT);
        renderers.setBlockRenderType(() -> ModBlocks.cowJar.value(), ChunkSectionLayer.CUTOUT);
    }

}
