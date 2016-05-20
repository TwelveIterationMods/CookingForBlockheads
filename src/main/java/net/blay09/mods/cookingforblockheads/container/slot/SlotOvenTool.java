package net.blay09.mods.cookingforblockheads.container.slot;

import net.blay09.mods.cookingforblockheads.client.ClientProxy;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SlotOvenTool extends SlotItemHandler {

    private final int iconIndex;

    public SlotOvenTool(IItemHandler itemHandler, int id, int x, int y, int iconIndex) {
        super(itemHandler, id, x, y);
        this.iconIndex = iconIndex;
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    @Nonnull
    public TextureAtlasSprite getBackgroundSprite() {
        return ClientProxy.ovenToolIcons[iconIndex];
    }

}
