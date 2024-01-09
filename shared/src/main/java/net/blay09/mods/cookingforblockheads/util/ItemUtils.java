package net.blay09.mods.cookingforblockheads.util;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Random;

public class ItemUtils {

    private static final Random rand = new Random();

    public static void spawnItemStack(Level level, double x, double y, double z, ItemStack stack) {
        float offsetX = rand.nextFloat() * 0.8F + 0.1F;
        float offsetY = rand.nextFloat() * 0.8F + 0.1F;
        float offsetZ = rand.nextFloat() * 0.8F + 0.1F;

        while (!stack.isEmpty()) {
            ItemEntity itemEntity = new ItemEntity(level, x + (double) offsetX, y + (double) offsetY, z + (double) offsetZ, stack.split(rand.nextInt(21) + 10));
            float motion = 0.05F;
            itemEntity.setDeltaMovement(rand.nextGaussian() * motion, rand.nextGaussian() * motion + 0.2, rand.nextGaussian() * motion);
            level.addFreshEntity(itemEntity);
        }
    }

}
