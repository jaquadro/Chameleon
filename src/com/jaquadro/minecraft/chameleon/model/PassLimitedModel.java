package com.jaquadro.minecraft.chameleon.model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.MinecraftForgeClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PassLimitedModel extends ProxyBuilderModel
{
    private static final List<BakedQuad> EMPTY = new ArrayList<BakedQuad>(0);

    private Set<BlockRenderLayer> renderLayers;

    public PassLimitedModel (IBakedModel parent, BlockRenderLayer renderLayer) {
        super(parent);

        this.renderLayers = new HashSet<BlockRenderLayer>();
        this.renderLayers.add(renderLayer);
    }

    public PassLimitedModel (IBakedModel parent, Set<BlockRenderLayer> renderLayers) {
        super(parent);

        this.renderLayers = new HashSet<BlockRenderLayer>(renderLayers);
    }

    @Override
    protected IBakedModel buildModel (IBlockState state, IBakedModel parent) {
        return parent;
    }

    @Override
    public List<BakedQuad> getQuads (IBlockState state, EnumFacing side, long rand) {
        BlockRenderLayer renderLayer = MinecraftForgeClient.getRenderLayer();

        if (renderLayers.contains(renderLayer))
            return super.getQuads(state, side, rand);

        return EMPTY;
    }
}
