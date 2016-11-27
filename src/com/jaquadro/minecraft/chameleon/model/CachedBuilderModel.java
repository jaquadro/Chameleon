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
    private ProxyBuilderModel model;

    public CachedBuilderModel (final ProxyBuilderModel model) {
        this.model = model;
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
        try {
            IBakedModel cmodel = modelCache.get(new KeyedBlockState(state, model.getKey(state)));
            return cmodel.getQuads(state, side, rand);
        }
        catch (ExecutionException e) {
            return ImmutableList.of();
        }
    }

    @Override
    public boolean isAmbientOcclusion () {
        return model.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d () {
        return model.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer () {
        return model.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture () {
        return model.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms () {
        return model.getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides () {
        return model.getOverrides();
    }
}
