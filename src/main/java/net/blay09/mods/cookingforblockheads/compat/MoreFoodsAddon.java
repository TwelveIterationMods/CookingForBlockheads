package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.cookingforblockheads.api.CookingForBlockheadsAPI;
import net.blay09.mods.cookingforblockheads.api.ToastErrorHandler;
import net.blay09.mods.cookingforblockheads.api.ToastOutputHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;

public class MoreFoodsAddon extends SimpleAddon {

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
		super("morefood");

		addTool(TOOLS);
		addNonFoodRecipe(ADDITIONAL_RECIPES);

		final ItemStack breadItem = getModItemStack(BREAD_ITEM);
		final ItemStack toastItem = getModItemStack(TOAST_ITEM);
		if(breadItem != null && toastItem != null) {
			CookingForBlockheadsAPI.addToastHandler(breadItem, new ToastOutputHandler() {
				@Override
				public ItemStack getToasterOutput(ItemStack itemStack) {
					return toastItem;
				}
			});
		}

		CookingForBlockheadsAPI.addToastHandler(new ItemStack(Items.BREAD), new ToastErrorHandler() {
			@Override
			public ITextComponent getToasterHint(EntityPlayer player, ItemStack itemStack) {
				return new TextComponentTranslation("hint.cookingforblockheads:moreFoods.cutBreadFirst");
			}
		});

		MinecraftForge.EVENT_BUS.register(this);
	}

}
