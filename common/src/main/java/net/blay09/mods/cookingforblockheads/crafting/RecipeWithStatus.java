package net.blay09.mods.cookingforblockheads.crafting;

import net.blay09.mods.cookingforblockheads.tag.ModItemTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record RecipeWithStatus(RecipeDisplayEntry recipeDisplayEntry, List<Ingredient> missingIngredients,
                               int missingIngredientsMask, List<ItemStack> lockedInputs, List<List<IngredientAmount>> ingredientAmounts,
                               int craftableAmount, Optional<Component> error) {

    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeWithStatus> STREAM_CODEC = StreamCodec.composite(
            RecipeDisplayEntry.STREAM_CODEC,
            RecipeWithStatus::recipeDisplayEntry,
            ByteBufCodecs.collection(ArrayList::new, Ingredient.CONTENTS_STREAM_CODEC),
            RecipeWithStatus::missingIngredients,
            ByteBufCodecs.INT,
            RecipeWithStatus::missingIngredientsMask,
            ItemStack.OPTIONAL_LIST_STREAM_CODEC,
            RecipeWithStatus::lockedInputs,
            IngredientAmount.LIST_STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new)),
            RecipeWithStatus::ingredientAmounts,
            ByteBufCodecs.INT,
            RecipeWithStatus::craftableAmount,
            ComponentSerialization.STREAM_CODEC.apply(ByteBufCodecs::optional),
            RecipeWithStatus::error,
            RecipeWithStatus::new
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, List<RecipeWithStatus>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.collection(
            ArrayList::new));

    public static RecipeWithStatus best(@Nullable RecipeWithStatus first, @Nullable RecipeWithStatus second) {
        if (first == null) {
            return second;
        } else if (second == null) {
            return first;
        }

        if (first.missingIngredients.size() < second.missingIngredients.size()) {
            return first;
        } else if (second.missingIngredients.size() < first.missingIngredients.size()) {
            return second;
        }

        return first;
    }

    public boolean isMissingIngredients() {
        return !missingIngredients.isEmpty();
    }

    public boolean isMissingUtensils() {
        return missingIngredients.stream().anyMatch(RecipeWithStatus::isUtensil);
    }

    public boolean canCraft() {
        return !isMissingIngredients() && error.isEmpty();
    }

    private static boolean isUtensil(Ingredient ingredient) {
        for (final var utensilItem : BuiltInRegistries.ITEM.getTagOrEmpty(ModItemTags.UTENSILS)) {
            if (ingredient.acceptsItem(utensilItem)) {
                return true;
            }
        }

        return false;
    }
}
