package net.blay09.mods.cookingforblockheads.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class ToasterRecipe implements Recipe<SingleRecipeInput> {

    private final Ingredient ingredient;
    private final ItemStackTemplate resultItem;

    public ToasterRecipe(Ingredient ingredient, ItemStackTemplate resultItem) {
        this.ingredient = ingredient;
        this.resultItem = resultItem;
    }

    @Override
    public boolean matches(SingleRecipeInput recipeInput, Level level) {
        return ingredient.test(recipeInput.item());
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
    public RecipeSerializer<ToasterRecipe> getSerializer() {
        return ModRecipes.toasterRecipes.serializer();
    }

    @Override
    public RecipeType<ToasterRecipe> getType() {
        return ModRecipes.toasterRecipes.type();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(ingredient);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return ModRecipes.toasterRecipes.bookCategory();
    }

    private static final MapCodec<ItemStackTemplate> RESULT_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("item")
                    .orElse(BuiltInRegistries.ITEM.wrapAsHolder(Items.AIR))
                    .forGetter(ItemStackTemplate::typeHolder),
            ExtraCodecs.POSITIVE_INT.fieldOf("count").orElse(1).forGetter(ItemStackTemplate::count),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(ItemStackTemplate::components)
    ).apply(instance, ItemStackTemplate::new));

    private static final MapCodec<ToasterRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
            RESULT_CODEC.fieldOf("result").forGetter(recipe -> recipe.resultItem)
    ).apply(instance, ToasterRecipe::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, ToasterRecipe> STREAM_CODEC = StreamCodec.of(
            ToasterRecipe::toNetwork,
            ToasterRecipe::fromNetwork
    );

    public static ToasterRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
        final var ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
        final var resultItem = ItemStackTemplate.STREAM_CODEC.decode(buf);
        return new ToasterRecipe(ingredient, resultItem);
    }

    public static void toNetwork(RegistryFriendlyByteBuf buf, ToasterRecipe recipe) {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.ingredient);
        ItemStackTemplate.STREAM_CODEC.encode(buf, recipe.resultItem);
    }

    public static RecipeSerializer<ToasterRecipe> serializer() {
        return new RecipeSerializer<>(CODEC, STREAM_CODEC);
    }
}
