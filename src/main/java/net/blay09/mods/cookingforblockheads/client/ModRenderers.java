package net.blay09.mods.cookingforblockheads.client;

import net.blay09.mods.cookingforblockheads.block.ModBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

public class ModRenderers {

    public static void register() {
        RenderTypeLookup.setRenderLayer(ModBlocks.oven, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.milkJar, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.cowJar, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.fridge, it -> it == RenderType.cutout() || it == RenderType.translucent());
    }

}
