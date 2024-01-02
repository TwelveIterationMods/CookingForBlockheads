package net.blay09.mods.cookingforblockheads.tag;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {
    public static final TagKey<Item> DYEABLE = TagKey.create(Registries.ITEM, new ResourceLocation(CookingForBlockheads.MOD_ID, "dyeable"));
}
