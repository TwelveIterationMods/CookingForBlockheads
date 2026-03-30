package net.blay09.mods.cookingforblockheads.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;

import java.util.List;

public record KitchenProvidedRecipe(
        Identifier source,
        ItemStackTemplate resultItem
) implements Recipe<SingleRecipeInput> {

    @Override
    public boolean matches(SingleRecipeInput recipeInput, Level level) {
        return true;
    }

    @Override
    public ItemStack assemble(SingleRecipeInput recipeInput) {
        return resultItem.create();
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeSerializer<KitchenProvidedRecipe> getSerializer() {
        return ModRecipes.kitchenRecipes.serializer();
    }

    @Override
    public RecipeType<KitchenProvidedRecipe> getType() {
        return ModRecipes.kitchenRecipes.type();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(Ingredient.of(resultItem.item().value()));
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(new ShapelessCraftingRecipeDisplay(
                List.of(new SlotDisplay.ItemStackSlotDisplay(resultItem)),
                new SlotDisplay.ItemStackSlotDisplay(resultItem),
                SlotDisplay.Empty.INSTANCE
        ));
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return ModRecipes.kitchenRecipes.bookCategory();
    }

    private static final MapCodec<KitchenProvidedRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("source").forGetter(KitchenProvidedRecipe::source),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(KitchenProvidedRecipe::resultItem)
    ).apply(instance, KitchenProvidedRecipe::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, KitchenProvidedRecipe> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC,
            KitchenProvidedRecipe::source,
            ItemStackTemplate.STREAM_CODEC,
            KitchenProvidedRecipe::resultItem,
            KitchenProvidedRecipe::new
    );

    public static RecipeSerializer<KitchenProvidedRecipe> serializer() {
        return new RecipeSerializer<>(CODEC, STREAM_CODEC);
    }
}
