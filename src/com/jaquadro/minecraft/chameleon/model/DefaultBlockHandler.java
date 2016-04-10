package com.jaquadro.minecraft.chameleon.model;

import com.jaquadro.minecraft.chameleon.Chameleon;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

public abstract class DefaultBlockHandler implements ISmartBlockModel
{
    private static final List<BakedQuad> empty = new ArrayList<BakedQuad>(0);

    private boolean error;
    private TextureAtlasSprite iconParticle;

    public DefaultBlockHandler (TextureAtlasSprite iconParticle) {
        this.iconParticle = iconParticle;
    }

    @Override
    public List<BakedQuad> getFaceQuads (EnumFacing facing) {
        logError();
        return empty;
    }

    @Override
    public List<BakedQuad> getGeneralQuads () {
        logError();
        return empty;
    }

    @Override
    public boolean isAmbientOcclusion () {
        logError();
        return true;
    }

    @Override
    public boolean isGui3d () {
        logError();
        return true;
    }

    @Override
    public boolean isBuiltInRenderer () {
        logError();
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture () {
        return iconParticle;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms () {
        logError();
        return ItemCameraTransforms.DEFAULT;
    }

    private void logError () {
        if (!error) {
            String name = this.getClass().getName() + " (" + toString() + ")";
            FMLLog.log(Chameleon.MOD_ID, Level.ERROR, "Default implementation called for ISmartBlockModel on " + name);
            error = true;
        }
    }
}
