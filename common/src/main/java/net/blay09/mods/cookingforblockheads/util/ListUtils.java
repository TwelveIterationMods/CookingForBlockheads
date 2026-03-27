package net.blay09.mods.cookingforblockheads.util;

import net.minecraft.core.NonNullList;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class ListUtils {
    public static <T> NonNullList<T> nonNullListOf(@Nullable List<T> list, T defaultValue) {
        if (list == null) {
            return null;
        }

        final var result = NonNullList.withSize(list.size(), defaultValue);
        for (int i = 0; i < list.size(); i++) {
            T value = list.get(i);
            result.set(i, value != null ? value : defaultValue);
        }
        return result;
    }
}
