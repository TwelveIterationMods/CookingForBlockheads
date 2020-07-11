package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenConnector;
import net.blay09.mods.cookingforblockheads.api.capability.CapabilityKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.capability.KitchenItemProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = CookingForBlockheads.MOD_ID)
public class CompatCapabilityLoader {

    private static KitchenConnectorCapabilityProvider connectorCapabilityProvider;
    private static ResourceLocation itemProviderResourceKey;
    private static ResourceLocation connectorResourceKey;

    private static final Set<ResourceLocation> kitchenItemProviders = new HashSet<>();
    private static final Set<ResourceLocation> kitchenConnectors = new HashSet<>();

    public static void addKitchenItemProvider(ResourceLocation registryName) {
        kitchenItemProviders.add(registryName);
    }

    public static void addKitchenConnector(ResourceLocation registryName) {
        kitchenConnectors.add(registryName);
    }

    @SubscribeEvent
    public static void attachTileEntityCapabilities(AttachCapabilitiesEvent<TileEntity> event) {
        TileEntity tileEntity = event.getObject();

        if (tileEntity.getType() != null) {
            if (kitchenItemProviders.contains(tileEntity.getType().getRegistryName())) {
                if (itemProviderResourceKey == null) {
                    itemProviderResourceKey = new ResourceLocation(CookingForBlockheads.MOD_ID, "kitchen_item_provider");
                }

                event.addCapability(itemProviderResourceKey, new KitchenItemCapabilityProvider(tileEntity));
            }

            if (kitchenConnectors.contains(tileEntity.getType().getRegistryName())) {
                if (connectorResourceKey == null) {
                    connectorResourceKey = new ResourceLocation(CookingForBlockheads.MOD_ID, "kitchen_connector");
                }

                if (connectorCapabilityProvider == null) {
                    connectorCapabilityProvider = new KitchenConnectorCapabilityProvider();
                }

                event.addCapability(connectorResourceKey, connectorCapabilityProvider);
            }
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
