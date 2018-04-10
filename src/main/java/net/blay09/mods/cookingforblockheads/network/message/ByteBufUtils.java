package net.blay09.mods.cookingforblockheads.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;

public class ByteBufUtils {

    public final static ItemStack readItemStack(final ByteBuf from) {
        return net.minecraftforge.fml.common.network.ByteBufUtils.readItemStack(from);
    }

    public final static void writeItemStack(final ByteBuf to, final ItemStack stack) {
        // Function net.minecraft.network.PacketBuffer.writeItemStack()
        // encodes "stackSize" on a single byte. This creates encoding
        // and decoding issue when value (modulo 256) is greater than 127
        final int count = stack.getCount();
        if (count < 128) {
            net.minecraftforge.fml.common.network.ByteBufUtils.writeItemStack(to, stack);
        } else {
            stack.setCount(127);
            net.minecraftforge.fml.common.network.ByteBufUtils.writeItemStack(to, stack);
            stack.setCount(count);
        }
    }

}
