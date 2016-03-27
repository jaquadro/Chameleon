package com.jaquadro.minecraft.chameleon.model;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Vector3f;

@SideOnly(Side.CLIENT)
public abstract class BlockModel implements IBakedModel
{
    private static final ItemTransformVec3f transformThirdPerson = new ItemTransformVec3f(new Vector3f(10, -45, 170), new Vector3f(0, 0.09375f, -0.078125f), new Vector3f(.375f, .375f, .375f));
    private static final ItemCameraTransforms transform = new ItemCameraTransforms(transformThirdPerson,
        ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT);

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
}
