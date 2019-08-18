package com.jaquadro.minecraft.chameleon.resources;

import net.minecraft.util.IStringSerializable;

public interface IItemEnum extends IStringSerializable
{
    String getUnlocalizedName ();

    int getMetadata ();
}
