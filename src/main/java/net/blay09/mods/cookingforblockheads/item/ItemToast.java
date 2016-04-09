package net.blay09.mods.cookingforblockheads.item;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemFood;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToast extends ItemFood {

	public ItemToast() {
		super(7, 1.2f, false);
		setRegistryName(CookingForBlockheads.MOD_ID + ":toast");
		setUnlocalizedName(getRegistryName().toString());
		setCreativeTab(CookingForBlockheads.creativeTab);
	}

	@SideOnly(Side.CLIENT)
	public void registerModels(ItemModelMesher mesher) {
		mesher.register(this, 0, new ModelResourceLocation(CookingForBlockheads.MOD_ID + ":toast", "inventory"));
	}
}
