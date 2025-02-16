package net.blay09.mods.cookingforblockheads.api.event;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class OvenItemSmeltedEvent extends BalmEvent {

    private final Player player;
    private final Level level;
    private final BlockPos pos;
    private final ItemStack resultItem;

    public OvenItemSmeltedEvent(Player player, Level level, BlockPos pos, ItemStack resultItem) {
        this.player = player;
        this.level = level;
        this.pos = pos;
        this.resultItem = resultItem;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getResultItem() {
        return resultItem;
    }

    public Level getLevel() {
        return level;
    }

    public BlockPos getPos() {
        return pos;
    }
}
