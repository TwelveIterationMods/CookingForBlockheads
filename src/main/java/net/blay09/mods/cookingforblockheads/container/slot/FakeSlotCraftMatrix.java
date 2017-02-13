package net.blay09.mods.cookingforblockheads.container.slot;

import com.google.common.collect.Lists;
import net.blay09.mods.cookingforblockheads.blaycommon.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class FakeSlotCraftMatrix extends FakeSlot {

	private static final float ITEM_SWITCH_TIME = 40f;

	private final List<ItemStack> visibleStacks = Lists.newArrayList();

	private float visibleItemTime;
	private int visibleItemIndex;
	private boolean isLocked;

	public FakeSlotCraftMatrix(int slotId, int x, int y) {
		super(slotId, x, y);
	}

	public void setIngredient(List<ItemStack> ingredients) {
		ItemStack prevLockStack = isLocked ? getStack() : null;
		visibleStacks.clear();
		if(ingredients != null) {
			for(ItemStack itemStack : ingredients) {
				if(itemStack != null) {
					if (itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
						List<ItemStack> subItems = Lists.newArrayList();
						itemStack.getItem().getSubItems(itemStack.getItem(), itemStack.getItem().getCreativeTab(), subItems);
						visibleStacks.addAll(subItems);
					} else {
						itemStack.setCount(1);
						visibleStacks.add(itemStack);
					}
				}
			}
		}
		visibleItemTime = 0;
		visibleItemIndex = 0;
		isLocked = false;
		if(prevLockStack != null) {
			for(int i = 0; i < visibleStacks.size(); i++) {
				if(ItemUtils.areItemStacksEqualWithWildcard(visibleStacks.get(i), prevLockStack)) {
					visibleItemIndex = i;
					isLocked = true;
				}
			}
		}
	}

	public void updateSlot(float partialTicks) {
		if(!isLocked) {
			visibleItemTime += partialTicks;
			if (visibleItemTime >= ITEM_SWITCH_TIME) {
				visibleItemIndex++;
				if (visibleItemIndex >= visibleStacks.size()) {
					visibleItemIndex = 0;
				}
				visibleItemTime = 0;
			}
		}
	}

	@Override
	public ItemStack getStack() {
		return visibleStacks.size() > 0 ? visibleStacks.get(visibleItemIndex) : ItemStack.EMPTY;
	}

	@Override
	public boolean getHasStack() {
		return visibleStacks.size() > 0;
	}

	@Override
	public boolean canBeHovered() {
		return visibleStacks.size() > 0;
	}

	public List<ItemStack> getVisibleStacks() {
		return visibleStacks;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean locked) {
		isLocked = locked;
	}

	public void scrollDisplayList(int i) {
		isLocked = true;
		visibleItemIndex += i;
		if(visibleItemIndex >= visibleStacks.size()) {
			visibleItemIndex = 0;
		} else if (visibleItemIndex < 0) {
			visibleItemIndex = visibleStacks.size() - 1;
		}
	}

}
