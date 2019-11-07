package net.blay09.mods.cookingforblockheads.block;

import net.minecraft.util.ResourceLocation;

public abstract class BlockDyeableKitchen extends BlockKitchen {

    protected BlockDyeableKitchen(Properties properties, ResourceLocation registryName) {
        super(properties, registryName);
    }

    @Override
    protected boolean isDyeable() {
        return true;
    }
}
