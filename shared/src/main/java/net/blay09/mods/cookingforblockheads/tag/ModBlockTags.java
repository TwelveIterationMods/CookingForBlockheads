package net.blay09.mods.cookingforblockheads.tag;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
    public static final TagKey<Block> KITCHEN_ITEM_PROVIDERS = TagKey.create(Registries.BLOCK, new ResourceLocation(CookingForBlockheads.MOD_ID, "kitchen_item_providers"));
    public static final TagKey<Block> KITCHEN_CONNECTORS = TagKey.create(Registries.BLOCK, new ResourceLocation(CookingForBlockheads.MOD_ID, "kitchen_connectors"));
    public static final TagKey<Block> DYEABLE = TagKey.create(Registries.BLOCK, new ResourceLocation(CookingForBlockheads.MOD_ID, "dyeable"));
}
