package net.blay09.mods.cookingforblockheads.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageRequestRecipes implements IMessage {

    private ItemStack outputItem;
    private boolean forceNoFilter;

    public MessageRequestRecipes() {
    }

    public MessageRequestRecipes(ItemStack outputItem, boolean forceNoFilter) {
        this.outputItem = outputItem;
        this.forceNoFilter = forceNoFilter;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        outputItem = ByteBufUtils.readItemStack(buf);
        forceNoFilter = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, outputItem);
        buf.writeBoolean(forceNoFilter);
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public boolean isForceNoFilter() {
        return forceNoFilter;
    }
}