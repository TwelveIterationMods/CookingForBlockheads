package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.tile.TileCowJar;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCowJar extends BlockMilkJar {

    public BlockCowJar() {
        super("cowJar");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileCowJar();
    }

}
