package net.blay09.mods.cookingforblockheads.api.event;

import net.blay09.mods.balm.platform.event.BidirectionalEventMapper;
import net.blay09.mods.balm.platform.event.EventMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public record OvenItemSmeltedEvent(Player player, Level level, BlockPos pos, ItemStack resultItem) {
    public static final BidirectionalEventMapper<Consumer<OvenItemSmeltedEvent>> EVENT = EventMapper.createBound(OvenItemSmeltedEvent.class);

}
