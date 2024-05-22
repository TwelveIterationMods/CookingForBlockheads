package net.blay09.mods.cookingforblockheads.compat;

import mcp.mobius.waila.api.*;
import net.blay09.mods.cookingforblockheads.block.*;

public class WTHITCookingForBlockheadsPlugin implements IWailaPlugin {
    @Override
    public void register(IRegistrar registrar) {
        registrar.addComponent(new GenericComponentProvider(CookingForBlockheadsWailaUtils::appendMilkJarTooltip), TooltipPosition.BODY, MilkJarBlock.class);
        registrar.addComponent(new GenericComponentProvider(CookingForBlockheadsWailaUtils::appendToasterTooltip), TooltipPosition.BODY, ToasterBlock.class);
        registrar.addComponent(new GenericComponentProvider(CookingForBlockheadsWailaUtils::appendOvenTooltip), TooltipPosition.BODY, OvenBlock.class);
        registrar.addComponent(new GenericComponentProvider(CookingForBlockheadsWailaUtils::appendFridgeTooltip), TooltipPosition.BODY, FridgeBlock.class);
        registrar.addComponent(new GenericComponentProvider(CookingForBlockheadsWailaUtils::appendSinkTooltip), TooltipPosition.BODY, SinkBlock.class);
    }

    private static class GenericComponentProvider implements IBlockComponentProvider {
        private final CookingForBlockheadsWailaUtils.TooltipAppender appender;

        private GenericComponentProvider(CookingForBlockheadsWailaUtils.TooltipAppender appender) {
            this.appender = appender;
        }

        @Override
        public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
            appender.appendTooltip(accessor.getBlockEntity(), accessor.getPlayer(), tooltip::addLine);
        }
    }
}
