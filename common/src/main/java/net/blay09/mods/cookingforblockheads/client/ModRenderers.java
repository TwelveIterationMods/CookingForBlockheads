package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.cookingforblockheads.block.*;
import net.blay09.mods.cookingforblockheads.client.render.*;
import net.blay09.mods.cookingforblockheads.block.entity.ModBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

public class ModRenderers {

    public static void initialize(BalmRenderers renderers) {
        renderers.registerBlockEntityRenderer(id("tool_rack"), ModBlockEntities.toolRack::get, ToolRackRenderer::new);
        renderers.registerBlockEntityRenderer(id("milk_jar"), ModBlockEntities.milkJar::get, MilkJarRenderer::new);
        renderers.registerBlockEntityRenderer(id("cow_jar"), ModBlockEntities.cowJar::get, CowJarRenderer::new);
        renderers.registerBlockEntityRenderer(id("toaster"), ModBlockEntities.toaster::get, ToasterRenderer::new);
        renderers.registerBlockEntityRenderer(id("cooking_table"), ModBlockEntities.cookingTable::get, CookingTableRenderer::new);
        renderers.registerBlockEntityRenderer(id("oven"), ModBlockEntities.oven::get, OvenRenderer::new);
        renderers.registerBlockEntityRenderer(id("fridge"), ModBlockEntities.fridge::get, FridgeRenderer::new);
        renderers.registerBlockEntityRenderer(id("spice_rack"), ModBlockEntities.spiceRack::get, SpiceRackRenderer::new);
        renderers.registerBlockEntityRenderer(id("counter"), ModBlockEntities.counter::get, CounterRenderer::new);
        renderers.registerBlockEntityRenderer(id("cabinet"), ModBlockEntities.cabinet::get, CabinetRenderer::new);
        renderers.registerBlockEntityRenderer(id("sink"), ModBlockEntities.sink::get, SinkRenderer::new);
        renderers.registerBlockEntityRenderer(id("fruit_basket"), ModBlockEntities.fruitBasket::get, FruitBasketRenderer::new);

        renderers.registerBlockColorHandler(id("sink"), (state, world, pos, i) -> 0x3f76e4, () -> {
            final var allSinks = Arrays.copyOf(ModBlocks.dyedSinks, ModBlocks.dyedSinks.length + 1);
            allSinks[allSinks.length - 1] = ModBlocks.sink;
            return allSinks;
        });

        // We cannot use foreach loops because the array is only filled at a later time on Neo/Forge
        final var ovens = ModBlocks.ovens;
        for (int i = 0; i < ovens.length; i++) {
            final var j = i;
            renderers.setBlockRenderType(() -> ovens[j], ChunkSectionLayer.CUTOUT);
        }
        final var fridges = ModBlocks.fridges;
        for (int i = 0; i < fridges.length; i++) {
            final var j = i;
            renderers.setBlockRenderType(() -> ovens[j], ChunkSectionLayer.CUTOUT);
        }
        renderers.setBlockRenderType(() -> ModBlocks.milkJar, ChunkSectionLayer.CUTOUT);
        renderers.setBlockRenderType(() -> ModBlocks.cowJar, ChunkSectionLayer.CUTOUT);
    }

}
