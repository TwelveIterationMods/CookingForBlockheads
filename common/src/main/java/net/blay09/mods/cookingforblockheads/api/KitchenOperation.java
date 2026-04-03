package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public interface KitchenOperation {
    KitchenOperation EMPTY = new KitchenOperation() {
    };

    default Optional<Component> getFeedback() {
        return Optional.empty();
    }

    default ItemStack getImmediateResult() {
        return ItemStack.EMPTY;
    }
}
