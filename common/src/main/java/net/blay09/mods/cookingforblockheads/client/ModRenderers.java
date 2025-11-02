package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.balm.client.color.block.BalmBlockColorRegistrar;
import net.blay09.mods.balm.client.renderer.blockentity.BalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.client.renderer.chunk.BalmBlockRenderTypeRegistrar;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.block.entity.ModBlockEntities;
import net.blay09.mods.cookingforblockheads.client.render.*;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

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

    public static void initialize(BalmBlockColorRegistrar blockColors) {
        blockColors.register((state, world, pos, i) -> 0x3f76e4, ModBlocks.sinks.values());
    }

    public static void initialize(BalmBlockRenderTypeRegistrar blockRenderTypes) {
        ModBlocks.ovens.forEach((color, block) -> blockRenderTypes.setRenderLayer(block, ChunkSectionLayer.CUTOUT));
        ModBlocks.fridges.forEach((color, block) -> blockRenderTypes.setRenderLayer(block, ChunkSectionLayer.CUTOUT));
        blockRenderTypes.setRenderLayer(ModBlocks.milkJar, ChunkSectionLayer.CUTOUT);
        blockRenderTypes.setRenderLayer(ModBlocks.cowJar, ChunkSectionLayer.CUTOUT);
    }

}
