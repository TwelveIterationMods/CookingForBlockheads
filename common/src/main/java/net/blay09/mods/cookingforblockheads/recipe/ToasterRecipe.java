package net.blay09.mods.cookingforblockheads.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class ToasterRecipe implements Recipe<Container> {

    private final Ingredient ingredient;
    private final ItemStack resultItem;

    public ToasterRecipe(Ingredient ingredient, ItemStack resultItem) {
        this.ingredient = ingredient;
        this.resultItem = resultItem;
    }

    @Override
    public boolean matches(Container container, Level level) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            final var slotStack = container.getItem(i);
            if (ingredient.test(slotStack)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack assemble(Container container, HolderLookup.Provider provider) {
        return resultItem.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return resultItem;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.toasterRecipeSerializer;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.toasterRecipeType;
    }

    static class Serializer implements RecipeSerializer<ToasterRecipe> {

        private static final MapCodec<ItemStack> RESULT_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
                BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("item")
                        .orElse(BuiltInRegistries.ITEM.wrapAsHolder(Items.AIR))
                        .forGetter(ItemStack::getItemHolder),
                ExtraCodecs.POSITIVE_INT.fieldOf("count").orElse(1).forGetter(ItemStack::getCount),
                DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(ItemStack::getComponentsPatch)
        ).apply(instance, ItemStack::new));

        private static final MapCodec<ToasterRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                RESULT_CODEC.fieldOf("result").forGetter(recipe -> recipe.resultItem)
        ).apply(instance, ToasterRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, ToasterRecipe> STREAM_CODEC = StreamCodec.of(
                ToasterRecipe.Serializer::toNetwork,
                ToasterRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<ToasterRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ToasterRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        public static ToasterRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            final var ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
            final var resultItem = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
            return new ToasterRecipe(ingredient, resultItem);
        }

        public static void toNetwork(RegistryFriendlyByteBuf buf, ToasterRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.ingredient);
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, recipe.resultItem);
        }
    }

}