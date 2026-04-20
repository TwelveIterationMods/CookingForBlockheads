package net.blay09.mods.cookingforblockheads.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

public class ModBlockTags {
    public static final TagKey<Block> COOKING_TABLES = TagKey.create(Registries.BLOCK, id("cooking_tables"));
    public static final TagKey<Block> COUNTERS = TagKey.create(Registries.BLOCK, id("counters"));
    public static final TagKey<Block> CABINETS = TagKey.create(Registries.BLOCK, id("cabinets"));
    public static final TagKey<Block> SINKS = TagKey.create(Registries.BLOCK, id("sinks"));
    public static final TagKey<Block> OVENS = TagKey.create(Registries.BLOCK, id("ovens"));
    public static final TagKey<Block> FRIDGES = TagKey.create(Registries.BLOCK, id("fridges"));
    public static final TagKey<Block> CHICKEN_SINKS = TagKey.create(Registries.BLOCK, id("chicken_sinks"));
    public static final TagKey<Block> KITCHEN_ITEM_PROVIDERS = TagKey.create(Registries.BLOCK, id("kitchen_item_providers"));
    public static final TagKey<Block> KITCHEN_CONNECTORS = TagKey.create(Registries.BLOCK, id("kitchen_connectors"));
}
