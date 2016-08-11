package com.jaquadro.minecraft.chameleon.block.tiledata;

import net.minecraft.nbt.NBTTagCompound;

public abstract class TileDataShim
{
    public abstract void readFromNBT (NBTTagCompound tag);

    public abstract NBTTagCompound writeToNBT (NBTTagCompound tag);
}
