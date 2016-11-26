package com.jaquadro.minecraft.chameleon.model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

public abstract class ProxyBuilderModel implements IBakedModel
{
    private static final List<BakedQuad> EMPTY = new ArrayList<BakedQuad>(0);
    private static final List<Object> EMPTY_KEY = new ArrayList<Object>();

    private IBakedModel parent;
    private IBakedModel proxy;
    private TextureAtlasSprite iconParticle;

    public ProxyBuilderModel (TextureAtlasSprite iconParticle) {
        this.iconParticle = iconParticle;
    }

    public ProxyBuilderModel (IBakedModel parent) {
        this.parent = parent;
    }

    @Override
    public List<BakedQuad> getQuads (IBlockState state, EnumFacing side, long rand) {
        if (proxy == null)
            setProxy(state);

        if (proxy == null)
            return EMPTY;

        return proxy.getQuads(state, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion () {
        IBakedModel model = getActiveModel();
        return (model != null) ? model.isAmbientOcclusion() : true;
    }

    @Override
    public boolean isGui3d () {
        IBakedModel model = getActiveModel();
        return (model != null) ? model.isGui3d() : false;
    }

    @Override
    public boolean isBuiltInRenderer () {
        IBakedModel model = getActiveModel();
        return (model != null) ? model.isBuiltInRenderer() : false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture () {
        IBakedModel model = getActiveModel();
        return (model != null) ? model.getParticleTexture() : iconParticle;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms () {
        IBakedModel model = getActiveModel();
        return (model != null) ? model.getItemCameraTransforms() : ItemCameraTransforms.DEFAULT;
    }

    @Override
    public ItemOverrideList getOverrides () {
        IBakedModel model = getActiveModel();
        return (model != null) ? model.getOverrides() : ItemOverrideList.NONE;
    }

    public List<Object> getKey (IBlockState state) {
        return EMPTY_KEY;
    }

    protected abstract IBakedModel buildModel (IBlockState state, IBakedModel parent);

    public final IBakedModel buildModel (IBlockState state) {
        return this.buildModel(state, parent);
    }

    private void setProxy (IBlockState state) {
        if (state == null)
            proxy = parent;
        else
            proxy = buildModel(state, parent);
    }

    private IBakedModel getActiveModel () {
        return (proxy != null) ? proxy : parent;
    }
}
