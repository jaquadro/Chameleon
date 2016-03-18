package com.jaquadro.minecraft.chameleon.model;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class BlockModel implements IBakedModel
{
    @Override
    public boolean isAmbientOcclusion () {
        return true;
    }

    @Override
    public boolean isGui3d () {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer () {
        return false;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms () {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public ItemOverrideList getOverrides () {
        return ItemOverrideList.NONE;
    }
}
