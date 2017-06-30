package net.blay09.mods.cookingforblockheads;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModSounds {

	public static SoundEvent fridgeOpen;
	public static SoundEvent fridgeClose;
	public static SoundEvent ovenOpen;
	public static SoundEvent ovenClose;
	public static SoundEvent toasterStart;
	public static SoundEvent toasterStop;

	public static void register(IForgeRegistry<SoundEvent> registry) {
		registry.registerAll(
				fridgeOpen = newSoundEvent("fridge_open"),
				fridgeClose = newSoundEvent("fridge_close"),
				ovenOpen = newSoundEvent("oven_open"),
				ovenClose = newSoundEvent("oven_close"),
				toasterStart = newSoundEvent("toaster_start"),
				toasterStop = newSoundEvent("toaster_stop")
		);
	}

	private static SoundEvent newSoundEvent(String name) {
		ResourceLocation location = new ResourceLocation(CookingForBlockheads.MOD_ID, name);
		SoundEvent sound = new SoundEvent(location);
		sound.setRegistryName(location);
		return sound;
	}

}
