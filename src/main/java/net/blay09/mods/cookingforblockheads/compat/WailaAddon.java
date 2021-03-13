package net.blay09.mods.cookingforblockheads.compat;

import mcp.mobius.waila.api.*;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.block.*;
import net.blay09.mods.cookingforblockheads.tile.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

@mcp.mobius.waila.api.WailaPlugin(CookingForBlockheads.MOD_ID)
public class WailaAddon implements IWailaPlugin {

    @Override
    public void register(IRegistrar registrar) {
        registrar.registerComponentProvider(new MilkJarDataProvider(), TooltipPosition.BODY, MilkJarBlock.class);
        registrar.registerComponentProvider(new ToasterDataProvider(), TooltipPosition.BODY, ToasterBlock.class);
        registrar.registerComponentProvider(new OvenDataProvider(), TooltipPosition.BODY, OvenBlock.class);
        registrar.registerComponentProvider(new FridgeDataProvider(), TooltipPosition.BODY, FridgeBlock.class);
        registrar.registerComponentProvider(new SinkDataProvider(), TooltipPosition.BODY, SinkBlock.class);
    }

    public static class MilkJarDataProvider implements IComponentProvider {

        @Override
        public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
            TileEntity tileEntity = accessor.getTileEntity();
            if (tileEntity instanceof MilkJarTileEntity) {
                MilkJarTileEntity tileMilkJar = (MilkJarTileEntity) tileEntity;
                tooltip.add(new TranslationTextComponent("waila.cookingforblockheads:milk_stored", (int) tileMilkJar.getMilkAmount(), (int) tileMilkJar.getMilkCapacity()));
            }
        }

    }

    public static class ToasterDataProvider implements IComponentProvider {

        @Override
        public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
            TileEntity tileEntity = accessor.getTileEntity();
            if (tileEntity instanceof ToasterTileEntity) {
                ToasterTileEntity tileToaster = (ToasterTileEntity) tileEntity;
                if (tileToaster.isActive()) {
                    tooltip.add(new TranslationTextComponent("waila.cookingforblockheads:toast_progress", (int) (tileToaster.getToastProgress() * 100)));
                }
            }
        }

    }

    public static class OvenDataProvider implements IComponentProvider {

        @Override
        public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
            TileEntity tileEntity = accessor.getTileEntity();
            if (tileEntity instanceof OvenTileEntity) {
                OvenTileEntity tileOven = (OvenTileEntity) tileEntity;
                if (tileOven.hasPowerUpgrade()) {
                    tooltip.add(new TranslationTextComponent("waila.cookingforblockheads:heating_unit"));
                }
            }
        }

    }

    public static class FridgeDataProvider implements IComponentProvider {

        @Override
        public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
            TileEntity tileEntity = accessor.getTileEntity();
            if (tileEntity instanceof FridgeTileEntity) {
                FridgeTileEntity tileFridge = (FridgeTileEntity) tileEntity;
                if (tileFridge.hasIceUpgrade()) {
                    tooltip.add(new TranslationTextComponent("waila.cookingforblockheads:ice_unit"));
                }

                if (tileFridge.hasPreservationUpgrade()) {
                    tooltip.add(new TranslationTextComponent("waila.cookingforblockheads:preservation_chamber"));
                }
            }
        }

    }

    public static class SinkDataProvider implements IComponentProvider {

        @Override
        public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
            TileEntity tileEntity = accessor.getTileEntity();
            if (tileEntity instanceof SinkTileEntity && CookingForBlockheadsConfig.COMMON.sinkRequiresWater.get()) {
                SinkTileEntity sink = (SinkTileEntity) tileEntity;
                tooltip.add(new TranslationTextComponent("waila.cookingforblockheads:water_stored", sink.getWaterAmount(), sink.getWaterCapacity()));
            }
        }

    }
}
