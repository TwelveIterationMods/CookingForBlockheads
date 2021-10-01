//package net.blay09.mods.cookingforblockheads.compat; TODO
//
//import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
//import net.blay09.mods.cookingforblockheads.api.capability.*;
//import net.minecraft.core.Direction;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.HashSet;
//import java.util.Set;
//
//public class CompatCapabilityLoader {
//
//    private static KitchenConnectorCapabilityProvider connectorCapabilityProvider;
//    private static ResourceLocation itemProviderResourceKey;
//    private static ResourceLocation connectorResourceKey;
//
//    @SubscribeEvent
//    public static void attachTileEntityCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
//        BlockEntity tileEntity = event.getObject();
//
//        if (kitchenItemProviders.contains(tileEntity.getType().getRegistryName())) {
//            if (itemProviderResourceKey == null) {
//                itemProviderResourceKey = new ResourceLocation(CookingForBlockheads.MOD_ID, "kitchen_item_provider");
//            }
//
//            event.addCapability(itemProviderResourceKey, new KitchenItemCapabilityProvider(tileEntity));
//        }
//
//        if (kitchenConnectors.contains(tileEntity.getType().getRegistryName())) {
//            if (connectorResourceKey == null) {
//                connectorResourceKey = new ResourceLocation(CookingForBlockheads.MOD_ID, "kitchen_connector");
//            }
//
//            if (connectorCapabilityProvider == null) {
//                connectorCapabilityProvider = new KitchenConnectorCapabilityProvider();
//            }
//
//            event.addCapability(connectorResourceKey, connectorCapabilityProvider);
//        }
//    }
//
//    private static final class KitchenConnectorCapabilityProvider implements ICapabilityProvider {
//
//        @SuppressWarnings("NullableProblems")
//        private final LazyOptional<IKitchenConnector> kitchenConnectorCap = LazyOptional.of(CapabilityKitchenConnector.CAPABILITY::getDefaultInstance);
//
//        @Override
//        public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
//            return CapabilityKitchenConnector.CAPABILITY.orEmpty(capability, kitchenConnectorCap);
//        }
//    }
//
//    private final static ItemStackHandler emptyItemHandler = new ItemStackHandler(0);
//
//    private final static class KitchenItemCapabilityProvider implements ICapabilityProvider {
//
//        private final LazyOptional<IKitchenItemProvider> itemProviderCap;
//
//        public KitchenItemCapabilityProvider(final BlockEntity entity) {
//            itemProviderCap = LazyOptional.of(() -> {
//                LazyOptional<IItemHandler> itemHandlerCap = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
//                return new DefaultKitchenItemProvider(itemHandlerCap.orElse(emptyItemHandler));
//            });
//        }
//
//        @Override
//        public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
//            return CapabilityKitchenItemProvider.CAPABILITY.orEmpty(capability, itemProviderCap);
//        }
//    }
//
//}
