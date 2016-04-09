package net.blay09.mods.cookingforblockheads.container.slot;

import com.google.common.collect.Lists;
import net.blay09.mods.cookingforblockheads.registry.recipe.FoodIngredient;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class FakeSlotCraftMatrix extends FakeSlot {

	private static final int ITEM_SWITCH_TIME = 20;

	private final List<ItemStack> visibleStacks = Lists.newArrayList();

	private FoodIngredient ingredient;

	private int visibleItemTime;
	private int visibleItemIndex;

	public FakeSlotCraftMatrix(int slotId, int x, int y) {
		super(slotId, x, y);
	}

	public void setIngredient(FoodIngredient ingredient) {
		this.ingredient = ingredient;
		visibleStacks.clear();
		if(ingredient != null) {
			for(ItemStack itemStack : ingredient.getItemStacks()) {
				if(itemStack != null) {
					if (itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
						List<ItemStack> subItems = Lists.newArrayList();
						itemStack.getItem().getSubItems(itemStack.getItem(), itemStack.getItem().getCreativeTab(), subItems);
						visibleStacks.addAll(subItems);
					} else {
						visibleStacks.add(itemStack);
					}
				}
			}
		}
		visibleItemTime = 0;
		visibleItemIndex = 0;
	}

	public void updateSlot() {
		visibleItemTime++;
		if(visibleItemTime >= ITEM_SWITCH_TIME) {
			visibleItemIndex++;
			if(visibleItemIndex >= visibleStacks.size()) {
				visibleItemIndex = 0;
			}
		}
	}

	@Override
	public ItemStack getStack() {
		return ingredient != null ? visibleStacks.get(visibleItemIndex) : null;
	}

	@Override
	public boolean getHasStack() {
		return ingredient != null;
	}

	@Override
	public boolean canBeHovered() {
		return ingredient != null;
	}
}
