package com.jaquadro.minecraft.chameleon.model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

public class WrappedChamModel extends ChamModel
{
    protected IBakedModel model;

    public WrappedChamModel (IBakedModel model, IBlockState state, boolean mergeLayers, Object... args) {
        super(state, mergeLayers, args);
        this.model = model;
    }

    @Override
    public List<BakedQuad> getQuads (IBlockState state, EnumFacing facing, long rand) {
        List<BakedQuad> inner = super.getQuads(state, facing, rand);
        if (inner == null || inner.size() == 0)
            return model.getQuads(state, facing, rand);

        List<BakedQuad> combined = new ArrayList<BakedQuad>();
        combined.addAll(model.getQuads(state, facing, rand));
        combined.addAll(inner);

        return combined;
    }

    @Override
    public TextureAtlasSprite getParticleTexture () {
        return model.getParticleTexture();
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
        return false;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms () {
        return model.getItemCameraTransforms();
    }
}
