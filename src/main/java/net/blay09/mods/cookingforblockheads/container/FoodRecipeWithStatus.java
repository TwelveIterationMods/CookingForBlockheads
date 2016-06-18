package net.blay09.mods.cookingforblockheads.container;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.cookingforblockheads.registry.RecipeStatus;
import net.blay09.mods.cookingforblockheads.registry.RecipeType;
import net.blay09.mods.cookingforblockheads.registry.recipe.FoodIngredient;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.List;

public class FoodRecipeWithStatus {

    private final int id;
    private final int recipeWidth;
    private final List<FoodIngredient> craftMatrix;
    private final ItemStack outputItem;
    private final RecipeType type;
    private final RecipeStatus status;

    public FoodRecipeWithStatus(int id, ItemStack outputItem, int recipeWidth, List<FoodIngredient> craftMatrix, RecipeType type, RecipeStatus status) {
        this.id = id;
        this.outputItem = outputItem;
        this.recipeWidth = recipeWidth;
        this.craftMatrix = craftMatrix;
        this.type = type;
        this.status = status;
    }

    public int getId() {
        return id;
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

    public RecipeType getType() {
        return type;
    }

    public RecipeStatus getStatus() {
        return status;
    }

    public static FoodRecipeWithStatus read(ByteBuf buf) {
        int id = buf.readInt();
        ItemStack outputItem = ByteBufUtils.readItemStack(buf);
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
        RecipeType type = RecipeType.fromId(buf.readByte());
        RecipeStatus status = RecipeStatus.fromId(buf.readByte());
        return new FoodRecipeWithStatus(id, outputItem, recipeWidth, craftMatrix, type, status);
    }

    public void write(ByteBuf buf) {
        buf.writeInt(id);
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
        buf.writeByte(type.ordinal());
        buf.writeByte(status.ordinal());
    }
}
