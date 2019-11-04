package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.client.render.*;
import net.blay09.mods.cookingforblockheads.tile.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = CookingForBlockheads.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TileEntityRenderers {

    @SubscribeEvent
    public static void registerRenderers(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntitySpecialRenderer(ToolRackTileEntity.class, new ToolRackRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileMilkJar.class, new MilkJarRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileCowJar.class, new CowJarRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(ToasterTileEntity.class, new ToasterRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(CookingTableTileEntity.class, new CookingTableRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(OvenTileEntity.class, new OvenRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileFridge.class, new FridgeRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(SpiceRackTileEntity.class, new SpiceRackRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(CounterTileEntity.class, new CounterRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(CabinetTileEntity.class, new CabinetRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileSink.class, new SinkRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(FruitBasketTileEntity.class, new FruitBasketRenderer());
    }

}
