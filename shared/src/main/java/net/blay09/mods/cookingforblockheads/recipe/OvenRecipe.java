package net.blay09.mods.cookingforblockheads.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
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

public class OvenRecipe implements Recipe<Container> {

    private final Ingredient ingredient;
    private final ItemStack resultItem;

    public OvenRecipe(Ingredient ingredient, ItemStack resultItem) {
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
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return resultItem.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return resultItem;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ovenRecipeSerializer;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.ovenRecipeType;
    }

    static class Serializer implements RecipeSerializer<OvenRecipe> {

        private static final Codec<ItemStack> RESULT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("item")
                        .orElse(BuiltInRegistries.ITEM.wrapAsHolder(Items.AIR))
                        .forGetter(ItemStack::getItemHolder),
                ExtraCodecs.strictOptionalField(ExtraCodecs.POSITIVE_INT, "count", 1).forGetter(ItemStack::getCount),
                CompoundTag.CODEC.optionalFieldOf("tag").forGetter((itemStack) -> Optional.ofNullable(itemStack.getTag()))
        ).apply(instance, ItemStack::new));

        private static final Codec<OvenRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                RESULT_CODEC.fieldOf("result").forGetter(recipe -> recipe.resultItem)
        ).apply(instance, OvenRecipe::new));

        @Override
        public Codec<OvenRecipe> codec() {
            return CODEC;
        }

        @Override
        public OvenRecipe fromNetwork(FriendlyByteBuf buf) {
            final var ingredient = Ingredient.fromNetwork(buf);
            final var resultItem = buf.readItem();
            return new OvenRecipe(ingredient, resultItem);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, OvenRecipe recipe) {
            recipe.ingredient.toNetwork(buf);
            buf.writeItem(recipe.resultItem);
        }
    }

}
