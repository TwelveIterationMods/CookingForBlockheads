package net.blay09.mods.cookingforblockheads.block;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;

public class ModBlocks {

    private static final List<Block> blocks = Lists.newArrayList();

    public static Block cookingTable;
    public static Block oven;
    public static Block fridge;
    public static Block sink;
    public static Block toolRack;
    public static Block toaster;
    public static Block milkJar;
    public static Block cowJar;
    public static Block spiceRack;
    public static Block counter;
    public static Block corner;
    public static Block kitchenFloor;
    public static Block fruitBasket;
    public static Block cuttingBoard;

    public static void register(IForgeRegistry<Block> registry) {
        cookingTable = registerBlock(registry, new BlockCookingTable());
        oven = registerBlock(registry, new BlockOven());
        fridge = registerBlock(registry, new BlockFridge());
        sink = registerBlock(registry, new BlockSink());
        toolRack = registerBlock(registry, new BlockToolRack());
        toaster = registerBlock(registry, new BlockToaster());
        milkJar = registerBlock(registry, new BlockMilkJar());
        cowJar = registerBlock(registry, new BlockCowJar());
        spiceRack = registerBlock(registry, new BlockSpiceRack());
        counter = registerBlock(registry, new BlockCounter());
        corner = registerBlock(registry, new BlockCorner());
        kitchenFloor = registerBlock(registry, new BlockKitchenFloor());
        fruitBasket = registerBlock(registry, new BlockFruitBasket());
        cuttingBoard = registerBlock(registry, new BlockCuttingBoard());
    }

    private static Block registerBlock(IForgeRegistry<Block> registry, Block block) {
        if (block instanceof IRegisterableBlock) {
            ResourceLocation registryName = ((IRegisterableBlock) block).createRegistryName();
            block.setRegistryName(registryName);
            block.setUnlocalizedName(registryName.toString());
            registry.register(block);
            blocks.add(block);
            return block;
        } else {
            throw new RuntimeException("Tried to register a block that does not implement IBlockCFB");
        }
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        for (Block block : blocks) {
            ItemBlock itemBlock = ((IRegisterableBlock) block).createItemBlock(block);
            if (itemBlock != null) {
                itemBlock.setRegistryName(((IRegisterableBlock) block).createRegistryName());
            }
            registry.register(itemBlock);
        }
    }

    public static void registerModels() {
        for (Block block : blocks) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(((IRegisterableBlock) block).createRegistryName(), "inventory"));
        }
    }

    public static void registerTileEntities() {
        for (Block block : blocks) {
            Class<? extends TileEntity> tileEntityClass = ((IRegisterableBlock) block).getTileEntityClass();
            if (tileEntityClass != null) {
                GameRegistry.registerTileEntity(tileEntityClass, ((IRegisterableBlock) block).createRegistryName());
            }
        }
    }

}
