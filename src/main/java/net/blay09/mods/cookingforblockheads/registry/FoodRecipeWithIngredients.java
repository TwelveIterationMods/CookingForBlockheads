package net.blay09.mods.cookingforblockheads.registry;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.cookingforblockheads.network.message.ByteBufUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;

public class FoodRecipeWithIngredients {
    private final ItemStack outputItem;
    private final RecipeType recipeType;
    private final int recipeWidth;
    private final List<NonNullList<ItemStack>> craftMatrix;

    public FoodRecipeWithIngredients(ItemStack outputItem, RecipeType recipeType, int recipeWidth, List<NonNullList<ItemStack>> craftMatrix) {
        this.outputItem = outputItem;
        this.recipeType = recipeType;
        this.recipeWidth = recipeWidth;
        this.craftMatrix = craftMatrix;
    }

    public static FoodRecipeWithIngredients read(ByteBuf buf) {
        ItemStack outputItem = ByteBufUtils.readItemStack(buf);
        int recipeWidth = buf.readByte();
        int ingredientCount = buf.readByte();
        List<NonNullList<ItemStack>> craftMatrix = Lists.newArrayListWithCapacity(ingredientCount);
        for (int i = 0; i < ingredientCount; i++) {
            int stackCount = buf.readByte();
            if(stackCount > 0) {
                NonNullList<ItemStack> stackList = NonNullList.create();
                for(int j = 0; j < stackCount; j++) {
                    stackList.add(ByteBufUtils.readItemStack(buf));
                }
                craftMatrix.add(stackList);
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
        for(List<ItemStack> stackList : craftMatrix) {
            buf.writeByte(stackList.size());
            for(ItemStack stack : stackList) {
                ByteBufUtils.writeItemStack(buf, stack);
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

    public List<NonNullList<ItemStack>> getCraftMatrix() {
        return craftMatrix;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

}
