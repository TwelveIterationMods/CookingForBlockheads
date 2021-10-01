package net.blay09.mods.cookingforblockheads.menu.slot;

import com.mojang.datafixers.util.Pair;
import net.blay09.mods.cookingforblockheads.client.ModTextures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;

public class SlotOvenTool extends Slot {

    private final int iconIndex;

    public SlotOvenTool(Container container, int id, int x, int y, int iconIndex) {
        super(container, id, x, y);
        this.iconIndex = iconIndex;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Nullable
    @Override
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(InventoryMenu.BLOCK_ATLAS, ModTextures.ovenToolIcons[iconIndex]);
    }
}

