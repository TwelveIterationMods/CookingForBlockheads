package net.blay09.mods.cookingforblockheads.fabric.datagen;

import net.blay09.mods.balm.tags.BalmItemTags;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.blay09.mods.cookingforblockheads.item.ModItems;
import net.blay09.mods.cookingforblockheads.tag.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.data.recipes.SimpleCookingRecipeBuilder.smelting;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                shaped(RecipeCategory.DECORATIONS, ModBlocks.cookingTables.get(null))
                        .pattern("SSS")
                        .pattern("CBC")
                        .pattern("CCC")
                        .define('S', BalmItemTags.STONES)
                        .define('C', Blocks.TERRACOTTA)
                        .define('B', ModItems.craftingBook)
                        .unlockedBy("has_crafting_book", has(ModItems.craftingBook))
                        .save(exporter);

                shapeless(RecipeCategory.DECORATIONS, ModBlocks.cookingTables.get(null))
                        .requires(ModItemTags.DYED_COOKING_TABLES)
                        .requires(Items.BONE_MEAL)
                        .unlockedBy("has_dyed_cooking_table", has(ModItemTags.DYED_COOKING_TABLES))
                        .save(exporter, "remove_dye_from_cooking_table");

                ModBlocks.cookingTables.forEach((color, block) -> {
                    if (color != null) {
                        shapeless(RecipeCategory.DECORATIONS, block)
                                .requires(ModItemTags.COOKING_TABLES)
                                .requires(BalmItemTags.DYE_TAGS[color.ordinal()])
                                .unlockedBy("has_cooking_table", has(ModBlocks.cookingTables.get(null)))
                                .save(exporter, "dye_" + color.getSerializedName() + "_cooking_table");
                    }
                });

                shaped(RecipeCategory.DECORATIONS, ModBlocks.fruitBasket)
                        .pattern("SPS")
                        .define('S', ItemTags.WOODEN_SLABS)
                        .define('P', ItemTags.WOODEN_PRESSURE_PLATES)
                        .unlockedBy("has_planks", has(ItemTags.PLANKS))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.ovens.get(DyeColor.WHITE))
                        .pattern("GGG")
                        .pattern("IFI")
                        .pattern("III")
                        .define('I', BalmItemTags.IRON_INGOTS)
                        .define('G', Blocks.BLACK_STAINED_GLASS)
                        .define('F', Blocks.FURNACE)
                        .unlockedBy("has_furnace", has(Blocks.FURNACE))
                        .save(exporter);

                ModBlocks.ovens.forEach((color, block) ->
                        shapeless(RecipeCategory.DECORATIONS, block)
                                .requires(ModItemTags.OVENS)
                                .requires(BalmItemTags.DYE_TAGS[color.ordinal()])
                                .unlockedBy("has_oven", has(ModBlocks.ovens.get(DyeColor.WHITE)))
                                .save(exporter, "dye_" + color.getSerializedName() + "_oven"));

                shapeless(RecipeCategory.DECORATIONS, ModBlocks.fridges.get(DyeColor.WHITE))
                        .requires(BalmItemTags.WOODEN_CHESTS)
                        .requires(Blocks.IRON_DOOR)
                        .unlockedBy("has_iron_ingot", has(BalmItemTags.IRON_INGOTS))
                        .save(exporter);

                ModBlocks.ovens.forEach((color, block) ->
                        shapeless(RecipeCategory.DECORATIONS, block)
                                .requires(ModItemTags.FRIDGES)
                                .requires(BalmItemTags.DYE_TAGS[color.ordinal()])
                                .unlockedBy("has_fridge", has(ModBlocks.fridges.get(DyeColor.WHITE)))
                                .save(exporter, "dye_" + color.getSerializedName() + "_fridge"));


                shaped(RecipeCategory.DECORATIONS, ModBlocks.milkJar)
                        .pattern("GPG")
                        .pattern("GMG")
                        .pattern("GGG")
                        .define('G', Blocks.GLASS)
                        .define('P', ItemTags.PLANKS)
                        .define('M', Items.MILK_BUCKET)
                        .unlockedBy("has_milk_bucket", has(Items.MILK_BUCKET))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.connectors.get(null))
                        .pattern("SSS")
                        .pattern("CCC")
                        .pattern("CCC")
                        .define('S', BalmItemTags.STONES)
                        .define('C', Blocks.TERRACOTTA)
                        .unlockedBy("has_terracotta", has(Blocks.TERRACOTTA))
                        .save(exporter);

                shapeless(RecipeCategory.DECORATIONS, ModBlocks.connectors.get(null))
                        .requires(ModItemTags.DYED_CONNECTORS)
                        .requires(Items.BONE_MEAL)
                        .unlockedBy("has_dyed_connector", has(ModItemTags.DYED_CONNECTORS))
                        .save(exporter, "remove_dye_from_connector");

                ModBlocks.connectors.forEach((color, block) -> {
                    if (color != null) {
                        shapeless(RecipeCategory.DECORATIONS, block)
                                .requires(ModItemTags.CONNECTORS)
                                .requires(BalmItemTags.DYE_TAGS[color.ordinal()])
                                .unlockedBy("has_oven", has(ModBlocks.connectors.get(DyeColor.WHITE)))
                                .save(exporter, "dye_" + color.getSerializedName() + "_connector");
                    }
                });

                shaped(RecipeCategory.DECORATIONS, ModBlocks.counters.get(null))
                        .pattern("SSS")
                        .pattern("CBC")
                        .pattern("CCC")
                        .define('S', BalmItemTags.STONES)
                        .define('C', Blocks.TERRACOTTA)
                        .define('B', BalmItemTags.WOODEN_CHESTS)
                        .unlockedBy("has_terracotta", has(Blocks.TERRACOTTA))
                        .save(exporter);

                shapeless(RecipeCategory.DECORATIONS, ModBlocks.counters.get(null))
                        .requires(ModItemTags.DYED_COUNTERS)
                        .requires(Items.BONE_MEAL)
                        .unlockedBy("has_dyed_counter", has(ModItemTags.DYED_COUNTERS))
                        .save(exporter, "remove_dye_from_counter");

                ModBlocks.connectors.forEach((color, block) -> {
                    if (color != null) {
                        shapeless(RecipeCategory.DECORATIONS, block)
                                .requires(ModItemTags.COUNTERS)
                                .requires(BalmItemTags.DYE_TAGS[color.ordinal()])
                                .unlockedBy("has_counter", has(ModBlocks.counters.get(null)))
                                .save(exporter, "dye_" + color.getSerializedName() + "_counter");
                    }
                });

                shaped(RecipeCategory.DECORATIONS, ModBlocks.cuttingBoard)
                        .pattern("A")
                        .pattern("S")
                        .define('A', Items.IRON_AXE)
                        .define('S', ItemTags.WOODEN_SLABS)
                        .unlockedBy("has_axe", has(Items.IRON_AXE))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.sinks.get(null))
                        .pattern("III")
                        .pattern("CBC")
                        .pattern("CCC")
                        .define('I', BalmItemTags.IRON_INGOTS)
                        .define('C', Blocks.TERRACOTTA)
                        .define('B', Items.WATER_BUCKET)
                        .unlockedBy("has_water_bucket", has(Items.WATER_BUCKET))
                        .save(exporter);

                shapeless(RecipeCategory.DECORATIONS, ModBlocks.sinks.get(null))
                        .requires(ModItemTags.DYED_SINKS)
                        .requires(Items.BONE_MEAL)
                        .unlockedBy("has_dyed_sink", has(ModItemTags.DYED_SINKS))
                        .save(exporter, "remove_dye_from_sink");

                ModBlocks.connectors.forEach((color, block) -> {
                    if (color != null) {
                        shapeless(RecipeCategory.DECORATIONS, block)
                                .requires(ModItemTags.SINKS)
                                .requires(BalmItemTags.DYE_TAGS[color.ordinal()])
                                .unlockedBy("has_sink", has(ModBlocks.sinks.get(null)))
                                .save(exporter, "dye_" + color.getSerializedName() + "_sink");
                    }
                });

                shaped(RecipeCategory.DECORATIONS, ModBlocks.cabinets.get(null))
                        .pattern("CCC")
                        .pattern("CBC")
                        .define('C', Blocks.TERRACOTTA)
                        .define('B', BalmItemTags.WOODEN_CHESTS)
                        .unlockedBy("has_terracotta", has(Blocks.TERRACOTTA))
                        .save(exporter);

                shapeless(RecipeCategory.DECORATIONS, ModBlocks.cabinets.get(null))
                        .requires(ModItemTags.DYED_CABINETS)
                        .requires(Items.BONE_MEAL)
                        .unlockedBy("has_dyed_cabinet", has(ModItemTags.DYED_CABINETS))
                        .save(exporter, "remove_dye_from_cabinet");

                ModBlocks.connectors.forEach((color, block) -> {
                    if (color != null) {
                        shapeless(RecipeCategory.DECORATIONS, block)
                                .requires(ModItemTags.CABINETS)
                                .requires(BalmItemTags.DYE_TAGS[color.ordinal()])
                                .unlockedBy("has_cabinet", has(ModBlocks.cabinets.get(null)))
                                .save(exporter, "dye_" + color.getSerializedName() + "_cabinet");
                    }
                });

                shaped(RecipeCategory.DECORATIONS, ModBlocks.toolRack)
                        .pattern("SSS")
                        .pattern("I I")
                        .define('S', ItemTags.WOODEN_SLABS)
                        .define('I', BalmItemTags.IRON_NUGGETS)
                        .unlockedBy("has_iron_ingot", has(BalmItemTags.IRON_INGOTS))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.spiceRack)
                        .pattern("SS")
                        .define('S', ItemTags.WOODEN_SLABS)
                        .unlockedBy("has_planks", has(ItemTags.PLANKS))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.toaster)
                        .pattern("  B")
                        .pattern("IDI")
                        .pattern("ILI")
                        .define('I', BalmItemTags.IRON_INGOTS)
                        .define('B', Blocks.STONE_BUTTON)
                        .define('D', Blocks.IRON_TRAPDOOR)
                        .define('L', Items.LAVA_BUCKET)
                        .unlockedBy("has_lava_bucket", has(Items.LAVA_BUCKET))
                        .save(exporter);

                shaped(RecipeCategory.MISC, ModItems.craftingBook)
                        .pattern(" D ")
                        .pattern("CBC")
                        .pattern(" D ")
                        .define('D', TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "gems/diamond")))
                        .define('C', Blocks.CRAFTING_TABLE)
                        .define('B', ModItems.recipeBook)
                        .unlockedBy("has_recipe_book", has(ModItems.recipeBook))
                        .save(exporter);

                shapeless(RecipeCategory.MISC, ModItems.noFilterBook)
                        .requires(ModItems.recipeBook)
                        .unlockedBy("has_recipe_book", has(ModItems.recipeBook))
                        .save(exporter);

                shaped(RecipeCategory.MISC, ModItems.heatingUnit)
                        .pattern("NNN")
                        .pattern("ICI")
                        .define('N', BalmItemTags.IRON_NUGGETS)
                        .define('I', BalmItemTags.IRON_INGOTS)
                        .define('C', Blocks.COMPARATOR)
                        .unlockedBy("has_redstone", has(Items.REDSTONE))
                        .save(exporter);

                shaped(RecipeCategory.MISC, ModItems.preservationChamber)
                        .pattern("RRR")
                        .pattern("ICI")
                        .define('R', Items.REDSTONE)
                        .define('I', BalmItemTags.IRON_INGOTS)
                        .define('C', Blocks.COMPARATOR)
                        .unlockedBy("has_redstone", has(Items.REDSTONE))
                        .save(exporter);

                shaped(RecipeCategory.MISC, ModItems.iceUnit)
                        .pattern("SSS")
                        .pattern("ICI")
                        .define('S', Items.SNOWBALL)
                        .define('I', BalmItemTags.IRON_INGOTS)
                        .define('C', Blocks.COMPARATOR)
                        .unlockedBy("has_redstone", has(Items.REDSTONE))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.kitchenFloors.get(DyeColor.WHITE), 12)
                        .pattern("BW")
                        .pattern("WB")
                        .define('B', Blocks.COAL_BLOCK)
                        .define('W', Blocks.QUARTZ_BLOCK)
                        .unlockedBy("has_quartz", has(Items.QUARTZ))
                        .save(exporter);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.kitchenFloors.get(DyeColor.WHITE), 4)
                        .pattern("BW")
                        .pattern("WB")
                        .define('B', Blocks.WHITE_CONCRETE)
                        .define('W', Blocks.BLACK_CONCRETE)
                        .unlockedBy("has_white_concrete", has(Items.WHITE_CONCRETE))
                        .save(exporter, "kitchen_floor_from_concrete");

                dyedKitchenFloorRecipe(ModBlocks.kitchenFloors.get(DyeColor.ORANGE).asBlock(), BalmItemTags.ORANGE_DYES).save(exporter);
                dyedKitchenFloorRecipe(ModBlocks.kitchenFloors.get(DyeColor.MAGENTA).asBlock(), BalmItemTags.MAGENTA_DYES).save(exporter);
                dyedKitchenFloorRecipe(ModBlocks.kitchenFloors.get(DyeColor.LIGHT_BLUE).asBlock(), BalmItemTags.LIGHT_BLUE_DYES).save(exporter);
                dyedKitchenFloorRecipe(ModBlocks.kitchenFloors.get(DyeColor.YELLOW).asBlock(), BalmItemTags.YELLOW_DYES).save(exporter);
                dyedKitchenFloorRecipe(ModBlocks.kitchenFloors.get(DyeColor.LIME).asBlock(), BalmItemTags.LIME_DYES).save(exporter);
                dyedKitchenFloorRecipe(ModBlocks.kitchenFloors.get(DyeColor.PINK).asBlock(), BalmItemTags.PINK_DYES).save(exporter);
                dyedKitchenFloorRecipe(ModBlocks.kitchenFloors.get(DyeColor.GRAY).asBlock(), BalmItemTags.GRAY_DYES).save(exporter);
                dyedKitchenFloorRecipe(ModBlocks.kitchenFloors.get(DyeColor.LIGHT_GRAY).asBlock(), BalmItemTags.LIGHT_GRAY_DYES).save(exporter);
                dyedKitchenFloorRecipe(ModBlocks.kitchenFloors.get(DyeColor.CYAN).asBlock(), BalmItemTags.CYAN_DYES).save(exporter);
                dyedKitchenFloorRecipe(ModBlocks.kitchenFloors.get(DyeColor.PURPLE).asBlock(), BalmItemTags.PURPLE_DYES).save(exporter);
                dyedKitchenFloorRecipe(ModBlocks.kitchenFloors.get(DyeColor.BLUE).asBlock(), BalmItemTags.BLUE_DYES).save(exporter);
                dyedKitchenFloorRecipe(ModBlocks.kitchenFloors.get(DyeColor.BROWN).asBlock(), BalmItemTags.BROWN_DYES).save(exporter);
                dyedKitchenFloorRecipe(ModBlocks.kitchenFloors.get(DyeColor.GREEN).asBlock(), BalmItemTags.GREEN_DYES).save(exporter);
                dyedKitchenFloorRecipe(ModBlocks.kitchenFloors.get(DyeColor.RED).asBlock(), BalmItemTags.RED_DYES).save(exporter);
                dyedKitchenFloorRecipe(ModBlocks.kitchenFloors.get(DyeColor.BLACK).asBlock(), BalmItemTags.BLACK_DYES).save(exporter);

                smelting(Ingredient.of(Items.BOOK), RecipeCategory.MISC, ModItems.recipeBook, 0.15f, 200).unlockedBy("has_book", has(Items.BOOK))
                        .save(exporter);
                smelting(Ingredient.of(ModItems.noFilterBook), RecipeCategory.MISC, ModItems.recipeBook, 0f, 200).unlockedBy("has_no_filter_edition",
                        has(ModItems.noFilterBook)).save(exporter, "recipe_book_from_smelting_no_filter_edition");
            }

            private ShapedRecipeBuilder dyedKitchenFloorRecipe(Block kitchenFloor, TagKey<Item> dyeTag) {
                final var whiteKitchenFloor = ModBlocks.kitchenFloors.get(DyeColor.WHITE);
                return shaped(RecipeCategory.DECORATIONS, kitchenFloor, 8)
                        .pattern("FFF")
                        .pattern("FDF")
                        .pattern("FFF")
                        .define('F', whiteKitchenFloor)
                        .define('D', dyeTag)
                        .unlockedBy("has_kitchen_floor", has(whiteKitchenFloor));
            }
        };
    }

    @Override
    public String getName() {
        return CookingForBlockheads.MOD_ID;
    }
}
