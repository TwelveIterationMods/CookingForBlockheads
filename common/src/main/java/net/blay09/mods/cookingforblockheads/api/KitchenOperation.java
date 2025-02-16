package net.blay09.mods.cookingforblockheads.api;

import net.minecraft.network.chat.Component;

import java.util.Optional;

/**
 * Not currently used for anything, feel free to just use EMPTY.
 */
public interface KitchenOperation {
    KitchenOperation EMPTY = new KitchenOperation() {
    };

    default Optional<Component> getFeedback() {
        return Optional.empty();
    }
}
