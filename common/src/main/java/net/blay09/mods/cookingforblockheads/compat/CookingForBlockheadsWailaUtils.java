package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.CookingForBlockheadsConfig;
import net.blay09.mods.cookingforblockheads.block.entity.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Consumer;

public class CookingForBlockheadsWailaUtils {
    @FunctionalInterface
    public interface TooltipAppender {

        void appendTooltip(BlockEntity blockEntity, Player player, Consumer<Component> tooltipConsumer);
    }

    public static final ResourceLocation MILK_JAR_UID = ResourceLocation.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "milk_jar");

    public static final ResourceLocation TOASTER_UID = ResourceLocation.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "toaster");
    public static final ResourceLocation OVEN_UID = ResourceLocation.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "oven");
    public static final ResourceLocation FRIDGE_UID = ResourceLocation.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "fridge");
    public static final ResourceLocation SINK_UID = ResourceLocation.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "sink");

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

            if (fridge.hasPreservationUpgrade()) {
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
