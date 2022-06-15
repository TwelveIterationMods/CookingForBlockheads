package net.blay09.mods.cookingforblockheads.compat;

import mcp.mobius.waila.api.*;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.block.*;
import net.blay09.mods.cookingforblockheads.tile.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

@WailaPlugin(id = CookingForBlockheads.MOD_ID)
public class WailaAddon implements IWailaPlugin {

    @Override
    public void register(IRegistrar registrar) {
        registrar.addComponent(new MilkJarDataProvider(), TooltipPosition.HEAD, MilkJarBlock.class);
        registrar.addComponent(new ToasterDataProvider(), TooltipPosition.BODY, ToasterBlock.class);
        registrar.addComponent(new OvenDataProvider(), TooltipPosition.BODY, OvenBlock.class);
        registrar.addComponent(new FridgeDataProvider(), TooltipPosition.BODY, FridgeBlock.class);
        registrar.addComponent(new SinkDataProvider(), TooltipPosition.BODY, SinkBlock.class);
    }

    public static class MilkJarDataProvider implements IBlockComponentProvider {

        @Override
        public void appendHead(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
            BlockEntity blockEntity = accessor.getBlockEntity();
            if (blockEntity instanceof CowJarBlockEntity cowJar && cowJar.getCustomName() != null) {
                tooltip.add(cowJar.getCustomName());
            }
        }

        @Override
        public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
            BlockEntity blockEntity = accessor.getBlockEntity();
            if (blockEntity instanceof MilkJarBlockEntity milkJar) {
                FluidTank fluidTank = milkJar.getFluidTank();
                tooltip.add(Component.translatable("waila.cookingforblockheads:milk_stored", fluidTank.getAmount(), fluidTank.getCapacity()));
            }
        }

    }

    public static class ToasterDataProvider implements IBlockComponentProvider {

        @Override
        public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
            BlockEntity blockEntity = accessor.getBlockEntity();
            if (blockEntity instanceof ToasterBlockEntity toaster) {
                if (toaster.isActive()) {
                    tooltip.add(Component.translatable("waila.cookingforblockheads:toast_progress", (int) (toaster.getToastProgress() * 100)));
                }
            }
        }

    }

    public static class OvenDataProvider implements IBlockComponentProvider {

        @Override
        public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
            BlockEntity tileEntity = accessor.getBlockEntity();
            if (tileEntity instanceof OvenBlockEntity oven) {
                if (oven.hasPowerUpgrade()) {
                    tooltip.add(Component.translatable("waila.cookingforblockheads:heating_unit"));
                }
            }
        }

    }

    public static class FridgeDataProvider implements IBlockComponentProvider {

        @Override
        public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
            BlockEntity blockEntity = accessor.getBlockEntity();
            if (blockEntity instanceof FridgeBlockEntity fridge) {
                if (fridge.hasIceUpgrade()) {
                    tooltip.add(Component.translatable("waila.cookingforblockheads:ice_unit"));
                }

                if (fridge.hasPreservationUpgrade()) {
                    tooltip.add(Component.translatable("waila.cookingforblockheads:preservation_chamber"));
                }
            }
        }

    }

    public static class SinkDataProvider implements IBlockComponentProvider {

        @Override
        public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
            BlockEntity blockEntity = accessor.getBlockEntity();
            if (blockEntity instanceof SinkBlockEntity sink && CookingForBlockheadsConfig.getActive().sinkRequiresWater) {
                FluidTank fluidTank = sink.getFluidTank();
                tooltip.add(Component.translatable("waila.cookingforblockheads:water_stored", fluidTank.getAmount(), fluidTank.getCapacity()));
            }
        }

    }
}
