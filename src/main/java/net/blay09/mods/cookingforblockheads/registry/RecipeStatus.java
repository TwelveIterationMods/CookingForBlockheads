package net.blay09.mods.cookingforblockheads.registry;

public enum RecipeStatus {
	MISSING_INGREDIENTS,
	MISSING_TOOLS,
	AVAILABLE;

	private static RecipeStatus[] values = values();

	public static RecipeStatus fromId(int i) {
		return values[i];
	}
}
