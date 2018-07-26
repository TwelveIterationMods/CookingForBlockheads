package net.blay09.mods.cookingforblockheads.client;

import com.google.common.collect.Maps;
import net.blay09.mods.cookingforblockheads.block.BlockKitchen;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.model.TRSRTransformation;

import javax.annotation.Nullable;
import javax.vecmath.Vector3f;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LowerableFacingOnDemandModel implements IBakedModel {

    private final Map<String, IBakedModel> cache = Maps.newHashMap();

    private IModel baseModel;
    private TextureAtlasSprite particleTexture;

    public LowerableFacingOnDemandModel(IModel baseModel) {
        this.baseModel = baseModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if (state != null) {
            String stateString = state.toString();
            IBakedModel bakedModel = cache.get(stateString);
            if (bakedModel == null) {
                TRSRTransformation transform = TRSRTransformation.from(state.getValue(BlockKitchen.FACING));
                if (state.getValue(BlockKitchen.LOWERED)) {
                    transform = transform.compose(new TRSRTransformation(new Vector3f(0f, -0.05f, 0f), null, null, null));
                }

                bakedModel = baseModel.bake(transform, DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());
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
        return ItemOverrideList.NONE;
    }

}
