package net.blay09.mods.cookingforblockheads.fabric.datagen;

import net.blay09.mods.cookingforblockheads.tag.ModEntityTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityTypes;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagProvider extends EntityTypeTagsProvider {
    public ModEntityTypeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        tag(ModEntityTypeTags.COW).add(EntityTypes.COW.builtInRegistryHolder().key());
    }
}
