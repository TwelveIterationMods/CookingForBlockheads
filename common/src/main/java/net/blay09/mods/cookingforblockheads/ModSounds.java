package net.blay09.mods.cookingforblockheads;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {

    public static DeferredObject<SoundEvent> fridgeOpen;
    public static DeferredObject<SoundEvent> fridgeClose;
    public static DeferredObject<SoundEvent> ovenOpen;
    public static DeferredObject<SoundEvent> ovenClose;
    public static DeferredObject<SoundEvent> toasterStart;
    public static DeferredObject<SoundEvent> toasterStop;

    public static void initialize(BalmSounds sounds) {
        fridgeOpen = sounds.register(id("fridge_open"));
        fridgeClose = sounds.register(id("fridge_close"));
        ovenOpen = sounds.register(id("oven_open"));
        ovenClose = sounds.register(id("oven_close"));
        toasterStart = sounds.register(id("toaster_start"));
        toasterStop = sounds.register(id("toaster_stop"));
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation(CookingForBlockheads.MOD_ID, name);
    }

}
