package net.blay09.mods.cookingbook.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.cookingbook.food.FoodRecipe;
import net.blay09.mods.cookingbook.food.recipe.RemoteCraftingFood;

public class MessageRecipeInfo implements IMessage {

    public int slotIndex;
    public FoodRecipe recipe;
    public boolean isMissingTools;
    public boolean hasVariants;
    public boolean canSmelt;

    public MessageRecipeInfo() {}

    public MessageRecipeInfo(int slotIndex, FoodRecipe recipe, boolean isMissingTools, boolean hasVariants, boolean canSmelt) {
        this.slotIndex = slotIndex;
        this.recipe = recipe;
        this.isMissingTools = isMissingTools;
        this.hasVariants = hasVariants;
        this.canSmelt = canSmelt;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        slotIndex = buf.readShort();
        if(slotIndex != -1) {
            recipe = RemoteCraftingFood.read(buf);
        }
        isMissingTools = buf.readBoolean();
        hasVariants = buf.readBoolean();
        canSmelt = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (recipe == null) {
            buf.writeShort(-1);
        } else {
            buf.writeShort(slotIndex);
            RemoteCraftingFood.write(buf, recipe);
        }
        buf.writeBoolean(isMissingTools);
        buf.writeBoolean(hasVariants);
        buf.writeBoolean(canSmelt);
    }
}
