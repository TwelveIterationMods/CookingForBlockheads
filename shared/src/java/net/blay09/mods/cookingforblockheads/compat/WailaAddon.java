//package net.blay09.mods.cookingforblockheads.compat; TODO
//
//import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
//import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
//import net.blay09.mods.cookingforblockheads.block.*;
//import net.blay09.mods.cookingforblockheads.tile.*;
//import net.minecraft.network.chat.Component;
//import net.minecraft.network.chat.TranslatableComponent;
//import net.minecraft.world.level.block.entity.BlockEntity;
//
//import java.util.List;
//
//@mcp.mobius.waila.api.WailaPlugin(CookingForBlockheads.MOD_ID)
//public class WailaAddon implements IWailaPlugin {
//
//    @Override
//    public void register(IRegistrar registrar) {
//        registrar.registerComponentProvider(new MilkJarDataProvider(), TooltipPosition.HEAD, MilkJarBlock.class);
//        registrar.registerComponentProvider(new MilkJarDataProvider(), TooltipPosition.BODY, MilkJarBlock.class);
//        registrar.registerComponentProvider(new ToasterDataProvider(), TooltipPosition.BODY, ToasterBlock.class);
//        registrar.registerComponentProvider(new OvenDataProvider(), TooltipPosition.BODY, OvenBlock.class);
//        registrar.registerComponentProvider(new FridgeDataProvider(), TooltipPosition.BODY, FridgeBlock.class);
//        registrar.registerComponentProvider(new SinkDataProvider(), TooltipPosition.BODY, SinkBlock.class);
//    }
//
//    public static class MilkJarDataProvider implements IComponentProvider {
//
//        @Override
//        public void appendHead(List<Component> tooltip, IDataAccessor accessor, IPluginConfig config) {
//            BlockEntity blockEntity = accessor.getTileEntity();
//            if (blockEntity instanceof CowJarBlockEntity cowJar && cowJar.getCustomName() != null) {
//                tooltip.clear();
//                tooltip.add(cowJar.getCustomName());
//            }
//        }
//
//        @Override
//        public void appendBody(List<Component> tooltip, IDataAccessor accessor, IPluginConfig config) {
//            BlockEntity blockEntity = accessor.getTileEntity();
//            if (blockEntity instanceof MilkJarBlockEntity milkJar) {
//                tooltip.add(new TranslatableComponent("waila.cookingforblockheads:milk_stored", (int) milkJar.getMilkAmount(), (int) milkJar.getMilkCapacity()));
//            }
//        }
//
//    }
//
//    public static class ToasterDataProvider implements IComponentProvider {
//
//        @Override
//        public void appendBody(List<Component> tooltip, IDataAccessor accessor, IPluginConfig config) {
//            BlockEntity blockEntity = accessor.getTileEntity();
//            if (blockEntity instanceof ToasterBlockEntity toaster) {
//                if (toaster.isActive()) {
//                    tooltip.add(new TranslatableComponent("waila.cookingforblockheads:toast_progress", (int) (toaster.getToastProgress() * 100)));
//                }
//            }
//        }
//
//    }
//
//    public static class OvenDataProvider implements IComponentProvider {
//
//        @Override
//        public void appendBody(List<Component> tooltip, IDataAccessor accessor, IPluginConfig config) {
//            BlockEntity tileEntity = accessor.getTileEntity();
//            if (tileEntity instanceof OvenBlockEntity oven) {
//                if (oven.hasPowerUpgrade()) {
//                    tooltip.add(new TranslatableComponent("waila.cookingforblockheads:heating_unit"));
//                }
//            }
//        }
//
//    }
//
//    public static class FridgeDataProvider implements IComponentProvider {
//
//        @Override
//        public void appendBody(List<Component> tooltip, IDataAccessor accessor, IPluginConfig config) {
//            BlockEntity blockEntity = accessor.getTileEntity();
//            if (blockEntity instanceof FridgeBlockEntity fridge) {
//                if (fridge.hasIceUpgrade()) {
//                    tooltip.add(new TranslatableComponent("waila.cookingforblockheads:ice_unit"));
//                }
//
//                if (fridge.hasPreservationUpgrade()) {
//                    tooltip.add(new TranslatableComponent("waila.cookingforblockheads:preservation_chamber"));
//                }
//            }
//        }
//
//    }
//
//    public static class SinkDataProvider implements IComponentProvider {
//
//        @Override
//        public void appendBody(List<Component> tooltip, IDataAccessor accessor, IPluginConfig config) {
//            BlockEntity blockEntity = accessor.getTileEntity();
//            if (blockEntity instanceof SinkBlockEntity sink && CookingForBlockheadsConfig.getActive().sinkRequiresWater) {
//                tooltip.add(new TranslatableComponent("waila.cookingforblockheads:water_stored", sink.getWaterAmount(), sink.getWaterCapacity()));
//            }
//        }
//
//    }
//}
