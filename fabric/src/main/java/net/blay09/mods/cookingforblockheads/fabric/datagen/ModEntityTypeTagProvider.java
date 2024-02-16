package net.blay09.mods.cookingforblockheads.fabric.datagen;

import net.blay09.mods.cookingforblockheads.tag.ModEntityTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagProvider extends FabricTagProvider<EntityType<?>> {
    public ModEntityTypeTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.ENTITY_TYPE, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        getOrCreateTagBuilder(ModEntityTypeTags.COW).add(EntityType.COW);
    }
}
