package net.blay09.mods.cookingforblockheads.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class TextUtils {
    public static Component coloredTextComponent(String i18n, ChatFormatting color) {
        var textComponent = Component.translatable(i18n);
        textComponent.withStyle(color);
        return textComponent;
    }
}
