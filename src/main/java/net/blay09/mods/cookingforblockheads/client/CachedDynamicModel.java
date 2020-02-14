package net.blay09.mods.cookingforblockheads.client;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.TransformationMatrix;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.SimpleModelTransform;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;

public class CachedDynamicModel implements IBakedModel {

    private final Map<String, IBakedModel> cache = Maps.newHashMap();

    private final ModelBakery modelBakery;
    private final Function<BlockState, IUnbakedModel> baseModelFunction;
    private final List<Pair<Predicate<BlockState>, IBakedModel>> parts;
    private final Function<BlockState, ImmutableMap<String, String>> textureMapFunction;
    private final ResourceLocation location;

    private TextureAtlasSprite particleTexture;

    public CachedDynamicModel(ModelBakery modelBakery, Function<BlockState, IUnbakedModel> baseModelFunction, @Nullable List<Pair<Predicate<BlockState>, IBakedModel>> parts, @Nullable Function<BlockState, ImmutableMap<String, String>> textureMapFunction, ResourceLocation location) {
        this.modelBakery = modelBakery;
        this.baseModelFunction = baseModelFunction;
        this.parts = parts;
        this.textureMapFunction = textureMapFunction;
        this.location = location;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
        return getQuads(state, side, rand, EmptyModelData.INSTANCE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        if (state != null) {
            String stateString = state.toString();
            IBakedModel bakedModel = cache.get(stateString);
            if (bakedModel == null) {
                Matrix4f transform = new Matrix4f();
                if (state.has(BlockKitchen.LOWERED) && state.get(BlockKitchen.LOWERED)) {
                    transform.translate(new Vector3f(0, -0.05f, 0f));
                }

                IUnbakedModel baseModel = baseModelFunction.apply(state);
                IUnbakedModel retexturedBaseModel = textureMapFunction != null ? retexture(baseModel, textureMapFunction.apply(state)) : baseModel;
                SimpleModelTransform modelTransform = new SimpleModelTransform(new TransformationMatrix(transform));
                bakedModel = retexturedBaseModel.bakeModel(modelBakery, ModelLoader.defaultTextureGetter(), modelTransform, location);
                cache.put(stateString, bakedModel);

                if (particleTexture == null && bakedModel != null) {
                    particleTexture = bakedModel.getParticleTexture(EmptyModelData.INSTANCE);
                }
            }

            return bakedModel != null ? bakedModel.getQuads(state, side, rand, EmptyModelData.INSTANCE) : Collections.emptyList();
        }

        return Collections.emptyList();
    }

    private IUnbakedModel retexture(IUnbakedModel baseModel, ImmutableMap<String, String> apply) {
        return baseModel; // TODO
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean func_230044_c_() {
        return false; // TODO ??
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return particleTexture != null ? particleTexture : ModelLoader.White.instance();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.EMPTY;
    }

}
