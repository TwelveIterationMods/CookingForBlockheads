package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.NeoForgeCookingForBlockheads;
import net.blay09.mods.cookingforblockheads.api.capability.*;
import net.blay09.mods.cookingforblockheads.compat.json.JsonCompatLoader;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.event.AttachCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = CookingForBlockheads.MOD_ID)
public class CompatCapabilityLoader {

    private static KitchenConnectorCapabilityProvider connectorCapabilityProvider;
    private static ResourceLocation itemProviderResourceKey;
    private static ResourceLocation connectorResourceKey;

    @SubscribeEvent
    public static void attachTileEntityCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        BlockEntity blockEntity = event.getObject();

        ResourceLocation blockEntityRegistryName = BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(blockEntity.getType());
        if (JsonCompatLoader.kitchenItemProviders.contains(blockEntityRegistryName)) {
            if (itemProviderResourceKey == null) {
                itemProviderResourceKey = new ResourceLocation(CookingForBlockheads.MOD_ID, "kitchen_item_provider");
            }

            event.addCapability(itemProviderResourceKey, new KitchenItemCapabilityProvider(blockEntity));
        }

        if (JsonCompatLoader.kitchenConnectors.contains(blockEntityRegistryName)) {
            if (connectorResourceKey == null) {
                connectorResourceKey = new ResourceLocation(CookingForBlockheads.MOD_ID, "kitchen_connector");
            }

            if (connectorCapabilityProvider == null) {
                connectorCapabilityProvider = new KitchenConnectorCapabilityProvider();
            }

            event.addCapability(connectorResourceKey, connectorCapabilityProvider);
        }
    }

    private static final class KitchenConnectorCapabilityProvider implements ICapabilityProvider {

        private final LazyOptional<IKitchenConnector> kitchenConnectorCap = LazyOptional.of(DefaultKitchenConnector::new);

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
            return NeoForgeCookingForBlockheads.KITCHEN_CONNECTOR_CAPABILITY.orEmpty(capability, kitchenConnectorCap);
        }
    }

    private final static ItemStackHandler emptyItemHandler = new ItemStackHandler(0);

    private final static class KitchenItemCapabilityProvider implements ICapabilityProvider {

        private final LazyOptional<IKitchenItemProvider> itemProviderCap;

        public KitchenItemCapabilityProvider(final BlockEntity entity) {
            itemProviderCap = LazyOptional.of(() -> {
                LazyOptional<IItemHandler> itemHandlerCap = entity.getCapability(Capabilities.ITEM_HANDLER, null);
                return new ItemHandlerKitchenItemProvider(itemHandlerCap.orElse(emptyItemHandler));
            });
        }

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
            return NeoForgeCookingForBlockheads.KITCHEN_ITEM_PROVIDER_CAPABILITY.orEmpty(capability, itemProviderCap);
        }
    }

}
