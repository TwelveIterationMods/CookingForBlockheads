package net.blay09.mods.cookingforblockheads.util;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class TextUtils {
    public static ITextComponent coloredTextComponent(String i18n, TextFormatting color) {
        TranslationTextComponent textComponent = new TranslationTextComponent(i18n);
        textComponent.func_240699_a_(color);
        return textComponent;
    }
}
