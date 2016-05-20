package net.blay09.mods.cookingforblockheads;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nullable;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {}

	public void init(FMLInitializationEvent event) {}

	public void addScheduledTask(Runnable runnable) {
		FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(runnable);
	}

	@Nullable
	public IAnimationStateMachine loadAnimationStateMachine(ResourceLocation location, ImmutableMap<String, ITimeValue> parameters) {
		return null;
	}

}
