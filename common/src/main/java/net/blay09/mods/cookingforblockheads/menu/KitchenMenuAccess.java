package net.blay09.mods.cookingforblockheads.menu;

import net.blay09.mods.cookingforblockheads.api.Kitchen;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public interface KitchenMenuAccess {
    KitchenMenuAccess NULL = new KitchenMenuAccess() {
        public <T> Optional<T> evaluate(BiFunction<Kitchen, ServerKitchenMenuState, T> action) {
            return Optional.empty();
        }
    };

    static KitchenMenuAccess create(Kitchen kitchen, ServerKitchenMenuState state) {
        return new KitchenMenuAccess() {
            public <T> Optional<T> evaluate(BiFunction<Kitchen, ServerKitchenMenuState, @Nullable T> action) {
                return Optional.ofNullable(action.apply(kitchen, state));
            }
        };
    }

    <T> Optional<T> evaluate(BiFunction<Kitchen, ServerKitchenMenuState, T> evaluator);

    default <T> T evaluate(BiFunction<Kitchen, ServerKitchenMenuState, T> action, T defaultValue) {
        return this.evaluate(action).orElse(defaultValue);
    }

    default void execute(BiConsumer<Kitchen, ServerKitchenMenuState> action) {
        this.evaluate((kitchen, state) -> {
            action.accept(kitchen, state);
            return Optional.empty();
        });
    }
}
