package net.blay09.mods.cookingforblockheads.api.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nonnull;

public class OvenItemSmeltedEvent extends PlayerEvent.ItemSmeltedEvent {

    private final World world;
    private final BlockPos pos;

    public OvenItemSmeltedEvent(PlayerEntity player, World world, BlockPos pos, ItemStack resultItem) {
        super(player, resultItem);
        this.world = world;
        this.pos = pos;
    }

    public World getWorld() {
        return world;
    }

    public BlockPos getPos() {
        return pos;
    }
}
