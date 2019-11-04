package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CookingForBlockheads.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSprites {

    public static final TextureAtlasSprite[] ovenToolIcons = new TextureAtlasSprite[4];

    @SubscribeEvent
    public static void registerIconsPre(TextureStitchEvent.Pre event) {
        event.addSprite(new ResourceLocation(CookingForBlockheads.MOD_ID, "item/slot_bakeware"));
        event.addSprite(new ResourceLocation(CookingForBlockheads.MOD_ID, "item/slot_pot"));
        event.addSprite(new ResourceLocation(CookingForBlockheads.MOD_ID, "item/slot_saucepan"));
        event.addSprite(new ResourceLocation(CookingForBlockheads.MOD_ID, "item/slot_skillet"));
        event.addSprite(new ResourceLocation(CookingForBlockheads.MOD_ID, "block/milk"));
    }

    @SubscribeEvent
    public static void registerIconsPost(TextureStitchEvent.Post event) {
        ovenToolIcons[0] = event.getMap().getSprite(new ResourceLocation(CookingForBlockheads.MOD_ID, "item/slot_bakeware"));
        ovenToolIcons[1] = event.getMap().getSprite(new ResourceLocation(CookingForBlockheads.MOD_ID, "item/slot_pot"));
        ovenToolIcons[2] = event.getMap().getSprite(new ResourceLocation(CookingForBlockheads.MOD_ID, "item/slot_saucepan"));
        ovenToolIcons[3] = event.getMap().getSprite(new ResourceLocation(CookingForBlockheads.MOD_ID, "item/slot_skillet"));
    }

}
