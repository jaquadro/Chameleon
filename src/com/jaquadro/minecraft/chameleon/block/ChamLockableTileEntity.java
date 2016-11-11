package com.jaquadro.minecraft.chameleon.block;

import com.jaquadro.minecraft.chameleon.block.tiledata.LockableData;
import com.jaquadro.minecraft.chameleon.block.tiledata.TileDataShim;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

public abstract class ChamLockableTileEntity extends ChamInvTileEntity implements ILockableContainer
{
    private LockableData lockableData;

    @Override
    public void injectData (TileDataShim shim) {
        super.injectData(shim);
        if (shim instanceof LockableData)
            lockableData = (LockableData)shim;
    }

    @Override
    public boolean isLocked () {
        if (lockableData != null)
            return lockableData.isLocked();

        return false;
    }

    @Override
    public void setLockCode (LockCode code) {
        if (lockableData != null)
            lockableData.setLockCode(code);
    }

    @Override
    public LockCode getLockCode () {
        if (lockableData != null)
            return lockableData.getLockCode();

        return null;
    }
}
