package com.jaquadro.minecraft.chameleon.model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IFlexibleBakedModel;

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
    public List<BakedQuad> getFaceQuads (EnumFacing facing) {
        List<BakedQuad> inner = super.getFaceQuads(facing);
        if (inner == null || inner.size() == 0)
            return model.getFaceQuads(facing);

        List<BakedQuad> combined = new ArrayList<BakedQuad>();
        combined.addAll(model.getFaceQuads(facing));
        combined.addAll(inner);

        return combined;
    }

    @Override
    public List<BakedQuad> getGeneralQuads () {
        List<BakedQuad> inner = super.getGeneralQuads();
        if (inner == null || inner.size() == 0)
            return model.getGeneralQuads();

        List<BakedQuad> combined = new ArrayList<BakedQuad>();
        combined.addAll(model.getGeneralQuads());
        combined.addAll(inner);

        return combined;
    }

    @Override
    public TextureAtlasSprite getParticleTexture () {
        return model.getParticleTexture();
    }

    @Override
    public VertexFormat getFormat () {
        if (model instanceof IFlexibleBakedModel)
            return ((IFlexibleBakedModel) model).getFormat();

        return super.getFormat();
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
