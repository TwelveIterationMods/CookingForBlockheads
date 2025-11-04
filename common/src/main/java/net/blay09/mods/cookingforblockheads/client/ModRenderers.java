package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.client.renderer.blockentity.BalmBlockEntityRendererRegistrar;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.block.entity.ModBlockEntities;
import net.blay09.mods.cookingforblockheads.client.render.*;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.level.block.Block;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

public class ModRenderers {

    public static void initialize(BalmBlockEntityRendererRegistrar blockEntityRenderers) {
        blockEntityRenderers.register(ModBlockEntities.toolRack, ToolRackRenderer::new);
        blockEntityRenderers.register(ModBlockEntities.milkJar, MilkJarRenderer::new);
        blockEntityRenderers.register(ModBlockEntities.cowJar, CowJarRenderer::new);
        blockEntityRenderers.register(ModBlockEntities.toaster, ToasterRenderer::new);
        blockEntityRenderers.register(ModBlockEntities.cookingTable, CookingTableRenderer::new);
        blockEntityRenderers.register(ModBlockEntities.oven, OvenRenderer::new);
        blockEntityRenderers.register(ModBlockEntities.fridge, FridgeRenderer::new);
        blockEntityRenderers.register(ModBlockEntities.spiceRack, SpiceRackRenderer::new);
        blockEntityRenderers.register(ModBlockEntities.counter, CounterRenderer::new);
        blockEntityRenderers.register(ModBlockEntities.cabinet, CabinetRenderer::new);
        blockEntityRenderers.register(ModBlockEntities.sink, SinkRenderer::new);
        blockEntityRenderers.register(ModBlockEntities.fruitBasket, FruitBasketRenderer::new);
    }

    public static void initialize(BalmRenderers renderers) {
        renderers.registerBlockColorHandler(id("sink"), (state, world, pos, i) -> 0x3f76e4, () -> ModBlocks.sinks.getAll().toArray(Block[]::new));

        ModBlocks.ovens.forEachDeferred((color, block) -> renderers.setBlockRenderType(block::asBlock, ChunkSectionLayer.CUTOUT));
        ModBlocks.fridges.forEachDeferred((color, block) -> renderers.setBlockRenderType(block::asBlock, ChunkSectionLayer.CUTOUT));
        renderers.setBlockRenderType(() -> ModBlocks.milkJar.value(), ChunkSectionLayer.CUTOUT);
        renderers.setBlockRenderType(() -> ModBlocks.cowJar.value(), ChunkSectionLayer.CUTOUT);
    }

}
