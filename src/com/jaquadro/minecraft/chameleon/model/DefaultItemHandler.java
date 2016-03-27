package com.jaquadro.minecraft.chameleon.model;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartItemModel;

import java.util.List;

public abstract class DefaultItemHandler implements ISmartItemModel
{
    private TextureAtlasSprite iconParticle;

    public DefaultItemHandler (TextureAtlasSprite iconParticle) {
        this.iconParticle = iconParticle;
    }

    @Override
    public List<BakedQuad> getFaceQuads (EnumFacing facing) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BakedQuad> getGeneralQuads () {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAmbientOcclusion () {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isGui3d () {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isBuiltInRenderer () {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextureAtlasSprite getParticleTexture () {
        return iconParticle;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms () {
        throw new UnsupportedOperationException();
    }
}
