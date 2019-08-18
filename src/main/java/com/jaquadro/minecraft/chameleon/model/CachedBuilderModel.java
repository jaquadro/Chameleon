package com.jaquadro.minecraft.chameleon.model;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CachedBuilderModel implements IBakedModel
{
    LoadingCache<KeyedBlockState, IBakedModel> modelCache;
    private IBlockState baseState;
    private ProxyBuilderModel model;

    public CachedBuilderModel (final ProxyBuilderModel model) {
        this(model, null);
    }

    public CachedBuilderModel (final ProxyBuilderModel model, IBlockState baseState) {
        this.model = model;
        this.baseState = baseState;

        modelCache = CacheBuilder.newBuilder().build(new CacheLoader<KeyedBlockState, IBakedModel>()
        {
            @Override
            public IBakedModel load (KeyedBlockState key) throws Exception {
                return model.buildModel(key.getState());
            }
        });
    }

    @Override
    public List<BakedQuad> getQuads (@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return getModel(state).getQuads(state, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion () {
        return getModel().isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d () {
        return getModel().isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer () {
        return getModel().isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture () {
        return getModel().getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms () {
        return getModel().getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides () {
        return getModel().getOverrides();
    }

    private IBakedModel getModel () {
        return getModel(baseState);
    }

    private IBakedModel getModel (IBlockState state) {
        try {
            if (state == null)
                return model;

            return modelCache.get(new KeyedBlockState(state, model.getKey(state)));
        }
        catch (Exception e) {
            return model;
        }
    }
}
