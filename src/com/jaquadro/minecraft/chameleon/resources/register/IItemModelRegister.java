package com.jaquadro.minecraft.chameleon.resources.register;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IItemModelRegister
{
    Item getItem ();

    List<ItemStack> getItemVariants ();

    IBakedModel getModel (ItemStack stack);
}
