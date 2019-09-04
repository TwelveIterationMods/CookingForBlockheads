package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenConnector;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.KitchenItemProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.HashMap;

public class CompatCapabilityLoader {
    private final static HashMap<Class<? extends TileEntity>, CapabilityType> tilesClasses = new HashMap<>();
    private final static CompatCapabilityLoader instance = new CompatCapabilityLoader();
    private static boolean registered = false;

    private enum CapabilityType {
        CONNECTOR, ITEM_PROVIDER
    }

    public static void register() {
        if (!registered) {
            MinecraftForge.EVENT_BUS.register(instance);
            registered = true;
        }
    }

    @SuppressWarnings("unchecked")
    private static void addTileEntityClass(final String className, final CapabilityType type) {
        try {
            Class<?> c = Class.forName(className);
            if (TileEntity.class.isAssignableFrom(c)) {
                tilesClasses.put((Class<? extends TileEntity>) c, type);
                register();
            } else {
                CookingForBlockheads.logger.warn("Incompatible TileEntity class: {}", className);
            }
        } catch (ClassNotFoundException e) {
            CookingForBlockheads.logger.warn("TileEntity class not found: {}", className);
        }
    }

    public static void addKitchenItemProviderClass(String className) {
        addTileEntityClass(className, CapabilityType.ITEM_PROVIDER);
    }

    public static void addKitchenConnectorClass(String className) {
        addTileEntityClass(className, CapabilityType.CONNECTOR);
    }

    private final static ResourceLocation itemProviderResourceKey = new ResourceLocation(CookingForBlockheads.MOD_ID,
            CapabilityKitchenItemProvider.CAPABILITY.getName());
    private final static ResourceLocation connectorResourceKey = new ResourceLocation(CookingForBlockheads.MOD_ID,
            CapabilityKitchenConnector.CAPABILITY.getName());
    private final static KitchenConnectorCapabilityProvider connectorCapabilityProvider = new KitchenConnectorCapabilityProvider();

    @SubscribeEvent
    public void tileEntity(AttachCapabilitiesEvent<TileEntity> event) {
        final TileEntity entity = event.getObject();
        final Class<? extends TileEntity> entityClass = entity.getClass();

        final CapabilityType type = tilesClasses.get(entityClass);
        if (type == null) {
            return;
        }
        switch (type) {
            case CONNECTOR:
                event.addCapability(connectorResourceKey, connectorCapabilityProvider);
                break;
            case ITEM_PROVIDER:
                event.addCapability(itemProviderResourceKey, new KitchenItemCapabilityProvider(entity));
                break;
        }
    }

    private static final class KitchenConnectorCapabilityProvider implements ICapabilityProvider {

        @SuppressWarnings("NullableProblems")
        private final LazyOptional<CapabilityKitchenConnector.IKitchenConnector> kitchenConnectorCap = LazyOptional.of(CapabilityKitchenConnector.CAPABILITY::getDefaultInstance);

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
            return CapabilityKitchenConnector.CAPABILITY.orEmpty(capability, kitchenConnectorCap);
        }
    }

    private final static ItemStackHandler emptyItemHandler = new ItemStackHandler(0);

    private final static class KitchenItemCapabilityProvider implements ICapabilityProvider {

        private final LazyOptional<IKitchenItemProvider> itemProviderCap;

        public KitchenItemCapabilityProvider(final TileEntity entity) {
            itemProviderCap = LazyOptional.of(() -> {
                LazyOptional<IItemHandler> itemHandlerCap = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                return new KitchenItemProvider(itemHandlerCap.orElse(emptyItemHandler));
            });
        }

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
            return CapabilityKitchenItemProvider.CAPABILITY.orEmpty(capability, itemProviderCap);
        }
    }

}
