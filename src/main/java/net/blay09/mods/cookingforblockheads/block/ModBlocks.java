package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public class ModBlocks {

    private static final List<Block> coloredBlocks = new ArrayList<>();

    public static Block oven;
    public static Block toolRack;
    public static Block toaster;
    public static Block milkJar;
    public static Block cowJar;
    public static Block spiceRack;
    public static Block kitchenFloor;
    public static Block fruitBasket;
    public static Block cuttingBoard;
    public static Block cookingTable;
    public static Block fridge;
    public static Block sink;
    public static Block counter;
    public static Block cabinet;
    public static Block corner;

    public static void register(IForgeRegistry<Block> registry) {
        registry.registerAll(
                cookingTable = registerBlock(registry, "cooking_table", CookingTableBlock::new),
                fridge = registerBlock(registry, "fridge", FridgeBlock::new),
                sink = registerBlock(registry, "sink", BlockSink::new),
                counter = registerBlock(registry, "counter", KitchenCounterBlock::new),
                cabinet = registerBlock(registry, "cabinet", BlockCabinet::new),
                corner = registerBlock(registry, "corner", KitchenCornerBlock::new),
                oven = new BlockOven().setRegistryName(BlockOven.name),
                toolRack = new BlockToolRack().setRegistryName(BlockToolRack.name),
                toaster = new BlockToaster().setRegistryName(BlockToaster.name),
                milkJar = new BlockMilkJar().setRegistryName(BlockMilkJar.name),
                cowJar = new BlockCowJar().setRegistryName(BlockCowJar.name),
                spiceRack = new BlockSpiceRack().setRegistryName(BlockSpiceRack.name),
                kitchenFloor = new BlockKitchenFloor().setRegistryName(BlockKitchenFloor.name),
                fruitBasket = new BlockFruitBasket().setRegistryName(BlockFruitBasket.name),
                cuttingBoard = new BlockCuttingBoard().setRegistryName(BlockCuttingBoard.name)
        );
    }

    private static Block registerBlock(IForgeRegistry<Block> registry, String baseName, BiFunction<DyeColor, ResourceLocation, BlockDyeableKitchen> factory) {
        ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, baseName);
        return factory.apply(DyeColor.WHITE, registryName).setRegistryName(registryName);
    }

    private static List<Block> registerColoredBlocks(IForgeRegistry<Block> registry, String baseName, BiFunction<DyeColor, ResourceLocation, BlockDyeableKitchen> factory) {
        List<Block> blocks = new ArrayList<>();
        for (DyeColor color : DyeColor.values()) {
            ResourceLocation registryName = new ResourceLocation(CookingForBlockheads.MOD_ID, color.getName() + "_" + baseName);
            Block block = factory.apply(color, registryName).setRegistryName(registryName);
            registry.register(block);
            coloredBlocks.add(block);
            blocks.add(block);
        }
        return blocks;
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        for (Block coloredBlock : coloredBlocks) {
            ResourceLocation registryName = Objects.requireNonNull(coloredBlock.getRegistryName());
            registry.register(new BlockItem(coloredBlock, createItemProperties()).setRegistryName(registryName));
        }

        registry.registerAll(
                new BlockItem(ModBlocks.oven, createItemProperties()).setRegistryName(BlockOven.name),
                new BlockItem(ModBlocks.toolRack, createItemProperties()).setRegistryName(BlockToolRack.name),
                new BlockItem(ModBlocks.toaster, createItemProperties()).setRegistryName(BlockToaster.name),
                new BlockItem(ModBlocks.milkJar, createItemProperties()).setRegistryName(BlockMilkJar.name),
                new BlockItem(ModBlocks.cowJar, createItemProperties()).setRegistryName(BlockCowJar.name),
                new BlockItem(ModBlocks.spiceRack, createItemProperties()).setRegistryName(BlockSpiceRack.name),
                new BlockItem(ModBlocks.kitchenFloor, createItemProperties()).setRegistryName(BlockKitchenFloor.name),
                new BlockItem(ModBlocks.fruitBasket, createItemProperties()).setRegistryName(BlockFruitBasket.name)
        );
    }

    private static Item.Properties createItemProperties() {
        return new Item.Properties().group(CookingForBlockheads.itemGroup);
    }

}
