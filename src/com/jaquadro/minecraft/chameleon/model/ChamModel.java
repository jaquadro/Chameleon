package com.jaquadro.minecraft.chameleon.model;

import com.jaquadro.minecraft.chameleon.Chameleon;
import com.jaquadro.minecraft.chameleon.render.ChamRender;
import com.jaquadro.minecraft.chameleon.render.ChamRenderManager;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.MinecraftForgeClient;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public abstract class ChamModel extends BlockModel
{
    private static final List<BakedQuad> EMPTY = new ArrayList<BakedQuad>(0);

    private List<BakedQuad>[] solidCache;
    private List<BakedQuad>[] cutoutCache;
    private List<BakedQuad>[] mippedCache;
    private List<BakedQuad>[] transCache;

    @SuppressWarnings("unchecked")
    public ChamModel (IBlockState state, boolean mergeLayers, Object... args) {
        if (state == null)
            return;

        if (!mergeLayers) {
            Block block = state.getBlock();
            if (block.canRenderInLayer(state, BlockRenderLayer.SOLID))
                solidCache = (List[]) Array.newInstance(ArrayList.class, 7);
            if (block.canRenderInLayer(state, BlockRenderLayer.CUTOUT))
                cutoutCache = (List[]) Array.newInstance(ArrayList.class, 7);
            if (block.canRenderInLayer(state, BlockRenderLayer.CUTOUT_MIPPED))
                mippedCache = (List[]) Array.newInstance(ArrayList.class, 7);
            if (block.canRenderInLayer(state, BlockRenderLayer.TRANSLUCENT))
                transCache = (List[]) Array.newInstance(ArrayList.class, 7);
        }
        else
            solidCache = (List[]) Array.newInstance(ArrayList.class, 7);

        for (int i = 0; i < 7; i++) {
            if (solidCache != null)
                solidCache[i] = EMPTY;
            if (cutoutCache != null)
                cutoutCache[i] = EMPTY;
            if (mippedCache != null)
                mippedCache[i] = EMPTY;
            if (transCache != null)
                transCache[i] = EMPTY;
        }

        ChamRender renderer = ChamRenderManager.instance.getRenderer(null);
        renderStart(renderer, state, args);

        if (!mergeLayers) {
            if (solidCache != null) {
                renderer.startBaking(getFormat());
                renderSolidLayer(renderer, state, args);
                renderer.stopBaking();
                updateCache(solidCache, renderer);
            }
            if (cutoutCache != null) {
                renderer.startBaking(getFormat());
                renderCutoutLayer(renderer, state, args);
                renderer.stopBaking();
                updateCache(cutoutCache, renderer);
            }
            if (mippedCache != null) {
                renderer.startBaking(getFormat());
                renderMippedLayer(renderer, state, args);
                renderer.stopBaking();
                updateCache(mippedCache, renderer);
            }
            if (transCache != null) {
                renderer.startBaking(getFormat());
                renderTransLayer(renderer, state, args);
                renderer.stopBaking();
                updateCache(transCache, renderer);
            }
        }
        else {
            renderer.startBaking(getFormat());
            renderSolidLayer(renderer, state, args);
            renderCutoutLayer(renderer, state, args);
            renderMippedLayer(renderer, state, args);
            renderTransLayer(renderer, state, args);
            renderer.stopBaking();
            updateCache(solidCache, renderer);
        }

        renderEnd(renderer, state, args);
        ChamRenderManager.instance.releaseRenderer(renderer);
    }

    @Override
    public List<BakedQuad> getQuads (IBlockState state, EnumFacing facing, long rand) {
        int index = (facing != null) ? facing.getIndex() : 6;

        BlockRenderLayer renderLayer = MinecraftForgeClient.getRenderLayer();
        if (renderLayer == null)
            return (solidCache != null) ? solidCache[index] : EMPTY;

        switch (renderLayer) {
            case SOLID:
                return (solidCache != null) ? solidCache[index] : EMPTY;
            case CUTOUT:
                return (cutoutCache != null) ? cutoutCache[index] : EMPTY;
            case CUTOUT_MIPPED:
                return (mippedCache != null) ? mippedCache[index] : EMPTY;
            case TRANSLUCENT:
                return (transCache != null) ? transCache[index] : EMPTY;
            default:
                return EMPTY;
        }
    }

    protected void renderStart (ChamRender renderer, IBlockState state, Object... args) { }

    protected void renderEnd (ChamRender renderer, IBlockState state, Object... args) { }

    protected void renderSolidLayer (ChamRender renderer, IBlockState state, Object... args) { }

    protected void renderCutoutLayer (ChamRender renderer, IBlockState state, Object... args) { }

    protected void renderMippedLayer (ChamRender renderer, IBlockState state, Object... args) { }

    protected void renderTransLayer (ChamRender renderer, IBlockState state, Object... args) { }

    private void updateCache (List<BakedQuad>[] cache, ChamRender renderer) {
        cache[6] = renderer.takeBakedQuads(null);
        for (EnumFacing facing : EnumFacing.VALUES)
            cache[facing.getIndex()] = renderer.takeBakedQuads(facing);
    }

    public static class NullModel extends ChamModel {

        public NullModel () {
            super(null, true);
        }

        @Override
        public TextureAtlasSprite getParticleTexture () {
            return Chameleon.instance.iconRegistry.getIcon(TextureMap.LOCATION_MISSING_TEXTURE);
        }
    }
}
