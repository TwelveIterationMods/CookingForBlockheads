package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

public class ModRenderers {

    public static void register() {
        RenderTypeLookup.setRenderLayer(ModBlocks.oven, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.milkJar, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.cowJar, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.fridge, RenderType.getCutout());
    }

}
