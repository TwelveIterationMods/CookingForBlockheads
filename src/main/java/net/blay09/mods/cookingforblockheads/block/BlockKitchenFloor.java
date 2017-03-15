package net.blay09.mods.cookingforblockheads.block;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockKitchenFloor extends Block {

	public BlockKitchenFloor() {
		super(Material.ROCK);

		setRegistryName(CookingForBlockheads.MOD_ID, "kitchen_floor");
		setUnlocalizedName(getRegistryNameString());
		setSoundType(SoundType.STONE);
		setHardness(5f);
		setResistance(10f);
		setCreativeTab(CookingForBlockheads.creativeTab);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		tooltip.add(TextFormatting.YELLOW + I18n.format("tooltip." + CookingForBlockheads.MOD_ID + ":multiblock_kitchen"));
		for (String s : I18n.format("tooltip." + getRegistryName() + ".description").split("\\\\n")) {
			tooltip.add(TextFormatting.GRAY + s);
		}
	}

	@SideOnly(Side.CLIENT)
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryNameString(), "inventory"));
	}

	public String getRegistryNameString() {
		//noinspection ConstantConditions
		return getRegistryName().toString();
	}

}
