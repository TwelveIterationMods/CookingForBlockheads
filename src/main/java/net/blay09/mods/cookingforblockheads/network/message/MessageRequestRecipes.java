package net.blay09.mods.cookingforblockheads.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageRequestRecipes implements IMessage {

    private ItemStack outputItem;

    public MessageRequestRecipes() {
    }

    public MessageRequestRecipes(ItemStack outputItem) {
        this.outputItem = outputItem;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        outputItem = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, outputItem);
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

}