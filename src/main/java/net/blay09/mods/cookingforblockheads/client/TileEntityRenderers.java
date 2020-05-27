package net.blay09.mods.cookingforblockheads.client;


import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.client.render.*;
import net.blay09.mods.cookingforblockheads.tile.ModTileEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


@Mod.EventBusSubscriber(modid = CookingForBlockheads.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TileEntityRenderers {

    @SubscribeEvent
    public static void registerRenderers(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.toolRack, ToolRackRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.milkJar, MilkJarRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.cowJar, CowJarRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.toaster, ToasterRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.cookingTable, CookingTableRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.oven, OvenRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.fridge, FridgeRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.spiceRack, SpiceRackRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.counter, CounterRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.cabinet, CabinetRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.sink, SinkRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.fruitBasket, FruitBasketRenderer::new);
    }

    @SubscribeEvent
    public static void initBlockColors(ColorHandlerEvent.Block event) {
        if (ModBlocks.fridge == null) {
            throw new RuntimeException("Something crashed the event bus earlier in the loading stage. This is NOT a Cooking for Blockheads issue! Scroll up in your latest.log to find the real crash log.");
        }

        event.getBlockColors().register((state, world, pos, i) -> state.get(BlockKitchen.COLOR).getColorValue(), ModBlocks.fridge);
        event.getBlockColors().register((state, world, pos, i) -> 4159204, ModBlocks.sink);
    }

}
