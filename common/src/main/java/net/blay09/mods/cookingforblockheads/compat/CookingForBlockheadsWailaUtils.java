package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.balm.platform.fluid.FluidTank;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.block.entity.*;
import net.blay09.mods.cookingforblockheads.api.UpgradeablePreservation;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Consumer;

public class CookingForBlockheadsWailaUtils {
    @FunctionalInterface
    public interface TooltipAppender {

        void appendTooltip(BlockEntity blockEntity, Player player, Consumer<Component> tooltipConsumer);
    }

    public static final Identifier MILK_JAR_UID = Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "milk_jar");

    public static final Identifier TOASTER_UID = Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "toaster");
    public static final Identifier OVEN_UID = Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "oven");
    public static final Identifier FRIDGE_UID = Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "fridge");
    public static final Identifier PRESERVATION_CHAMBER_UID = Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "preservation_chamber");
    public static final Identifier SINK_UID = Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "sink");

    public static void appendMilkJarTooltip(BlockEntity blockEntity, Player player, Consumer<Component> tooltipConsumer) {
        if (blockEntity instanceof CowJarBlockEntity cowJar && cowJar.getCustomName() != null) {
            tooltipConsumer.accept(cowJar.getCustomName());
        }
        if (blockEntity instanceof MilkJarBlockEntity milkJar) {
            FluidTank fluidTank = milkJar.getFluidTank();
            tooltipConsumer.accept(Component.translatable("waila.cookingforblockheads.milk_stored", fluidTank.getAmount(), fluidTank.getCapacity()));
        }
    }

    public static void appendToasterTooltip(BlockEntity blockEntity, Player player, Consumer<Component> tooltipConsumer) {
        if (blockEntity instanceof ToasterBlockEntity toaster && toaster.isActive()) {
            tooltipConsumer.accept(Component.translatable("waila.cookingforblockheads.toast_progress", (int) (toaster.getToastProgress() * 100)));
        }
    }

    public static void appendOvenTooltip(BlockEntity blockEntity, Player player, Consumer<Component> tooltipConsumer) {
        if (blockEntity instanceof OvenBlockEntity oven && oven.hasPowerUpgrade()) {
            tooltipConsumer.accept(Component.translatable("waila.cookingforblockheads.heating_unit"));
        }
    }

    public static void appendFridgeTooltip(BlockEntity blockEntity, Player player, Consumer<Component> tooltipConsumer) {
        if (blockEntity instanceof FridgeBlockEntity fridge) {
            if (fridge.hasIceUpgrade()) {
                tooltipConsumer.accept(Component.translatable("waila.cookingforblockheads.ice_unit"));
            }
        }
    }

    public static void appendPreservationChamberTooltip(BlockEntity blockEntity, Player player, Consumer<Component> tooltipConsumer) {
        if (blockEntity instanceof UpgradeablePreservation upgradeable) {
            if (upgradeable.hasPreservationUpgrade()) {
                tooltipConsumer.accept(Component.translatable("waila.cookingforblockheads.preservation_chamber"));
            }
        }
    }

    public static void appendSinkTooltip(BlockEntity blockEntity, Player player, Consumer<Component> tooltipConsumer) {
        if (blockEntity instanceof SinkBlockEntity sink && CookingForBlockheadsConfig.getActive().sinkRequiresWater) {
            FluidTank fluidTank = sink.getFluidTank();
            tooltipConsumer.accept(Component.translatable("waila.cookingforblockheads.water_stored", fluidTank.getAmount(), fluidTank.getCapacity()));
        }
    }

    private CookingForBlockheadsWailaUtils() {
    }
}
