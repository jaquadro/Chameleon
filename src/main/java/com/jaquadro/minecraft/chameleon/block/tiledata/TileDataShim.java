package com.jaquadro.minecraft.chameleon.block.tiledata;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class TileDataShim implements INBTSerializable<NBTTagCompound>
{
    public abstract void readFromNBT (NBTTagCompound tag);

    public abstract NBTTagCompound writeToNBT (NBTTagCompound tag);

    @Override
    public NBTTagCompound serializeNBT () {
        NBTTagCompound tag = new NBTTagCompound();
        return writeToNBT(tag);
    }

    @Override
    public void deserializeNBT (NBTTagCompound nbt) {
        readFromNBT(nbt);
    }
}
