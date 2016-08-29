package net.blay09.mods.cookingforblockheads.registry;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.cookingforblockheads.registry.recipe.FoodIngredient;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.List;

public class FoodRecipeWithIngredients {
    private final ItemStack outputItem;
    private final RecipeType recipeType;
    private final int recipeWidth;
    private final List<FoodIngredient> craftMatrix;

    public FoodRecipeWithIngredients(ItemStack outputItem, RecipeType recipeType, int recipeWidth, List<FoodIngredient> craftMatrix) {
        this.outputItem = outputItem;
        this.recipeType = recipeType;
        this.recipeWidth = recipeWidth;
        this.craftMatrix = craftMatrix;
    }

    public static FoodRecipeWithIngredients read(ByteBuf buf) {
        ItemStack outputItem = ByteBufUtils.readItemStack(buf);
        RecipeStatus status = RecipeStatus.fromId(buf.readByte());
        int recipeWidth = buf.readByte();
        List<FoodIngredient> craftMatrix;
        int ingredientCount = buf.readByte();
        craftMatrix = Lists.newArrayListWithCapacity(ingredientCount);
        for (int i = 0; i < ingredientCount; i++) {
            int stackCount = buf.readShort();
            if(stackCount > 0) {
                ItemStack[] itemStacks = new ItemStack[stackCount];
                for (int j = 0; j < stackCount; j++) {
                    itemStacks[j] = ByteBufUtils.readItemStack(buf);
                }
                boolean isToolItem = buf.readBoolean();
                craftMatrix.add(new FoodIngredient(itemStacks, isToolItem));
            } else {
                craftMatrix.add(null);
            }
        }
        RecipeType recipeType = RecipeType.fromId(buf.readByte());
        return new FoodRecipeWithIngredients(outputItem, recipeType, recipeWidth, craftMatrix);
    }

    public void write(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, outputItem);
        buf.writeByte(recipeWidth);
        buf.writeByte(craftMatrix.size());
        for(FoodIngredient ingredient : craftMatrix) {
            if(ingredient != null) {
                buf.writeShort(ingredient.getItemStacks().length);
                for (ItemStack ingredientStack : ingredient.getItemStacks()) {
                    ByteBufUtils.writeItemStack(buf, ingredientStack);
                }
                buf.writeBoolean(ingredient.isToolItem());
            } else {
                buf.writeShort(0);
            }
        }
        buf.writeByte(recipeType.ordinal());
    }

    public RecipeType getRecipeType() {
        return recipeType;
    }

    public int getRecipeWidth() {
        return recipeWidth;
    }

    public List<FoodIngredient> getCraftMatrix() {
        return craftMatrix;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

}
