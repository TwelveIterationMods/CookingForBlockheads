package net.blay09.mods.cookingforblockheads.fabric;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.api.EmptyLoadContext;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.api.KitchenItemProvider;
import net.blay09.mods.cookingforblockheads.kitchen.ContainerKitchenItemProvider;
import net.blay09.mods.cookingforblockheads.tag.ModBlockTags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;

public class FabricCookingForBlockheads implements ModInitializer {
    @Override
    public void onInitialize() {
        Balm.initializeMod(CookingForBlockheads.MOD_ID, EmptyLoadContext.INSTANCE, CookingForBlockheads::initialize);

        var itemProviderLookup = BlockApiLookup.get(Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "kitchen_item_provider"),
                KitchenItemProvider.class,
                Void.class);
        itemProviderLookup.registerFallback((level, pos, state, blockEntity, context) -> {
            if (state.is(ModBlockTags.KITCHEN_ITEM_PROVIDERS)) {
                if (blockEntity instanceof Container container) {
                    return new ContainerKitchenItemProvider(container);
                } else if (blockEntity instanceof BalmContainerProvider containerProvider) {
                    return new ContainerKitchenItemProvider(containerProvider.getContainer());
                }
            }
            return null;
        });
    }

}
