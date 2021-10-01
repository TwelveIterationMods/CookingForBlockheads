package net.blay09.mods.cookingforblockheads.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class TextUtils {
    public static Component coloredTextComponent(String i18n, ChatFormatting color) {
        TranslatableComponent textComponent = new TranslatableComponent(i18n);
        textComponent.withStyle(color);
        return textComponent;
    }
}
