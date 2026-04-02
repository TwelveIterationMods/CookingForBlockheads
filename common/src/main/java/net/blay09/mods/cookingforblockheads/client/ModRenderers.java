package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.balm.client.color.block.BalmBlockColorRegistrar;
import net.blay09.mods.balm.client.renderer.blockentity.BalmBlockEntityRendererRegistrar;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.block.entity.ModBlockEntities;
import net.blay09.mods.cookingforblockheads.client.render.*;
import net.minecraft.client.color.block.BlockTintSources;

import java.util.List;

public class ModRenderers {

    public static void initialize(BalmBlockEntityRendererRegistrar blockEntityRenderers) {
        blockEntityRenderers.register(ModBlockEntities.toolRack, ToolRackRenderer::new);
        blockEntityRenderers.register(ModBlockEntities.milkJar, MilkJarRenderer::new);
        blockEntityRenderers.register(ModBlockEntities.cowJar, CowJarRenderer::new);
        blockEntityRenderers.register(ModBlockEntities.cookieJar, CookieJarRenderer::new);
        blockEntityRenderers.register(ModBlockEntities.toaster, ToasterRenderer::new);
        blockEntityRenderers.register(ModBlockEntities.cookingTable, CookingTableRenderer::new);
        blockEntityRenderers.register(ModBlockEntities.oven, OvenRenderer::new);
        blockEntityRenderers.register(ModBlockEntities.fridge, FridgeRenderer::new);
        blockEntityRenderers.register(ModBlockEntities.spiceRack, SpiceRackRenderer::new);
        blockEntityRenderers.register(ModBlockEntities.counter, CounterRenderer::new);
        blockEntityRenderers.register(ModBlockEntities.cabinet, CabinetRenderer::new);
        blockEntityRenderers.register(ModBlockEntities.sink, SinkRenderer::new);
        blockEntityRenderers.register(ModBlockEntities.chickenSink, ChickenSinkRenderer::new);
        blockEntityRenderers.register(ModBlockEntities.fruitBasket, FruitBasketRenderer::new);
    }

    public static void initialize(BalmBlockColorRegistrar blockColors) {
        blockColors.register(List.of(BlockTintSources.constant(0xff3f76e4)), ModBlocks.sinks.values());
        blockColors.register(List.of(BlockTintSources.constant(0xff3f76e4)), ModBlocks.chickenSinks.values());
        blockColors.register(List.of(BlockTintSources.constant(0xff3f76e4)), ModBlocks.coffeeMachine);
    }

}
