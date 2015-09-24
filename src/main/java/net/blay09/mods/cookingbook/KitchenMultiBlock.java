package net.blay09.mods.cookingbook;

import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.cookingbook.api.kitchen.IKitchenSmeltingProvider;
import net.blay09.mods.cookingbook.api.kitchen.IKitchenStorageProvider;
import net.blay09.mods.cookingbook.api.kitchen.IKitchenItemProvider;
import net.blay09.mods.cookingbook.api.kitchen.IMultiblockKitchen;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KitchenMultiBlock {

    public static final Map<String, Class<? extends IMultiblockKitchen>> tileEntityWrappers = new HashMap<>();
    public static final Map<String, Class<? extends IMultiblockKitchen>> blockWrappers = new HashMap<>();

    private class BlockPosition {

        public final int x;
        public final int y;
        public final int z;

        public BlockPosition(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BlockPosition that = (BlockPosition) o;

            if (x != that.x) return false;
            if (y != that.y) return false;
            return z == that.z;

        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            result = 31 * result + z;
            return result;
        }
    }

    private final Map<BlockPosition, IMultiblockKitchen> kitchenParts = new HashMap<>();
    private final List<IKitchenStorageProvider> storageProviderList = new ArrayList<>();
    private final List<IKitchenItemProvider> itemProviderList = new ArrayList<>();
    private final List<IKitchenSmeltingProvider> smeltingProviderList = new ArrayList<>();

    public KitchenMultiBlock(World world, int x, int y, int z) {
        findNeighbourKitchenBlocks(world, x, y, z);
    }

    private void findNeighbourKitchenBlocks(World world, int x, int y, int z) {
        for (int i = 0; i <= 5; i++) {
            ForgeDirection dir = ForgeDirection.getOrientation(i);
            BlockPosition position = new BlockPosition(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
            if (!kitchenParts.containsKey(position)) {
                TileEntity tileEntity = world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
                IMultiblockKitchen kitchenPart;
                if (tileEntity instanceof IMultiblockKitchen) {
                    kitchenPart = (IMultiblockKitchen) tileEntity;
                } else if (tileEntity != null) {
                    kitchenPart = getWrapper(tileEntity);
                } else {
                    kitchenPart = getWrapper(world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ));
                }
                if (kitchenPart != null) {
                    kitchenParts.put(position, kitchenPart);
                    if (kitchenPart instanceof IKitchenStorageProvider) {
                        storageProviderList.add((IKitchenStorageProvider) kitchenPart);
                    }
                    if (kitchenPart instanceof IKitchenSmeltingProvider) {
                        smeltingProviderList.add((IKitchenSmeltingProvider) kitchenPart);
                    }
                    if (kitchenPart instanceof IKitchenItemProvider) {
                        itemProviderList.add((IKitchenItemProvider) kitchenPart);
                    }
                    findNeighbourKitchenBlocks(world, position.x, position.y, position.z);
                }
            }
        }
    }

    private final List<IInventory> sourceInventories = new ArrayList<>();

    public List<IInventory> getSourceInventories(InventoryPlayer playerInventory) {
        sourceInventories.clear();
        sourceInventories.add(playerInventory);
        for (IKitchenStorageProvider provider : storageProviderList) {
            sourceInventories.add(provider.getInventory());
        }
        return sourceInventories;
    }

    public ItemStack smeltItem(ItemStack itemStack, int count) {
        ItemStack restStack = itemStack.copy().splitStack(count);
        for (IKitchenSmeltingProvider provider : smeltingProviderList) {
            restStack = provider.smeltItem(restStack);
            if (restStack == null) {
                break;
            }
        }
        itemStack.stackSize -= (count - (restStack != null ? restStack.stackSize : 0));
        if (itemStack.stackSize <= 0) {
            return null;
        }
        return itemStack;
    }

    public boolean hasSmeltingProvider() {
        return smeltingProviderList.size() > 0;
    }

    public List<IKitchenItemProvider> getItemProviders() {
        return itemProviderList;
    }

    public static IMultiblockKitchen getWrapper(Block block) {
        GameRegistry.UniqueIdentifier identifier = GameRegistry.findUniqueIdentifierFor(block);
        if (identifier != null) {
            Class<? extends IMultiblockKitchen> clazz = blockWrappers.get(identifier.modId + ":" + identifier.name);
            if (clazz != null) {
                try {
                    return clazz.getConstructor(Block.class).newInstance(block);
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static IMultiblockKitchen getWrapper(TileEntity tileEntity) {
        Class<? extends IMultiblockKitchen> clazz = tileEntityWrappers.get(tileEntity.getClass().getName());
        if (clazz != null) {
            try {
                return clazz.getConstructor(TileEntity.class).newInstance(tileEntity);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
