package net.blay09.mods.cookingforblockheads.blaycommon;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Random;

public class ItemUtils {

	private static final Random rand = new Random();

	public static void dropItemHandlerItems(World world, BlockPos pos, IItemHandler itemHandler) {
		for (int i = 0; i < itemHandler.getSlots(); i++) {
			ItemStack itemStack = itemHandler.getStackInSlot(i);
			if (!itemStack.isEmpty()) {
				spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
			}
		}
	}

	public static void spawnItemStack(World world, double x, double y, double z, ItemStack stack) {
		float offsetX = rand.nextFloat() * 0.8F + 0.1F;
		float offsetY = rand.nextFloat() * 0.8F + 0.1F;
		float offsetZ = rand.nextFloat() * 0.8F + 0.1F;

		while (!stack.isEmpty()) {
			EntityItem entityitem = new EntityItem(world, x + (double) offsetX, y + (double) offsetY, z + (double) offsetZ, stack.splitStack(rand.nextInt(21) + 10));
			float motion = 0.05F;
			entityitem.motionX = rand.nextGaussian() * motion;
			entityitem.motionY = rand.nextGaussian() * motion + 0.2;
			entityitem.motionZ = rand.nextGaussian() * motion;
			world.spawnEntity(entityitem);
		}
	}

	public static boolean areItemStacksEqualWithWildcard(ItemStack first, ItemStack second) {
		return !(first.isEmpty() || second.isEmpty()) && first.getItem() == second.getItem() && (first.getItemDamage() == second.getItemDamage() || first.getItemDamage() == OreDictionary.WILDCARD_VALUE || second.getItemDamage() == OreDictionary.WILDCARD_VALUE);
	}

}
