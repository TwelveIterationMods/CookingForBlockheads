package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.tag.ModBlockTags;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = CookingForBlockheads.MOD_ID)
public class CapabilityBinder {

    private static final ResourceLocation itemProviderResourceKey = ResourceLocation.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "kitchen_item_provider");

    @SubscribeEvent
    public static void attachTileEntityCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        BlockEntity blockEntity = event.getObject();

        if (blockEntity.getBlockState().is(ModBlockTags.KITCHEN_ITEM_PROVIDERS)) {
            event.addCapability(itemProviderResourceKey, new KitchenItemCapabilityProvider(blockEntity));
        }
    }

    private final static ItemStackHandler emptyItemHandler = new ItemStackHandler(0);

    private final static class KitchenItemCapabilityProvider implements ICapabilityProvider {

        private final LazyOptional<KitchenItemProvider> itemProviderCap;

        public KitchenItemCapabilityProvider(final BlockEntity entity) {
            itemProviderCap = LazyOptional.of(() -> {
                LazyOptional<IItemHandler> itemHandlerCap = entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null);
                return new ItemHandlerKitchenItemProvider(itemHandlerCap.orElse(emptyItemHandler));
            });
        }

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
            return ForgeCookingForBlockheads.KITCHEN_ITEM_PROVIDER_CAPABILITY.orEmpty(capability, itemProviderCap);
        }
    }

}
