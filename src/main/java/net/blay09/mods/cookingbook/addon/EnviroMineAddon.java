package net.blay09.mods.cookingbook.addon;

import net.blay09.mods.cookingbook.KitchenMultiBlock;

public class EnviroMineAddon {

    public EnviroMineAddon() {
        KitchenMultiBlock.wrapperClasses.put("enviromine.blocks.tiles.TileEntityFreezer", SimpleStorageProvider.class);
    }

}
