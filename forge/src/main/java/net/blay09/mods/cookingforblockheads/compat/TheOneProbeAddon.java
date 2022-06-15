package net.blay09.mods.cookingforblockheads.compat;

import mcjty.theoneprobe.api.*;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.block.*;
import net.blay09.mods.cookingforblockheads.tile.*;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.InterModComms;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class TheOneProbeAddon {

    public static void register() {
        InterModComms.sendTo(Compat.THEONEPROBE, "getTheOneProbe", TopInitializer::new);
    }

    public static class TopInitializer implements Function<ITheOneProbe, Void> {
        @Nullable
        @Override
        public Void apply(@Nullable ITheOneProbe top) {
            if (top != null) {
                top.registerProvider(new ProbeInfoProvider());
            }
            return null;
        }
    }

    public static class ProbeInfoProvider implements IProbeInfoProvider {

        @Override
        public ResourceLocation getID() {
            return new ResourceLocation(CookingForBlockheads.MOD_ID, CookingForBlockheads.MOD_ID);
        }

        @Override
        public void addProbeInfo(ProbeMode mode, IProbeInfo info, Player playerEntity, Level level, BlockState state, IProbeHitData data) {
            if (state.getBlock() instanceof MilkJarBlock) {
                MilkJarBlockEntity tileEntity = tryGetTileEntity(level, data.getPos(), MilkJarBlockEntity.class);
                if (tileEntity != null) {
                    addMilkJarInfo(tileEntity, info);
                }
            } else if (state.getBlock() instanceof ToasterBlock) {
                ToasterBlockEntity tileEntity = tryGetTileEntity(level, data.getPos(), ToasterBlockEntity.class);
                if (tileEntity != null) {
                    addToasterInfo(tileEntity, info);
                }
            } else if (state.getBlock() instanceof OvenBlock) {
                OvenBlockEntity tileEntity = tryGetTileEntity(level, data.getPos(), OvenBlockEntity.class);
                if (tileEntity != null && tileEntity.hasPowerUpgrade()) {
                    info.text(Component.translatable("waila.cookingforblockheads:heating_unit"));
                }
            } else if (state.getBlock() instanceof FridgeBlock) {
                FridgeBlockEntity tileEntity = tryGetTileEntity(level, data.getPos(), FridgeBlockEntity.class);
                if (tileEntity != null && tileEntity.getBaseFridge().hasIceUpgrade()) {
                    info.text(Component.translatable("waila.cookingforblockheads:ice_unit"));
                }

                if (tileEntity != null && tileEntity.getBaseFridge().hasPreservationUpgrade()) {
                    info.text(Component.translatable("waila.cookingforblockheads:preservation_chamber"));
                }
            } else if (state.getBlock() instanceof SinkBlock) {
                SinkBlockEntity sink = tryGetTileEntity(level, data.getPos(), SinkBlockEntity.class);
                if (sink != null && CookingForBlockheadsConfig.getActive().sinkRequiresWater) {
                    info.text(Component.translatable("waila.cookingforblockheads:water_stored", sink.getFluidTank().getAmount(), sink.getFluidTank().getCapacity()));
                }
            }
        }

        private void addMilkJarInfo(MilkJarBlockEntity milkJar, IProbeInfo info) {
            info.text(Component.translatable("waila.cookingforblockheads:milk_stored", milkJar.getFluidTank().getAmount(), milkJar.getFluidTank().getCapacity()));
        }

        private void addToasterInfo(ToasterBlockEntity tileEntity, IProbeInfo info) {
            if (tileEntity.isActive()) {
                info.text(Component.translatable("waila.cookingforblockheads:toast_progress", (int) (tileEntity.getToastProgress() * 100)));
                info.progress((int) (tileEntity.getToastProgress() * 100), 100);
            }
        }

    }

    @Nullable
    @SuppressWarnings("unchecked")
    private static <T extends BlockEntity> T tryGetTileEntity(Level level, BlockPos pos, Class<T> tileClass) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity != null && tileClass.isAssignableFrom(blockEntity.getClass())) {
            return (T) blockEntity;
        }
        return null;
    }
}
