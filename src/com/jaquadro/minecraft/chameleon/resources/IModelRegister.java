package com.jaquadro.minecraft.chameleon.resources;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public interface IModelRegister
{
    List<IBlockState> getBlockStates ();

    List<ResourceLocation> getTextureResources ();

    IBakedModel getModel (IBlockState state);
}
