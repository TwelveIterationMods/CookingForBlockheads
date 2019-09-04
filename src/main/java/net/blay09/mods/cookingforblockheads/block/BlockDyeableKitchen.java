package net.blay09.mods.cookingforblockheads.block;

import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;

public abstract class BlockDyeableKitchen extends BlockKitchen {

    private final DyeColor dyeColor;
    private final ResourceLocation registryName;

    protected BlockDyeableKitchen(Properties properties, DyeColor dyeColor, ResourceLocation registryName) {
        super(properties, registryName);
        this.dyeColor = dyeColor;
        this.registryName = registryName;
    }

}
