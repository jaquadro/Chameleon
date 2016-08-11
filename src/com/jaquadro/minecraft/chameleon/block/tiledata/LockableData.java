package com.jaquadro.minecraft.chameleon.block.tiledata;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.LockCode;

public class LockableData extends TileDataShim
{
    private LockCode code = LockCode.EMPTY_CODE;

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        code = LockCode.fromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT (NBTTagCompound tag) {
        if (code != null)
            code.toNBT(tag);

        return tag;
    }

    public boolean isLocked ()
    {
        return this.code != null && !this.code.isEmpty();
    }

    public LockCode getLockCode ()
    {
        return this.code;
    }

    public void setLockCode (LockCode code)
    {
        this.code = code;
    }
}
