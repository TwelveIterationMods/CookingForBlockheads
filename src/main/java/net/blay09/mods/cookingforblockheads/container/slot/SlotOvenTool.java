package net.blay09.mods.cookingforblockheads.container.slot;

import net.blay09.mods.cookingforblockheads.client.ModSprites;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotOvenTool extends SlotItemHandler {

    public SlotOvenTool(IItemHandler itemHandler, int id, int x, int y, int iconIndex) {
        super(itemHandler, id, x, y);
        setBackground(AtlasTexture.LOCATION_BLOCKS_TEXTURE, ModSprites.ovenToolIcons[iconIndex]);
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

}

