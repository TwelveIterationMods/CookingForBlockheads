package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CookingForBlockheads.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTextures {

    public static final ResourceLocation[] ovenToolIcons = new ResourceLocation[] {
        new ResourceLocation(CookingForBlockheads.MOD_ID, "item/slot_bakeware"),
        new ResourceLocation(CookingForBlockheads.MOD_ID, "item/slot_pot"),
        new ResourceLocation(CookingForBlockheads.MOD_ID, "item/slot_saucepan"),
        new ResourceLocation(CookingForBlockheads.MOD_ID, "item/slot_skillet")
    };

    @SubscribeEvent
    public static void registerIconsPre(TextureStitchEvent.Pre event) {
        if (event.getMap().getBasePath().equals(PlayerContainer.LOCATION_BLOCKS_TEXTURE)) {
            for (ResourceLocation ovenToolIcon : ovenToolIcons) {
                event.addSprite(ovenToolIcon);
            }

            event.addSprite(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/milk"));
            event.addSprite(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/oven_front_active"));
            event.addSprite(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/oven_front_powered"));
            event.addSprite(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/oven_front_powered_active"));
        } else if (event.getMap().getBasePath().equals(Atlases.SIGN_ATLAS)) {
                event.addSprite(new ResourceLocation(CookingForBlockheads.MOD_ID, "entity/cow/cow"));
        }
    }

}
