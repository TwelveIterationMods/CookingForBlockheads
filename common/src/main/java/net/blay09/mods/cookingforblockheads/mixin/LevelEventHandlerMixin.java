package net.blay09.mods.cookingforblockheads.mixin;

import net.blay09.mods.cookingforblockheads.block.entity.CowJarBlockEntity;
import net.blay09.mods.cookingforblockheads.block.entity.ChickenSinkBlockEntity;
import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelEventHandler.class)
public class LevelEventHandlerMixin {

    @Inject(method = "notifyNearbyEntities(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Z)V", at = @At("HEAD"))
    private void notifyNearbyEntities(Level level, BlockPos pos, boolean playing, CallbackInfo ci) {
        int radius = 3;
        final var mutablePos = pos.mutable();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    mutablePos.setWithOffset(pos, x, y, z);
                    final var blockEntity = level.getBlockEntity(mutablePos);
                    if (blockEntity instanceof CowJarBlockEntity cowJar) {
                        cowJar.setRecordPlayingNearby(pos, playing);
                    } else if (blockEntity instanceof ChickenSinkBlockEntity chickenSink) {
                        chickenSink.setRecordPlayingNearby(pos, playing);
                    }
                }
            }
        }
    }
}
