package net.blay09.mods.cookingforblockheads.platform.attachment;

import net.blay09.mods.balm.platform.attachment.BalmDataAttachmentTypeRegistrar;
import net.blay09.mods.balm.platform.attachment.DataAttachmentLookup;
import net.blay09.mods.cookingforblockheads.Preferences;

public class ModDataAttachments {

    public static DataAttachmentLookup<Preferences> preferences;

    public static void initialize(BalmDataAttachmentTypeRegistrar registrar) {
        preferences = registrar.<Preferences>register("preferences", builder -> builder
                .persistent(Preferences.CODEC)
                .initializer(() -> Preferences.DEFAULT)
                .copyOnDeath()).asLookup();
    }
}
