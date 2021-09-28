package net.blay09.mods.cookingforblockheads.compat;

import com.google.common.base.Function;
import mcjty.theoneprobe.api.*;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.block.*;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.tile.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.InterModComms;

import javax.annotation.Nullable;

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
        public String getID() {
            return CookingForBlockheads.MOD_ID;
        }

        @Override
        public void addProbeInfo(ProbeMode mode, IProbeInfo info, PlayerEntity playerEntity, World world, BlockState state, IProbeHitData data) {
            if (state.getBlock() instanceof MilkJarBlock) {
                MilkJarTileEntity tileEntity = tryGetTileEntity(world, data.getPos(), MilkJarTileEntity.class);
                if (tileEntity != null) {
                    addMilkJarInfo(tileEntity, info);
                }
            } else if (state.getBlock() instanceof ToasterBlock) {
                ToasterTileEntity tileEntity = tryGetTileEntity(world, data.getPos(), ToasterTileEntity.class);
                if (tileEntity != null) {
                    addToasterInfo(tileEntity, info);
                }
            } else if (state.getBlock() instanceof OvenBlock) {
                OvenTileEntity tileEntity = tryGetTileEntity(world, data.getPos(), OvenTileEntity.class);
                if (tileEntity != null && tileEntity.hasPowerUpgrade()) {
                    info.text(new TranslationTextComponent("waila.cookingforblockheads:heating_unit"));
                }
            } else if (state.getBlock() instanceof FridgeBlock) {
                FridgeTileEntity tileEntity = tryGetTileEntity(world, data.getPos(), FridgeTileEntity.class);
                if (tileEntity != null && tileEntity.getBaseFridge().hasIceUpgrade()) {
                    info.text(new TranslationTextComponent("waila.cookingforblockheads:ice_unit"));
                }

                if (tileEntity != null && tileEntity.getBaseFridge().hasPreservationUpgrade()) {
                    info.text(new TranslationTextComponent("waila.cookingforblockheads:preservation_chamber"));
                }
            } else if (state.getBlock() instanceof SinkBlock) {
                SinkTileEntity tileEntity = tryGetTileEntity(world, data.getPos(), SinkTileEntity.class);
                if (tileEntity != null && CookingForBlockheadsConfig.COMMON.sinkRequiresWater.get()) {
                    info.text(new TranslationTextComponent("waila.cookingforblockheads:water_stored", tileEntity.getWaterAmount(), tileEntity.getWaterCapacity()));
                }
            }
        }

        private void addMilkJarInfo(MilkJarTileEntity tileEntity, IProbeInfo info) {
            info.text(new TranslationTextComponent("waila.cookingforblockheads:milk_stored", (int) tileEntity.getMilkAmount(), (int) tileEntity.getMilkCapacity()));
        }

        private void addToasterInfo(ToasterTileEntity tileEntity, IProbeInfo info) {
            if (tileEntity.isActive()) {
                info.text(new TranslationTextComponent("waila.cookingforblockheads:toast_progress", (int) (tileEntity.getToastProgress() * 100)));
                info.progress((int) (tileEntity.getToastProgress() * 100), 100);
            }
        }

    }

    @Nullable
    @SuppressWarnings("unchecked")
    private static <T extends TileEntity> T tryGetTileEntity(World world, BlockPos pos, Class<T> tileClass) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null && tileClass.isAssignableFrom(tileEntity.getClass())) {
            return (T) tileEntity;
        }
        return null;
    }
}
