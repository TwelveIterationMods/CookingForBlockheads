package net.blay09.mods.cookingbook.food.recipe;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.cookingbook.food.FoodRecipe;
import net.blay09.mods.cookingbook.food.FoodIngredient;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RemoteCraftingFood extends FoodRecipe {

    private final boolean isSmeltingRecipe;

    public RemoteCraftingFood(ItemStack outputItem, FoodIngredient[] craftMatrix, boolean isSmeltingRecipe) {
        this.outputItem = outputItem;
        this.craftMatrix = craftMatrix;
        this.isSmeltingRecipe = isSmeltingRecipe;
    }

    @Override
    public boolean isSmeltingRecipe() {
        return isSmeltingRecipe;
    }

    public static RemoteCraftingFood read(ByteBuf buf) {
        ItemStack outputItem = ByteBufUtils.readItemStack(buf);
        FoodIngredient[] craftMatrix = new FoodIngredient[buf.readByte()];
        for(int k = 0; k < craftMatrix.length; k++) {
            ItemStack[] itemStacks = new ItemStack[buf.readByte()];
            for(int l = 0; l < itemStacks.length; l++) {
                itemStacks[l] = ByteBufUtils.readItemStack(buf);
            }
            boolean isOptional = buf.readBoolean();
            craftMatrix[k] = new FoodIngredient(itemStacks, isOptional);
        }
        boolean isSmeltingRecipe = buf.readBoolean();
        return new RemoteCraftingFood(outputItem, craftMatrix, isSmeltingRecipe);
    }

    public static void write(ByteBuf buf, FoodRecipe recipe) {
        ByteBufUtils.writeItemStack(buf, recipe.getOutputItem());
        buf.writeByte(recipe.getCraftMatrix().length);
        for(FoodIngredient ingredient : recipe.getCraftMatrix()) {
            buf.writeByte(ingredient.getItemStacks().length);
            for(ItemStack ingredientStack : ingredient.getItemStacks()) {
                ByteBufUtils.writeItemStack(buf, ingredientStack);
            }
            buf.writeBoolean(ingredient.isToolItem());
        }
        buf.writeBoolean(recipe.isSmeltingRecipe());
    }
}
