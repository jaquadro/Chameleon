package com.jaquadro.minecraft.chameleon.resources.register;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.IBakedModel;

import java.util.List;

public interface IBlockModelRegister
{
    List<IBlockState> getBlockStates ();

    IBakedModel getModel (IBlockState state);
}
