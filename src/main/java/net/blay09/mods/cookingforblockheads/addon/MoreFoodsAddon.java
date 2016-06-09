package net.blay09.mods.cookingforblockheads.addon;

import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.api.ToastHandler;
import net.blay09.mods.cookingforblockheads.api.event.FoodRegistryInitEvent;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MoreFoodsAddon {

	private static final String MOD_ID = "morefood";

	private static final String[] ADDITIONAL_RECIPES = new String[] {
			"item_salt",
			"item_bacon_uncooked",
			"item_joint_pork",
			"item_tomato_chopped",
			"item_potato_peeled_chipped",
			"item_medium_dough",
			"item_stock",
			"item_cheese_slice",
			"item_uncooked_pizza",
			"item_yorkshire_pudding_pot",
			"item_uncooked_french_toast",
			"item_dough_medium",
			"item_dough_small",
			"item_potato_peeled",
			"item_french_toast_uncooked",
			"item_dumpling_uncooked",
			"item_cooked_french_toast_frying_pan",
			"item_batter",
			"item_french_toast_cooked_frying_pan",
			"item_minced_meat_cooked",
			"item_fish_battered_uncooked",
			"item_joint_beef",
			"item_minced_meat_uncooked",
			"item_intestines",
			"item_pot_salt",
			"item_sauce_tomato",
			"item_bucket_cheese",
			"item_stock_mix_uncooked",
			"item_pot_water_potato_boiled",
			"item_egg_uncooked_frying_pan",
			"item_joint_bacon",
			"item_pot_water_potato_peeled",
			"item_flour",
			"item_mushroom_fried_frying_pan",
			"item_mushroom_uncooked_frying_pan",
			"item_small_dough",
			"item_suet",
			"item_beef_patty_cooked",
			"item_onion_chopped",
			"item_pizza_uncooked",
			"item_sausage_uncooked",
			"item_hashbrown_uncooked",
			"item_dough",
			"item_pot_water",
			"item_beef_patty_uncooked",
			"item_chicken_bone",
			"item_fried_egg_frying_pan",
			"item_salt"
	};

	private static final String[] TOOLS = new String[] {
			"item_juicer",
			"item_mixing_bowl",
			"item_knife",
			"item_frying_pan",
			"item_pot",
			"item_mincer",
			"item_pestle_mortar"
	};

	public static final String BREAD_ITEM = "item_bread_slice";
	public static final String TOAST_ITEM = "item_toast";

	public MoreFoodsAddon() {
		for(String toolName : TOOLS) {
			Item toolItem = Item.REGISTRY.getObject(new ResourceLocation(MOD_ID, toolName));
			if(toolItem != null) {
				CookingForBlockheadsAPI.addToolItem(new ItemStack(toolItem));
			}
		}

		final Item breadItem = Item.REGISTRY.getObject(new ResourceLocation(MOD_ID, BREAD_ITEM));
		final Item toastItem = Item.REGISTRY.getObject(new ResourceLocation(MOD_ID, TOAST_ITEM));
		if(breadItem != null && toastItem != null) {
			CookingForBlockheadsAPI.addToastHandler(new ItemStack(breadItem), new ToastHandler() {
				@Override
				public ItemStack getToasterOutput(ItemStack itemStack) {
					return new ItemStack(toastItem);
				}
			});
		}

		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onFoodRegistryInit(FoodRegistryInitEvent event) {
		for(String s : ADDITIONAL_RECIPES) {
			Item item = Item.REGISTRY.getObject(new ResourceLocation(MOD_ID, s));
			if(item != null) {
				event.registerNonFoodRecipe(new ItemStack(item));
			}
		}
	}

}
