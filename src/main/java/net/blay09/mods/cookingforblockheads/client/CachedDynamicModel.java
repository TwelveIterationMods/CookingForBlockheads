package net.blay09.mods.cookingforblockheads.client;

import com.google.common.collect.Maps;
import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.BasicState;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.common.model.TRSRTransformation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Vector3f;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CachedDynamicModel implements IBakedModel {

    private final Map<String, IBakedModel> cache = Maps.newHashMap();

    private final ModelBakery modelBakery;
    private final IModel baseModel;

    private TextureAtlasSprite particleTexture;

    public CachedDynamicModel(ModelBakery modelBakery, IModel baseModel) {
        this.modelBakery = modelBakery;
        this.baseModel = baseModel;
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
                TRSRTransformation transform = TRSRTransformation.from(state.get(BlockKitchen.FACING));
                if (state.get(BlockKitchen.LOWERED)) {
                    transform = transform.compose(new TRSRTransformation(new Vector3f(0f, -0.05f, 0f), null, null, null));
                }

                BasicState modelState = new BasicState(transform, false);
                bakedModel = baseModel.bake(modelBakery, ModelLoader.defaultTextureGetter(), modelState, DefaultVertexFormats.BLOCK);
                cache.put(stateString, bakedModel);

                if (particleTexture == null) {
                    particleTexture = bakedModel.getParticleTexture();
                }
            }
            return bakedModel.getQuads(state, side, rand);
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
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return particleTexture != null ? particleTexture : ModelLoader.White.INSTANCE;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.EMPTY;
    }

}
