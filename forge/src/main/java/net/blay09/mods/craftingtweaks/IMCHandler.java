package net.blay09.mods.craftingtweaks;

import net.blay09.mods.craftingtweaks.api.*;
import net.blay09.mods.craftingtweaks.api.impl.DefaultCraftingGrid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;
import java.util.function.Predicate;

public class IMCHandler {

    public static final Logger logger = LogManager.getLogger();

    @SuppressWarnings("unchecked")
    public static void processInterMod(InterModProcessEvent event) {
        event.getIMCStream(it -> it.equals("RegisterProvider") || it.equals("RegisterProviderV2") || it.equals("RegisterProviderV3")).forEach(message -> {
            CompoundTag tagCompound = (CompoundTag) message.messageSupplier().get();
            String senderModId = message.senderModId();

            String containerClassName = tagCompound.getString("ContainerClass");
            Predicate<AbstractContainerMenu> containerPredicate = it -> it.getClass().getName().equals(containerClassName);

            String validContainerPredicateLegacy = tagCompound.getString("ContainerCallback");
            if (!validContainerPredicateLegacy.isEmpty()) {
                try {
                    Class<?> functionClass = Class.forName(validContainerPredicateLegacy);
                    if (!Function.class.isAssignableFrom(functionClass)) {
                        logger.error("{} sent a container callback that's not even a function", senderModId);
                        return;
                    }
                    Function<AbstractContainerMenu, Boolean> function = (Function<AbstractContainerMenu, Boolean>) functionClass.getDeclaredConstructor().newInstance();
                    containerPredicate = function::apply;
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    logger.error("{} sent an invalid container callback.", senderModId);
                }
            }

            String validContainerPredicate = tagCompound.getString("ValidContainerPredicate");
            if (!validContainerPredicate.isEmpty()) {
                try {
                    Class<?> predicateClass = Class.forName(validContainerPredicate);
                    if (!Predicate.class.isAssignableFrom(predicateClass)) {
                        logger.error("{} sent an invalid ValidContainerPredicate - it must implement Predicate<Container>", senderModId);
                        return;
                    }
                    containerPredicate = (Predicate<AbstractContainerMenu>) predicateClass.getDeclaredConstructor().newInstance();
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    logger.error("{} sent an invalid ValidContainerPredicate: {}", senderModId, e.getMessage());
                }
            }

            String getGridStartFunction = tagCompound.getString("GetGridStartFunction");
            Function<AbstractContainerMenu, Integer> gridStartFunction = null;
            if (!getGridStartFunction.isEmpty()) {
                try {
                    Class<?> functionClass = Class.forName(getGridStartFunction);
                    if (!Function.class.isAssignableFrom(functionClass)) {
                        logger.error("{} sent an invalid GetGridStartFunction - it must implement Function<Container, Integer>", senderModId);
                        return;
                    }
                    gridStartFunction = (Function<AbstractContainerMenu, Integer>) functionClass.getDeclaredConstructor().newInstance();
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    logger.error("{} sent an invalid GetGridStartFunction: {}", senderModId, e.getMessage());
                }
            }

            final Predicate<AbstractContainerMenu> effectiveContainerPredicate = containerPredicate;
            final Function<AbstractContainerMenu, Integer> effectiveGridStartFunction = gridStartFunction;
            CraftingTweaksAPI.registerCraftingGridProvider(new CraftingGridProvider() {
                @Override
                public String getModId() {
                    return senderModId;
                }

                @Override
                public boolean handles(AbstractContainerMenu menu) {
                    return effectiveContainerPredicate.test(menu);
                }

                @Override
                public void buildCraftingGrids(CraftingGridBuilder builder, AbstractContainerMenu menu) {
                    int gridSlotNumber = getIntOr(tagCompound, "GridSlotNumber", 1);
                    int gridSize = getIntOr(tagCompound, "GridSize", 9);

                    CraftingGridDecorator grid;
                    if (effectiveGridStartFunction != null) {
                        grid = new DefaultCraftingGrid(new ResourceLocation(senderModId, "default"), gridSlotNumber, gridSize) {
                            @Override
                            public int getGridStartSlot(Player player, AbstractContainerMenu menu) {
                                return effectiveGridStartFunction.apply(menu);
                            }
                        };
                        builder.addCustomGrid((CraftingGrid) grid);
                    } else {
                        grid = builder.addGrid(gridSlotNumber, gridSize);
                    }

                    int buttonOffsetX = tagCompound.contains("ButtonOffsetX") ? tagCompound.getInt("ButtonOffsetX") : -16;
                    int buttonOffsetY = tagCompound.contains("ButtonOffsetY") ? tagCompound.getInt("ButtonOffsetY") : 16;

                    ButtonAlignment alignToGrid = null;
                    String alignToGridName = tagCompound.getString("AlignToGrid");
                    switch (alignToGridName.toLowerCase()) {
                        case "north", "up" -> alignToGrid = ButtonAlignment.TOP;
                        case "south", "down" -> alignToGrid = ButtonAlignment.BOTTOM;
                        case "east", "right" -> alignToGrid = ButtonAlignment.LEFT;
                        case "west", "left" -> alignToGrid = ButtonAlignment.RIGHT;
                    }
                    grid.setButtonAlignment(alignToGrid);

                    if (tagCompound.getBoolean("HideButtons")) {
                        grid.hideAllTweakButtons();
                    }

                    if (tagCompound.getBoolean("PhantomItems")) {
                        grid.usePhantomItems();
                    }

                    CompoundTag rotateCompound = tagCompound.getCompound("TweakRotate");
                    if (!getBoolOr(rotateCompound, "Enabled", true)) {
                        grid.disableTweak(TweakType.Rotate);
                    }
                    if (!getBoolOr(rotateCompound, "ShowButton", true)) {
                        grid.hideTweakButton(TweakType.Rotate);
                    }
                    grid.setButtonPosition(TweakType.Rotate, buttonOffsetX + getIntOr(rotateCompound, "ButtonX", 0), buttonOffsetY + getIntOr(rotateCompound, "ButtonY", 0));

                    CompoundTag balanceCompound = tagCompound.getCompound("TweakBalance");
                    if (!getBoolOr(balanceCompound, "Enabled", true)) {
                        grid.disableTweak(TweakType.Balance);
                    }
                    if (!getBoolOr(balanceCompound, "ShowButton", true)) {
                        grid.hideTweakButton(TweakType.Balance);
                    }
                    grid.setButtonPosition(TweakType.Balance, buttonOffsetX + getIntOr(balanceCompound, "ButtonX", 0), buttonOffsetY + getIntOr(balanceCompound, "ButtonY", 0));

                    CompoundTag clearCompound = tagCompound.getCompound("TweakClear");
                    if (!getBoolOr(clearCompound, "Enabled", true)) {
                        grid.disableTweak(TweakType.Clear);
                    }
                    if (!getBoolOr(clearCompound, "ShowButton", true)) {
                        grid.hideTweakButton(TweakType.Clear);
                    }
                    grid.setButtonPosition(TweakType.Clear, buttonOffsetX + getIntOr(clearCompound, "ButtonX", 0), buttonOffsetY + getIntOr(balanceCompound, "ButtonY", 0));
                }
            });
            logger.info("{} has registered {} for CraftingTweaks", senderModId, containerClassName);
        });
    }

    private static int getIntOr(CompoundTag tagCompound, String key, int defaultVal) {
        return (tagCompound.contains(key) ? tagCompound.getInt(key) : defaultVal);
    }

    private static boolean getBoolOr(CompoundTag tagCompound, String key, boolean defaultVal) {
        return (tagCompound.contains(key) ? tagCompound.getBoolean(key) : defaultVal);
    }
}
