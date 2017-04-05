package net.blay09.mods.cookingforblockheads;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModSounds {

	public static SoundEvent fridgeOpen;
	public static SoundEvent fridgeClose;
	public static SoundEvent ovenOpen;
	public static SoundEvent ovenClose;
	public static SoundEvent toasterStart;
	public static SoundEvent toasterStop;

	public static void register() {
		fridgeOpen = registerSound("fridge_open");
		fridgeClose = registerSound("fridge_close");
		ovenOpen = registerSound("oven_open");
		ovenClose = registerSound("oven_close");
		toasterStart = registerSound("toaster_start");
		toasterStop = registerSound("toaster_stop");
	}

	private static SoundEvent registerSound(String name) {
		ResourceLocation location = new ResourceLocation(CookingForBlockheads.MOD_ID, name);
		SoundEvent sound = new SoundEvent(location);
		sound.setRegistryName(location);
		GameRegistry.register(sound);
		return sound;
	}
}
