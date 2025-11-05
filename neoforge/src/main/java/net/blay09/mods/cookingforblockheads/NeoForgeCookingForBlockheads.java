package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.neoforge.NeoForgeLoadContext;
import net.blay09.mods.cookingforblockheads.api.event.OvenItemSmeltedEvent;
import net.blay09.mods.cookingforblockheads.capability.ModCapabilities;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.compat.TheOneProbeAddon;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.tag.ModBlockTags;
import net.minecraft.world.Container;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

@Mod(CookingForBlockheads.MOD_ID)
public class NeoForgeCookingForBlockheads {

    public NeoForgeCookingForBlockheads(IEventBus eventBus) {
        Balm.events().onEvent(OvenItemSmeltedEvent.class, orig -> {
            PlayerEvent.ItemSmeltedEvent event = new PlayerEvent.ItemSmeltedEvent(orig.getPlayer(), orig.getResultItem(), 1);
            NeoForge.EVENT_BUS.post(event);
        });

        final var context = new NeoForgeLoadContext(eventBus);
        Balm.initializeMod(CookingForBlockheads.MOD_ID, context, CookingForBlockheads::initialize);

        eventBus.addListener(this::enqueueIMC);

        Balm.initializeIfLoaded(Compat.SPICE_OF_LIFE, "net.blay09.mods.cookingforblockheads.compat.SpiceOfLifeAddon");

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

    private void enqueueIMC(InterModEnqueueEvent event) {
        if (Balm.platform().isModLoaded(Compat.THEONEPROBE)) {
            TheOneProbeAddon.register();
        }
    }

}
