package net.blay09.mods.cookingforblockheads.api;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class FoodRecipeWithStatus {
    private final ItemStack outputItem;
    private final RecipeStatus status;

    public FoodRecipeWithStatus(ItemStack outputItem, RecipeStatus status) {
        this.outputItem = outputItem;
        this.status = status;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public RecipeStatus getStatus() {
        return status;
    }

    public static FoodRecipeWithStatus read(ByteBuf buf) {
        ItemStack outputItem = ByteBufUtils.readItemStack(buf);
        RecipeStatus status = RecipeStatus.fromId(buf.readByte());
        return new FoodRecipeWithStatus(outputItem, status);
    }

    public void write(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, outputItem);
        buf.writeByte(status.ordinal());
    }

}
