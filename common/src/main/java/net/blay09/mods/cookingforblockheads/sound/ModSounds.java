package net.blay09.mods.cookingforblockheads.sound;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {

    public static Holder<SoundEvent> fridgeOpen;
    public static Holder<SoundEvent> fridgeClose;
    public static Holder<SoundEvent> ovenOpen;
    public static Holder<SoundEvent> ovenClose;
    public static Holder<SoundEvent> toasterStart;
    public static Holder<SoundEvent> toasterStop;

    public static void initialize(BalmRegistrar.Scoped<SoundEvent> sounds) {
        fridgeOpen = sounds.register("fridge_open", SoundEvent::createVariableRangeEvent).asHolder();
        fridgeClose = sounds.register("fridge_close", SoundEvent::createVariableRangeEvent).asHolder();
        ovenOpen = sounds.register("oven_open", SoundEvent::createVariableRangeEvent).asHolder();
        ovenClose = sounds.register("oven_close", SoundEvent::createVariableRangeEvent).asHolder();
        toasterStart = sounds.register("toaster_start", SoundEvent::createVariableRangeEvent).asHolder();
        toasterStop = sounds.register("toaster_stop", SoundEvent::createVariableRangeEvent).asHolder();
    }

}
