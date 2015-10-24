package net.blay09.mods.cookingbook.container;

import net.blay09.mods.cookingbook.api.kitchen.IKitchenItemProvider;
import net.blay09.mods.cookingbook.registry.CookingRegistry;
import net.blay09.mods.cookingbook.registry.food.FoodIngredient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class SlotCraftMatrix extends Slot {

    private static final int ITEM_SWITCH_TIME = 20;

    private final EntityPlayer player;
    private FoodIngredient ingredient;
    private boolean enabled = true;

    private List<IInventory> sourceInventories;
    private List<IKitchenItemProvider> itemProviders;
    private boolean isNoFilter;
    private ItemStack[] visibleStacks;
    private int visibleItemTime;
    private int visibleItemIndex;

    public SlotCraftMatrix(EntityPlayer player, IInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.player = player;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        return false;
    }

    public void setIngredient(FoodIngredient ingredient) {
        this.ingredient = ingredient;
        if(ingredient == null) {
            putStack(null);
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean func_111238_b() {
        return enabled;
    }

    /**
     * SERVER ONLY
     */
    public void updateVisibleStacks() {
        if(ingredient != null) {
            visibleStacks = ingredient.getItemStacks();
            if (!isNoFilter && ingredient.getItemStacks().length > 1 && !ingredient.isToolItem()) {
                List<ItemStack> visibleStackList = new ArrayList<>();
                stackLoop:for (ItemStack visibleStack : visibleStacks) {
                    for (int i = 0; i < sourceInventories.size(); i++) {
                        for (int j = 0; j < sourceInventories.get(i).getSizeInventory(); j++) {
                            ItemStack itemStack = sourceInventories.get(i).getStackInSlot(j);
                            if (CookingRegistry.areItemStacksEqualWithWildcard(itemStack, visibleStack)) {
                                ItemStack displayStack = visibleStack.copy();
                                if (displayStack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                                    displayStack.setItemDamage(0);
                                }
                                visibleStackList.add(displayStack);
                                continue stackLoop;
                            }
                        }
                    }
                    for(IKitchenItemProvider provider : itemProviders) {
                        for(ItemStack itemStack : provider.getProvidedItemStacks()) {
                            if(CookingRegistry.areItemStacksEqualWithWildcard(itemStack, visibleStack)) {
                                ItemStack displayStack = visibleStack.copy();
                                if (displayStack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                                    displayStack.setItemDamage(0);
                                }
                                visibleStackList.add(displayStack);
                                continue stackLoop;
                            }
                        }
                    }
                }
                visibleStacks = visibleStackList.toArray(new ItemStack[visibleStackList.size()]);
            } else {
                for(int i = 0; i < visibleStacks.length; i++) {
                    ItemStack displayStack = visibleStacks[i].copy();
                    if(displayStack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                        displayStack.setItemDamage(0);
                    }
                    visibleStacks[i] = displayStack;
                }
            }
            if(visibleStacks.length == 1) {
                putStack(visibleStacks[0]);
                ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(player.openContainer.windowId, slotNumber, visibleStacks[0]));
            }
            visibleItemTime = ITEM_SWITCH_TIME;
            visibleItemIndex = 0;
        } else {
            putStack(null);
            ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(player.openContainer.windowId, slotNumber, null));
            visibleStacks = null;
        }
        update();
    }

    /**
     * SERVER ONLY
     */
    public void update() {
        if(visibleStacks != null && visibleStacks.length > 1) {
            visibleItemTime++;
            if(visibleItemTime >= ITEM_SWITCH_TIME) {
                visibleItemIndex++;
                if(visibleItemIndex >= visibleStacks.length) {
                    visibleItemIndex = 0;
                }
                putStack(visibleStacks[visibleItemIndex]);
                ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(player.openContainer.windowId, slotNumber, visibleStacks[visibleItemIndex]));
                visibleItemTime = 0;
            }
        }
    }

    /**
     * SERVER ONLY
     * @param sourceInventories
     */
    public void setSourceInventories(List<IInventory> sourceInventories) {
        this.sourceInventories = sourceInventories;
    }

    /**
     * SERVER ONLY
     * @param itemProviders
     */
    public void setItemProviders(List<IKitchenItemProvider> itemProviders) {
        this.itemProviders = itemProviders;
    }

    /**
     * SERVER ONLY
     * @param isNoFilter
     */
    public void setNoFilter(boolean isNoFilter) {
        this.isNoFilter = isNoFilter;
    }
}
