package net.blay09.mods.cookingforblockheads.api;

public enum RecipeStatus {
	MISSING_INGREDIENTS,
	MISSING_TOOLS,
	AVAILABLE;

	private static final RecipeStatus[] values = values();

	public static RecipeStatus fromId(int i) {
		return values[i];
	}
}
