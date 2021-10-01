package net.blay09.mods.cookingforblockheads.registry;

public enum FoodRecipeType {
    CRAFTING,
    SMELTING;

    private static final FoodRecipeType[] values = values();

    public static FoodRecipeType fromId(int i) {
        return values[i];
    }
}
