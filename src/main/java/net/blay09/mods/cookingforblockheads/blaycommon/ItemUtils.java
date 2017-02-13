package net.blay09.mods.cookingforblockheads.blaycommon;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;

public class ItemUtils {

	public static void dropContent(World world, BlockPos pos, IItemHandler itemHandler) {
		for (int i = 0; i < itemHandler.getSlots(); i++) {
			ItemStack itemStack = itemHandler.getStackInSlot(i);
			if (!itemStack.isEmpty()) {
				float offsetX = world.rand.nextFloat() * 0.8f + 0.1f;
				float offsetY = world.rand.nextFloat() * 0.8f + 0.1f;
				EntityItem entityItem;
				while (itemStack.getCount() > 0) {
					float offsetZ = world.rand.nextFloat() * 0.8f + 0.1f;
					int stackSize = Math.min(world.rand.nextInt(21) + 10, itemStack.getCount());
					itemStack.shrink(stackSize);

					entityItem = new EntityItem(world, (double) (pos.getX() + offsetX), (double) (pos.getY() + offsetY), (double) (pos.getZ() + offsetZ), new ItemStack(itemStack.getItem(), stackSize, itemStack.getItemDamage()));
					float motion = 0.05f;
					entityItem.motionX = world.rand.nextGaussian() * motion;
					entityItem.motionY = world.rand.nextGaussian() * motion + 0.2f;
					entityItem.motionZ = world.rand.nextGaussian() * motion;

					NBTTagCompound tagCompound = itemStack.getTagCompound();
					if (tagCompound != null) {
						entityItem.getEntityItem().setTagCompound(tagCompound.copy());
					}
					world.spawnEntity(entityItem);
				}
			}
		}
	}

	public static void dropItem(World world, BlockPos pos, ItemStack itemStack) {
		if(itemStack.isEmpty()) {
			return;
		}
		float offsetX = world.rand.nextFloat() * 0.8f + 0.1f;
		float offsetY = world.rand.nextFloat() * 0.8f + 0.1f;
		while (itemStack.getCount() > 0) {
			float offsetZ = world.rand.nextFloat() * 0.8f + 0.1f;
			int stackSize = Math.min(itemStack.getCount(), world.rand.nextInt(21) + 10);
			itemStack.shrink(stackSize);

			EntityItem entityItem = new EntityItem(world, (double) (pos.getX() + offsetX), (double) (pos.getY() + offsetY), (double) (pos.getZ() + offsetZ), new ItemStack(itemStack.getItem(), stackSize, itemStack.getItemDamage()));
			float motion = 0.05f;
			entityItem.motionX = world.rand.nextGaussian() * motion;
			entityItem.motionY = world.rand.nextGaussian() * motion + 0.2f;
			entityItem.motionZ = world.rand.nextGaussian() * motion;
			NBTTagCompound tagCompound = itemStack.getTagCompound();
			if (tagCompound != null) {
				entityItem.getEntityItem().setTagCompound(tagCompound.copy());
			}
			world.spawnEntity(entityItem);
		}
	}

	public static boolean areItemStacksEqualWithWildcard(ItemStack first, ItemStack second) {
		return !(first.isEmpty() || second.isEmpty()) && first.getItem() == second.getItem() && (first.getItemDamage() == second.getItemDamage() || first.getItemDamage() == OreDictionary.WILDCARD_VALUE || second.getItemDamage() == OreDictionary.WILDCARD_VALUE);
	}

}
