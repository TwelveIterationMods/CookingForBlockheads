package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.forge.ForgeLoadContext;
import net.blay09.mods.balm.forge.capability.ForgeBalmCapabilities;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProcessor;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.api.event.OvenItemSmeltedEvent;
import net.blay09.mods.cookingforblockheads.capability.ModCapabilities;
import net.blay09.mods.cookingforblockheads.client.CookingForBlockheadsClient;
import net.blay09.mods.cookingforblockheads.compat.Compat;
import net.blay09.mods.cookingforblockheads.compat.TheOneProbeAddon;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.tag.ModBlockTags;
import net.minecraft.world.Container;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

@Mod(CookingForBlockheads.MOD_ID)
public class ForgeCookingForBlockheads {

    public ForgeCookingForBlockheads(FMLJavaModLoadingContext context) {
        final var loadContext = new ForgeLoadContext(context.getModEventBus());
        Balm.getEvents().onEvent(OvenItemSmeltedEvent.class, orig -> {
            PlayerEvent.ItemSmeltedEvent event = new PlayerEvent.ItemSmeltedEvent(orig.getPlayer(), orig.getResultItem());
            MinecraftForge.EVENT_BUS.post(event);
        });

        final var forgeCapabilities = (ForgeBalmCapabilities) Balm.getCapabilities();
        forgeCapabilities.preRegisterType(id("kitchen_item_provider"), CapabilityManager.get(new CapabilityToken<KitchenItemProvider>() {
        }));
        forgeCapabilities.preRegisterType(id("kitchen_item_processor"), CapabilityManager.get(new CapabilityToken<KitchenItemProcessor>() {
        }));

        Balm.initializeMod(CookingForBlockheads.MOD_ID, loadContext, CookingForBlockheads::initialize);
        if (FMLEnvironment.dist.isClient()) {
            BalmClient.initializeMod(CookingForBlockheads.MOD_ID, loadContext, CookingForBlockheadsClient::initialize);
        }

        context.getModEventBus().addListener(this::enqueueIMC);

        Balm.getCapabilities()
                .registerFallbackBlockEntityProvider(id("kitchen_item_providers_tag"), ModCapabilities.KITCHEN_ITEM_PROVIDER, (blockEntity, direction) -> {
                    if (blockEntity.getBlockState().is(ModBlockTags.KITCHEN_ITEM_PROVIDERS)) {
                        final var level = blockEntity.getLevel();
                        if (blockEntity instanceof Container container) {
                            return new ContainerKitchenItemProvider(container);
                        } else if (blockEntity instanceof BalmContainerProvider containerProvider) {
                            return new ContainerKitchenItemProvider(containerProvider.getContainer());
                        } else if (level != null) {
                            final var itemHandler = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).resolve().orElse(null);
                            if (itemHandler != null) {
                                return new ItemHandlerKitchenItemProvider(itemHandler);
                            }
                        }
                    }
                    return null;
                });
    }

    private void enqueueIMC(InterModEnqueueEvent event) {
        if (Balm.isModLoaded(Compat.THEONEPROBE)) {
            TheOneProbeAddon.register();
        }
    }

}
