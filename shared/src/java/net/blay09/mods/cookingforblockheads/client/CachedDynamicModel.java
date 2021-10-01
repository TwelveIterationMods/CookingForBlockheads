package net.blay09.mods.cookingforblockheads.client;

import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;
import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class CachedDynamicModel implements BakedModel {

    private final Map<String, BakedModel> cache = new HashMap<>();
    private final Map<ResourceLocation, BakedModel> baseModelCache = new HashMap<>();

    private final ModelBakery modelBakery;
    private final Function<BlockState, ResourceLocation> baseModelFunction;
    private final List<Pair<Predicate<BlockState>, BakedModel>> parts;
    private final Function<BlockState, Map<String, String>> textureMapFunction;
    private final ResourceLocation location;

    private TextureAtlasSprite particleTexture;

    public CachedDynamicModel(ModelBakery modelBakery, Function<BlockState, ResourceLocation> baseModelFunction, @Nullable List<Pair<Predicate<BlockState>, BakedModel>> parts, @Nullable Function<BlockState, Map<String, String>> textureMapFunction, ResourceLocation location) {
        this.modelBakery = modelBakery;
        this.baseModelFunction = baseModelFunction;
        this.parts = parts;
        this.textureMapFunction = textureMapFunction;
        this.location = location;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
        if (state != null) {
            Matrix4f transform = BlockModelRotation.X0_Y0.getRotation().getMatrix();
            String stateString = state.toString();
            BakedModel bakedModel = cache.get(stateString);
            if (bakedModel == null) {
                if (state.hasProperty(BlockKitchen.LOWERED) && state.getValue(BlockKitchen.LOWERED)) {
                    transform.translate(new Vector3f(0, -0.05f, 0f));
                }

                if (state.hasProperty(BlockKitchen.FACING)) {
                    float angle = state.getValue(BlockKitchen.FACING).toYRot();
                    transform.multiply(new Quaternion(0f, 180 - angle, 0f, true));
                }

                ModelState modelTransform = new SimpleModelState(new Transformation(transform));

                ResourceLocation baseModelLocation = baseModelFunction.apply(state);

                // If we're going to retexture, we need to ensure the base model has already been baked to prevent circular parent references in the retextured model
                if (textureMapFunction != null && !baseModelCache.containsKey(baseModelLocation)) {
                    final UnbakedModel baseModel = ModelLoader.instance().getModelOrMissing(baseModelLocation);
                    final BakedModel bakedBaseModel = baseModel.bake(modelBakery, ModelLoader.defaultTextureGetter(), modelTransform, baseModelLocation);
                    baseModelCache.put(baseModelLocation, bakedBaseModel);
                }

                UnbakedModel retexturedBaseModel = textureMapFunction != null ? ModModels.retexture(modelBakery, baseModelLocation, textureMapFunction.apply(state)) : ModelLoader.instance().getModelOrMissing(baseModelLocation);
                bakedModel = retexturedBaseModel.bake(modelBakery, ModelLoader.defaultTextureGetter(), modelTransform, location);
                cache.put(stateString, bakedModel);

                if (particleTexture == null && bakedModel != null) {
                    particleTexture = bakedModel.getParticleIcon();
                }
            }

            return bakedModel != null ? bakedModel.getQuads(state, side, rand) : Collections.emptyList();
        }

        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return particleTexture != null ? particleTexture : ModelLoader.White.instance();
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

}
