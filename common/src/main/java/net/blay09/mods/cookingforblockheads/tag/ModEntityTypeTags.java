package net.blay09.mods.cookingforblockheads.tag;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class ModEntityTypeTags {
    public static final TagKey<EntityType<?>> COW = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(CookingForBlockheads.MOD_ID, "cows"));
}
