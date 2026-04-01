package net.blay09.mods.cookingforblockheads.rules;

import net.blay09.mods.cookingforblockheads.block.entity.ChickenSinkBlockEntity;
import net.blay09.mods.shogi.Shogi;
import net.blay09.mods.shogi.ShogiValue;
import net.blay09.mods.shogi.scope.ShogiScope;

import java.util.List;

import static net.blay09.mods.cookingforblockheads.CookingForBlockheads.id;

public final class CookingForBlockheadsRules {

    public static final ShogiScope SCOPE = Shogi.scope(id("rules"), scope -> scope.setDefaultNamespaces(List.of("cookingforblockheads", "shogi")));

    public static final ShogiValue<ChickenSinkBlockEntity, Boolean> chickenSinkMayLayEgg = SCOPE.booleanValue(id("chicken_sink_may_lay_egg"), _ -> true);
    public static final ShogiValue<ChickenSinkBlockEntity, Integer> chickenSinkEggLayTime = SCOPE.intValue(id("chicken_sink_egg_lay_time"), ChickenSinkBlockEntity::defaultEggLayTime);

    private CookingForBlockheadsRules() {
    }
}
