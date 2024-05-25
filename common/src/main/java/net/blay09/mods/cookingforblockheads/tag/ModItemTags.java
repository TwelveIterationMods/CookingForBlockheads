package net.blay09.mods.cookingforblockheads.tag;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {
    public static final TagKey<Item> IS_DYEABLE = TagKey.create(Registries.ITEM, new ResourceLocation(CookingForBlockheads.MOD_ID, "is_dyeable"));
    public static final TagKey<Item> COOKING_TABLES = TagKey.create(Registries.ITEM, new ResourceLocation(CookingForBlockheads.MOD_ID, "cooking_tables"));
    public static final TagKey<Item> DYED_COOKING_TABLES = TagKey.create(Registries.ITEM, new ResourceLocation(CookingForBlockheads.MOD_ID, "dyed_cooking_tables"));
    public static final TagKey<Item> OVENS = TagKey.create(Registries.ITEM, new ResourceLocation(CookingForBlockheads.MOD_ID, "ovens"));
    public static final TagKey<Item> DYED_OVENS = TagKey.create(Registries.ITEM, new ResourceLocation(CookingForBlockheads.MOD_ID, "dyed_ovens"));
    public static final TagKey<Item> CONNECTORS = TagKey.create(Registries.ITEM, new ResourceLocation(CookingForBlockheads.MOD_ID, "connectors"));
    public static final TagKey<Item> DYED_CONNECTORS = TagKey.create(Registries.ITEM, new ResourceLocation(CookingForBlockheads.MOD_ID, "dyed_connectors"));
    public static final TagKey<Item> UTENSILS = TagKey.create(Registries.ITEM, new ResourceLocation(CookingForBlockheads.MOD_ID, "utensils"));
    public static final TagKey<Item> FOODS = TagKey.create(Registries.ITEM, new ResourceLocation(CookingForBlockheads.MOD_ID, "foods"));
    public static final TagKey<Item> INGREDIENTS = TagKey.create(Registries.ITEM, new ResourceLocation(CookingForBlockheads.MOD_ID, "ingredients"));
    public static final TagKey<Item> MILK = TagKey.create(Registries.ITEM, new ResourceLocation(CookingForBlockheads.MOD_ID, "milk"));
    public static final TagKey<Item> WATER = TagKey.create(Registries.ITEM, new ResourceLocation(CookingForBlockheads.MOD_ID, "water"));
    public static final TagKey<Item> EXCLUDED = TagKey.create(Registries.ITEM, new ResourceLocation(CookingForBlockheads.MOD_ID, "excluded"));
}
