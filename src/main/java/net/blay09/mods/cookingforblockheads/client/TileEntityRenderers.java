package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.client.render.*;
import net.blay09.mods.cookingforblockheads.tile.*;
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
        ClientRegistry.bindTileEntitySpecialRenderer(ToolRackTileEntity.class, new ToolRackRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileMilkJar.class, new MilkJarRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCowJar.class, new CowJarRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(ToasterTileEntity.class, new ToasterRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(CookingTableTileEntity.class, new CookingTableRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(OvenTileEntity.class, new OvenRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(FridgeTileEntity.class, new FridgeRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(SpiceRackTileEntity.class, new SpiceRackRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(CounterTileEntity.class, new CounterRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(CabinetTileEntity.class, new CabinetRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(SinkTileEntity.class, new SinkRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(FruitBasketTileEntity.class, new FruitBasketRenderer());
    }

    @SubscribeEvent
    public static void initBlockColors(ColorHandlerEvent.Block event) {
        event.getBlockColors().register((state, world, pos, i) -> state.get(BlockKitchen.COLOR).getColorValue(), ModBlocks.fridge);
    }

}
