package net.blay09.mods.cookingforblockheads.menu.slot;

import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;

public class SlotOvenTool extends Slot {

    private static final Identifier[] ovenToolIcons = new Identifier[]{
            Identifier.withDefaultNamespace("container/slot/bakeware"),
            Identifier.withDefaultNamespace("container/slot/pot"),
            Identifier.withDefaultNamespace("container/slot/saucepan"),
            Identifier.withDefaultNamespace("container/slot/skillet")
    };

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
    public Identifier getNoItemIcon() {
        return ovenToolIcons[iconIndex];
    }
}

