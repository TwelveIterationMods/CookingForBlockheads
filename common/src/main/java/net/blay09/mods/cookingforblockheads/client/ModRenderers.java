package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.cookingforblockheads.block.BaseKitchenBlock;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.block.SinkBlock;
import net.blay09.mods.cookingforblockheads.client.render.*;
import net.blay09.mods.cookingforblockheads.block.entity.ModBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;

public class ModRenderers {

    public static void initialize(BalmRenderers renderers) {
        renderers.registerBlockEntityRenderer(ModBlockEntities.toolRack::get, ToolRackRenderer::new);
        renderers.registerBlockEntityRenderer(ModBlockEntities.milkJar::get, MilkJarRenderer::new);
        renderers.registerBlockEntityRenderer(ModBlockEntities.cowJar::get, CowJarRenderer::new);
        renderers.registerBlockEntityRenderer(ModBlockEntities.toaster::get, ToasterRenderer::new);
        renderers.registerBlockEntityRenderer(ModBlockEntities.cookingTable::get, CookingTableRenderer::new);
        renderers.registerBlockEntityRenderer(ModBlockEntities.oven::get, OvenRenderer::new);
        renderers.registerBlockEntityRenderer(ModBlockEntities.fridge::get, FridgeRenderer::new);
        renderers.registerBlockEntityRenderer(ModBlockEntities.spiceRack::get, SpiceRackRenderer::new);
        renderers.registerBlockEntityRenderer(ModBlockEntities.counter::get, CounterRenderer::new);
        renderers.registerBlockEntityRenderer(ModBlockEntities.cabinet::get, CabinetRenderer::new);
        renderers.registerBlockEntityRenderer(ModBlockEntities.sink::get, SinkRenderer::new);
        renderers.registerBlockEntityRenderer(ModBlockEntities.fruitBasket::get, FruitBasketRenderer::new);

        renderers.registerBlockColorHandler((state, world, pos, i) -> state.getValue(BaseKitchenBlock.COLOR).getTextColor(),
                () -> new Block[]{ModBlocks.fridge});
        renderers.registerBlockColorHandler((state, world, pos, i) -> 0x3f76e4, () -> {
            final var allSinks = Arrays.copyOf(ModBlocks.dyedSinks, ModBlocks.dyedSinks.length + 1);
            allSinks[allSinks.length - 1] = ModBlocks.sink;
            return allSinks;
        });

        for (final var oven : ModBlocks.ovens) {
            renderers.setBlockRenderType(() -> oven, RenderType.cutout());
        }
        renderers.setBlockRenderType(() -> ModBlocks.milkJar, RenderType.cutout());
        renderers.setBlockRenderType(() -> ModBlocks.cowJar, RenderType.cutout());
        renderers.setBlockRenderType(() -> ModBlocks.fridge, RenderType.cutout());
    }

}
