package net.blay09.mods.cookingforblockheads.client;

import com.google.common.collect.Maps;
import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.util.math.vector.Vector3f;
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
    private final Function<BlockState, ResourceLocation> baseModelFunction;
    private final List<Pair<Predicate<BlockState>, IBakedModel>> parts;
    private final Function<BlockState, Map<String, String>> textureMapFunction;
    private final ResourceLocation location;

    private TextureAtlasSprite particleTexture;

    public CachedDynamicModel(ModelBakery modelBakery, Function<BlockState, ResourceLocation> baseModelFunction, @Nullable List<Pair<Predicate<BlockState>, IBakedModel>> parts, @Nullable Function<BlockState, Map<String, String>> textureMapFunction, ResourceLocation location) {
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
            Matrix4f transform = ModelRotation.X0_Y0.getRotation().getMatrix();
            String stateString = state.toString();
            IBakedModel bakedModel = cache.get(stateString);
            if (bakedModel == null) {
                if (state.func_235901_b_(BlockKitchen.LOWERED) && state.get(BlockKitchen.LOWERED)) { // has
                    transform.translate(new Vector3f(0, -0.05f, 0f));
                }

                if (state.func_235901_b_(BlockKitchen.FACING)) { // has
                    float angle = state.get(BlockKitchen.FACING).getHorizontalAngle();
                    transform.mul(new Quaternion(0f, 180 - angle, 0f, true));
                }

                ResourceLocation baseModelLocation = baseModelFunction.apply(state);
                IUnbakedModel retexturedBaseModel = textureMapFunction != null ? ModModels.retexture(modelBakery, baseModelLocation, textureMapFunction.apply(state)) : ModelLoader.instance().getModelOrMissing(baseModelLocation);
                IModelTransform modelTransform = new SimpleModelTransform(new TransformationMatrix(transform));
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
        return false; // flat diffuse lighting = false
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
