package net.blay09.mods.cookingforblockheads.network.message;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.cookingforblockheads.registry.RecipeType;
import net.blay09.mods.cookingforblockheads.registry.recipe.FoodIngredient;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.List;

public class MessageCraftRecipe implements IMessage {

    private RecipeType recipeType;
    private List<ItemStack> craftMatrix;
    private boolean stack;

    public MessageCraftRecipe() {}

    public MessageCraftRecipe(RecipeType recipeType, List<ItemStack> craftMatrix, boolean stack) {
        this.recipeType = recipeType;
        this.craftMatrix = craftMatrix;
        this.stack = stack;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        recipeType = RecipeType.fromId(buf.readByte());
        int ingredientCount = buf.readByte();
        craftMatrix = Lists.newArrayListWithCapacity(ingredientCount);
        for(int i = 0; i < ingredientCount; i++) {
            craftMatrix.add(ByteBufUtils.readItemStack(buf));
        }
        stack = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(recipeType.ordinal());
        buf.writeByte(craftMatrix.size());
        for(ItemStack itemstack : craftMatrix) {
            ByteBufUtils.writeItemStack(buf, itemstack);
        }
        buf.writeBoolean(stack);
    }

    public List<ItemStack> getCraftMatrix() {
        return craftMatrix;
    }

    public boolean isStack() {
        return stack;
    }

    public RecipeType getRecipeType() {
        return recipeType;
    }
}
