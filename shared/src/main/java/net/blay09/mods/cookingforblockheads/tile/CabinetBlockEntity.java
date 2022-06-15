package net.blay09.mods.cookingforblockheads.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class CabinetBlockEntity extends CounterBlockEntity {

    public CabinetBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.cabinet.get(), pos, state);
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("container.cookingforblockheads.cabinet");
    }
}
