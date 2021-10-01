package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class ModTextures {

    public static final ResourceLocation[] ovenToolIcons = new ResourceLocation[]{
            new ResourceLocation(CookingForBlockheads.MOD_ID, "item/slot_bakeware"),
            new ResourceLocation(CookingForBlockheads.MOD_ID, "item/slot_pot"),
            new ResourceLocation(CookingForBlockheads.MOD_ID, "item/slot_saucepan"),
            new ResourceLocation(CookingForBlockheads.MOD_ID, "item/slot_skillet")
    };

    public static void initialize(BalmTextures textures) {
        for (ResourceLocation ovenToolIcon : ovenToolIcons) {
            textures.addSprite(InventoryMenu.BLOCK_ATLAS, ovenToolIcon);
        }

        textures.addSprite(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/milk"));
        textures.addSprite(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/oven_front_active"));
        textures.addSprite(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/oven_front_powered"));
        textures.addSprite(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(CookingForBlockheads.MOD_ID, "block/oven_front_powered_active"));
    }

}
