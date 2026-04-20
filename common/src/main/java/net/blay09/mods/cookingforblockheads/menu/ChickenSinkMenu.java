package net.blay09.mods.cookingforblockheads.menu;

import net.blay09.mods.balm.world.inventory.QuickMove;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.tag.ModBlockTags;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class ChickenSinkMenu extends AbstractContainerMenu {

    private static final Identifier WHEAT_SEEDS_SLOT_ICON = Identifier.withDefaultNamespace("container/slot/wheat_seeds");
    private static final int SLOT_COUNT = 6;

    private final Container container;
    private final ContainerLevelAccess access;
    private final QuickMove.Routing quickMove;

    public ChickenSinkMenu(int windowId, Inventory playerInventory) {
        this(windowId, playerInventory, new SimpleContainer(SLOT_COUNT), ContainerLevelAccess.NULL);
    }

    public ChickenSinkMenu(int windowId, Inventory playerInventory, Container container, ContainerLevelAccess access) {
        super(ModMenus.chickenSink.value(), windowId);
        checkContainerSize(container, SLOT_COUNT);
        this.container = container;
        this.access = access;
        container.startOpen(playerInventory.player);

        addSlot(new Slot(container, 0, 26, 20) {
            @Override
            public boolean mayPlace(ItemStack itemStack) {
                return itemStack.is(ItemTags.CHICKEN_FOOD);
            }

            @Override
            public Identifier getNoItemIcon() {
                return WHEAT_SEEDS_SLOT_ICON;
            }
        });

        for (int i = 1; i < SLOT_COUNT; i++) {
            addSlot(new Slot(container, i, 44 + i * 18, 20) {
                @Override
                public boolean mayPlace(ItemStack itemStack) {
                    return itemStack.is(ItemTags.EGGS);
                }
            });
        }


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 51 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 109));
        }

        quickMove = QuickMove.create(this, this::moveItemStackTo)
                .slot("chicken_food", 0)
                .slotRange("eggs", 1, SLOT_COUNT)
                .disableDefaultRoutes()
                .route(QuickMove.CONTAINER, QuickMove.PLAYER, true)
                .route(it -> it.is(ItemTags.CHICKEN_FOOD), QuickMove.PLAYER, "chicken_food")
                .route(it -> it.is(ItemTags.EGGS), QuickMove.PLAYER, "eggs")
                .build();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        return quickMove.transfer(this, player, slotIndex);
    }

    @Override
    public boolean stillValid(Player player) {
        return access.evaluate((level, pos) -> level.getBlockState(pos).is(ModBlockTags.CHICKEN_SINKS) && player.isWithinBlockInteractionRange(pos, 4f), true);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        container.stopOpen(player);
    }

}
