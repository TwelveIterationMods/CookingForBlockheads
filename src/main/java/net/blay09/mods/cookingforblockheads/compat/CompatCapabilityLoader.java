package net.blay09.mods.cookingforblockheads.compat;

import java.util.ArrayList;
import java.util.List;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenConnector;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.KitchenItemProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CompatCapabilityLoader {
    private final static List<Class<? extends TileEntity>> dynKitchenItemProviderEntities = new ArrayList<Class<? extends TileEntity>>();
    private final static List<Class<? extends TileEntity>> kitchenItemProviderEntities = new ArrayList<Class<? extends TileEntity>>();
    private final static List<Class<? extends TileEntity>> kitchenConnectorEntities = new ArrayList<Class<? extends TileEntity>>();
    private final static ItemStackHandler emptyItemHandler = new ItemStackHandler(0);
    private final static CompatCapabilityLoader loader = new CompatCapabilityLoader();
    private static boolean registered = false;

    public static void register() {
        if (registered)
            return;
        MinecraftForge.EVENT_BUS.register(loader);
        registered = true;
    }

    @SuppressWarnings("unchecked")
    private static void addTileEntityClass(final String className, final List<Class<? extends TileEntity>> list) {
        try {
            Class<?> c = Class.forName(className);
            if (TileEntity.class.isAssignableFrom(c)) {
                list.add((Class<? extends TileEntity>) c);
                register();
            } else
                CookingForBlockheads.logger.warn("Incompatible TileEntity class: {}", className);
        } catch (ClassNotFoundException e) {
            CookingForBlockheads.logger.warn("TileEntity class not found: {}", className);
        }
    }

    public static void addDynamicKitchenItemProviderClass(String className) {
        addTileEntityClass(className, dynKitchenItemProviderEntities);
    }

    public static void addKitchenItemProviderClass(String className) {
        addTileEntityClass(className, kitchenItemProviderEntities);
    }

    public static void addKitchenConnectorClass(String className) {
        addTileEntityClass(className, kitchenConnectorEntities);
    }

    private final static ResourceLocation capabilityKitchenItemProvider = new ResourceLocation(CookingForBlockheads.MOD_ID, CapabilityKitchenItemProvider.CAPABILITY.getName());
    private final static ResourceLocation capabilityKitchenConnector = new ResourceLocation(CookingForBlockheads.MOD_ID, CapabilityKitchenItemProvider.CAPABILITY.getName());
    @SubscribeEvent
    public void tileEntity(AttachCapabilitiesEvent<TileEntity> event) {
        final TileEntity entity = event.getObject();
        for (final Class<? extends TileEntity> c: kitchenItemProviderEntities) {
            if (! c.isInstance(entity))
                continue;
            event.addCapability(capabilityKitchenItemProvider, crateItemCapabilityProvider(entity, false));
            return;
        }
        for (final Class<? extends TileEntity> c: dynKitchenItemProviderEntities) {
            if (! c.isInstance(entity))
                continue;
            event.addCapability(capabilityKitchenItemProvider, crateItemCapabilityProvider(entity, true));
            return;
        }
        for (final Class<? extends TileEntity> c: kitchenConnectorEntities) {
            if (! c.isInstance(entity))
                continue;
            event.addCapability(capabilityKitchenConnector,
                    new ICapabilityProvider() {
                        @Override
                        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
                            return CapabilityKitchenConnector.CAPABILITY == capability;
                        }
                        @Override
                        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
                            return null;
                        }
                    });
            return;
        }
    }

    private ICapabilityProvider crateItemCapabilityProvider(final TileEntity entity, final boolean dynamic) {
        return new ICapabilityProvider() {
            private KitchenItemProvider kitchenProvider = null;
            private IItemHandler itemHandler = null;

            @Override
            public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
                return CapabilityKitchenItemProvider.CAPABILITY == capability;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
                if (CapabilityKitchenItemProvider.CAPABILITY != capability)
                    return null;
                if (itemHandler != null)
                    return (T) kitchenProvider;

                if (kitchenProvider == null)
                    kitchenProvider = dynamic ? new DynamicKitchenItemProvider(emptyItemHandler) : new KitchenItemProvider(emptyItemHandler);

                final IItemHandler handler;
                if (entity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
                    handler = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                else if (entity instanceof IItemHandler)
                    handler = (IItemHandler) entity;
                else
                    return (T) kitchenProvider;

                itemHandler = handler;
                kitchenProvider.setItemHandler(handler);
                return (T) kitchenProvider;
            }
        };
    }

    public static final class DynamicKitchenItemProvider extends KitchenItemProvider {
        private IItemHandler itemHandler;
        private int stackSize = 0;
        private boolean checked = false;

        public DynamicKitchenItemProvider(IItemHandler itemHandler) {
            setItemHandler(itemHandler);
        }

        @Override
        public void setItemHandler(IItemHandler itemHandler) {
            final int newSize = itemHandler.getSlots();
            if (itemHandler != this.itemHandler) {
                this.itemHandler = itemHandler;
                stackSize = newSize;
                super.setItemHandler(itemHandler);
            } else if (newSize > stackSize) {
                stackSize = newSize;
                super.setItemHandler(itemHandler);
            }
        }

        @Override
        public void resetSimulation() {
            checked = false;
            super.resetSimulation();
        }

        @Override
        public ItemStack useItemStack(int slot, int amount, boolean simulate, List<IKitchenItemProvider> inventories, boolean requireBucket) {
            if (simulate && (! checked)) {
                if (stackSize < itemHandler.getSlots()) {
                    setItemHandler(itemHandler);
                }
                checked = true;
            }
            return super.useItemStack(slot, amount, simulate, inventories, requireBucket);
        }
    }

}
