package net.blay09.mods.cookingforblockheads.compat;

import net.blay09.mods.cookingforblockheads.CookingForBlockheads;
import net.blay09.mods.cookingforblockheads.block.*;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

@WailaPlugin(CookingForBlockheads.MOD_ID)
public class FabricJadeCookingForBlockheadsPlugin implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new GenericComponentProvider(CookingForBlockheadsWailaUtils.MILK_JAR_UID, CookingForBlockheadsWailaUtils::appendMilkJarTooltip), MilkJarBlock.class);
        registration.registerBlockComponent(new GenericComponentProvider(CookingForBlockheadsWailaUtils.TOASTER_UID, CookingForBlockheadsWailaUtils::appendToasterTooltip), ToasterBlock.class);
        registration.registerBlockComponent(new GenericComponentProvider(CookingForBlockheadsWailaUtils.OVEN_UID, CookingForBlockheadsWailaUtils::appendOvenTooltip), OvenBlock.class);
        registration.registerBlockComponent(new GenericComponentProvider(CookingForBlockheadsWailaUtils.FRIDGE_UID, CookingForBlockheadsWailaUtils::appendFridgeTooltip), FridgeBlock.class);
        registration.registerBlockComponent(new GenericComponentProvider(CookingForBlockheadsWailaUtils.SINK_UID, CookingForBlockheadsWailaUtils::appendSinkTooltip), SinkBlock.class);
    }

    private static class GenericComponentProvider implements IBlockComponentProvider {
        private final ResourceLocation uid;
        private final CookingForBlockheadsWailaUtils.TooltipAppender appender;

        private GenericComponentProvider(ResourceLocation uid, CookingForBlockheadsWailaUtils.TooltipAppender appender) {
            this.uid = uid;
            this.appender = appender;
        }

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            appender.appendTooltip(accessor.getBlockEntity(), accessor.getPlayer(), tooltip::add);
        }

        @Override
        public ResourceLocation getUid() {
            return uid;
        }
    }
}
