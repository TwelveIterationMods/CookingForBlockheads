package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.tile.TileCowJar;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockCowJar extends BlockMilkJar {

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileCowJar();
    }

    @Override
    public String getIdentifier() {
        return "cow_jar";
    }

    @Nullable
    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileCowJar.class;
    }

}
