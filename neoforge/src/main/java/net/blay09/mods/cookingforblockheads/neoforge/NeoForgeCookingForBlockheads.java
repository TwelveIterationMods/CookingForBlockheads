package net.blay09.mods.cookingforblockheads.neoforge;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.neoforge.platform.runtime.NeoForgeLoadContext;
import net.blay09.mods.balm.world.BalmContainerProvider;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.api.event.OvenItemSmeltedEvent;
import net.blay09.mods.cookingforblockheads.capability.ModCapabilities;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.tag.ModBlockTags;
import net.minecraft.world.Container;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

@Mod(CookingForBlockheads.MOD_ID)
public class NeoForgeCookingForBlockheads {

    public NeoForgeCookingForBlockheads(ModContainer modContainer, IEventBus eventBus) {
        OvenItemSmeltedEvent.EVENT.register(event
                -> NeoForge.EVENT_BUS.post(new PlayerEvent.ItemSmeltedEvent(event.player(), event.resultItem(), 1)));

        final var context = new NeoForgeLoadContext(modContainer, eventBus);
        Balm.initializeMod(CookingForBlockheads.MOD_ID, context, CookingForBlockheads::initialize);

        Balm.initializeIfLoaded(Compat.SPICE_OF_LIFE, "net.blay09.mods.cookingforblockheads.neoforge.compat.SpiceOfLifeAddon");

        Balm.capabilities()
                .registerFallbackBlockEntityProvider(id("kitchen_item_providers_tag"), ModCapabilities.KITCHEN_ITEM_PROVIDER, (blockEntity, direction) -> {
                    if (blockEntity.getBlockState().is(ModBlockTags.KITCHEN_ITEM_PROVIDERS)) {
                        final var level = blockEntity.getLevel();
                        if (blockEntity instanceof Container container) {
                            return new ContainerKitchenItemProvider(container);
                        } else if (blockEntity instanceof BalmContainerProvider containerProvider) {
                            return new ContainerKitchenItemProvider(containerProvider.getContainer());
                        } else if (level != null) {
                            final var itemHandler = level.getCapability(Capabilities.Item.BLOCK, blockEntity.getBlockPos(), null);
                            if (itemHandler != null) {
                                return new ResourceHandlerKitchenItemProvider(itemHandler);
                            }
                        }
                    }
                    return null;
                });
    }

}
