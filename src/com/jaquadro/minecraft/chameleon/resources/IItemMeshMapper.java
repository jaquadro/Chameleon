package com.jaquadro.minecraft.chameleon.resources;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface IItemMeshMapper
{
    List<Pair<ItemStack, ModelResourceLocation>> getMeshMappings ();
    //ItemMeshDefinition getMeshResolver ();
}
