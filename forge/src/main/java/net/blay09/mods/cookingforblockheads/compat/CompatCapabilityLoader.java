package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.ForgeCookingForBlockheads;
import net.blay09.mods.cookingforblockheads.api.capability.DefaultKitchenConnector;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenConnector;
import net.blay09.mods.cookingforblockheads.api.capability.IKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.compat.json.JsonCompatLoader;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = CookingForBlockheads.MOD_ID)
public class CompatCapabilityLoader {

    private static KitchenConnectorCapabilityProvider connectorCapabilityProvider;
    private static ResourceLocation itemProviderResourceKey;
    private static ResourceLocation connectorResourceKey;

    @SubscribeEvent
    public static void attachTileEntityCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        BlockEntity blockEntity = event.getObject();

        ResourceLocation blockEntityRegistryName = ForgeRegistries.BLOCK_ENTITIES.getKey(blockEntity.getType());
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
            return ForgeCookingForBlockheads.KITCHEN_CONNECTOR_CAPABILITY.orEmpty(capability, kitchenConnectorCap);
        }
    }

    private final static ItemStackHandler emptyItemHandler = new ItemStackHandler(0);

    private final static class KitchenItemCapabilityProvider implements ICapabilityProvider {

        private final LazyOptional<IKitchenItemProvider> itemProviderCap;

        public KitchenItemCapabilityProvider(final BlockEntity entity) {
            itemProviderCap = LazyOptional.of(() -> {
                LazyOptional<IItemHandler> itemHandlerCap = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                return new ItemHandlerKitchenItemProvider(itemHandlerCap.orElse(emptyItemHandler));
            });
        }

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
            return ForgeCookingForBlockheads.KITCHEN_ITEM_PROVIDER_CAPABILITY.orEmpty(capability, itemProviderCap);
        }
    }

}
