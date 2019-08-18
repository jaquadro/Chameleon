package com.jaquadro.minecraft.chameleon.model;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Vector3f;

@SideOnly(Side.CLIENT)
public abstract class BlockModel implements IBakedModel
{
    ItemTransformVec3f transformGui = new ItemTransformVec3f(new Vector3f(30, 225, 0), new Vector3f(0, 0, 0), new Vector3f(.625f, .625f, .625f));
    ItemTransformVec3f transformFirstRight = new ItemTransformVec3f(new Vector3f(0, 45, 0), new Vector3f(0, 0, 0), new Vector3f(.4f, .4f, .4f));
    ItemTransformVec3f transformFirstLeft = new ItemTransformVec3f(new Vector3f(0, 225, 0), new Vector3f(0, 0, 0), new Vector3f(.4f, .4f, .4f));
    ItemTransformVec3f transformThirdRight = new ItemTransformVec3f(new Vector3f(75, 45, 0), new Vector3f(0, .15f, 0), new Vector3f(.375f, .375f, .375f));
    ItemTransformVec3f transformThirdLeft = new ItemTransformVec3f(new Vector3f(75, 225, 0), new Vector3f(0, .15f, 0), new Vector3f(.375f, .375f, .375f));
    ItemTransformVec3f transformHead = new ItemTransformVec3f(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(.5f, .5f, .5f));
    ItemTransformVec3f transformFixed = new ItemTransformVec3f(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(.5f, .5f, .5f));
    ItemTransformVec3f transformGround = new ItemTransformVec3f(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(.25f, .25f, .25f));
    ItemCameraTransforms transform = new ItemCameraTransforms(transformThirdLeft, transformThirdRight,
        transformFirstLeft, transformFirstRight, transformHead, transformGui, transformGround, transformFixed);

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
        return transform;
    }

    @Override
    public ItemOverrideList getOverrides () {
        return ItemOverrideList.NONE;
    }

    public VertexFormat getFormat () {
        return DefaultVertexFormats.ITEM;
    }
}
