package net.blay09.mods.cookingforblockheads.network.message;

import io.netty.buffer.ByteBuf;
import net.blay09.mods.cookingforblockheads.registry.RecipeType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageCraftRecipe implements IMessage {

    private ItemStack outputItem;
    private RecipeType recipeType;
    private NonNullList<ItemStack> craftMatrix;
    private boolean stack;

    public MessageCraftRecipe() {}

    public MessageCraftRecipe(ItemStack outputItem, RecipeType recipeType, NonNullList<ItemStack> craftMatrix, boolean stack) {
        this.outputItem = outputItem;
        this.recipeType = recipeType;
        this.craftMatrix = craftMatrix;
        this.stack = stack;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        outputItem = ByteBufUtils.readItemStack(buf);
        recipeType = RecipeType.fromId(buf.readByte());
        int ingredientCount = buf.readByte();
        craftMatrix = NonNullList.create();
        for(int i = 0; i < ingredientCount; i++) {
            craftMatrix.add(ByteBufUtils.readItemStack(buf));
        }
        stack = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, outputItem);
        buf.writeByte(recipeType.ordinal());
        buf.writeByte(craftMatrix.size());
        for(ItemStack itemstack : craftMatrix) {
            ByteBufUtils.writeItemStack(buf, itemstack);
        }
        buf.writeBoolean(stack);
    }

    public NonNullList<ItemStack> getCraftMatrix() {
        return craftMatrix;
    }

    public boolean isStack() {
        return stack;
    }

    public RecipeType getRecipeType() {
        return recipeType;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }
}
