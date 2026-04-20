package net.blay09.mods.cookingforblockheads.tag;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

public class ModBlockTags {
    public static final TagKey<Block> COOKING_TABLES = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "cooking_tables"));
    public static final TagKey<Block> CHICKEN_SINKS = TagKey.create(Registries.BLOCK, id("chicken_sinks"));
    public static final TagKey<Block> KITCHEN_ITEM_PROVIDERS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "kitchen_item_providers"));
    public static final TagKey<Block> KITCHEN_CONNECTORS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "kitchen_connectors"));
}
