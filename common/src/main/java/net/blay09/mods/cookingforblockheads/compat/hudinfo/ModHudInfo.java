package net.blay09.mods.cookingforblockheads.compat.hudinfo;

import net.blay09.mods.balm.platform.compatibility.hudinfo.BalmModSupportHudInfo;
import net.blay09.mods.cookingforblockheads.block.ModBlocks;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

public class ModHudInfo {
    public static void initialize(BalmModSupportHudInfo hudInfo) {
        hudInfo.registerBlockInfo(id("milk_jar"), ModBlocks.milkJar, new MilkJarBlockInfoProvider());
        hudInfo.registerBlockInfo(id("cow_jar"), ModBlocks.cowJar, new CowJarBlockInfoProvider());
        hudInfo.registerBlockInfo(id("toaster"), ModBlocks.toaster, new ToasterBlockInfoProvider());
        ModBlocks.ovens.forEach((color, oven) -> hudInfo.registerBlockInfo(id("oven"), oven, new OvenBlockInfoProvider()));
        ModBlocks.fridges.forEach((color, fridge) -> hudInfo.registerBlockInfo(id("fridge"), fridge, new FridgeBlockInfoProvider()));
        ModBlocks.sinks.forEach((color, sink) -> hudInfo.registerBlockInfo(id("sink"), sink, new SinkBlockInfoProvider()));

        hudInfo.registerGlobalBlockInfo(id("preservation_chamber"), new UpgradeablePreservationBlockInfoProvider());
    }
}
