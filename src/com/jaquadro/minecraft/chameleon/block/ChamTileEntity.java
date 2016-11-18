package com.jaquadro.minecraft.chameleon.block;

import com.jaquadro.minecraft.chameleon.Chameleon;
import com.jaquadro.minecraft.chameleon.block.tiledata.TileDataShim;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChamTileEntity extends TileEntity
{
    private NBTTagCompound failureSnapshot;
    private List<TileDataShim> fixedShims;
    private List<TileDataShim> portableShims;

    public boolean hasDataPacket () {
        return true;
    }

    public boolean dataPacketRequiresRenderUpdate () {
        return false;
    }

    public void injectData (TileDataShim shim) {
        if (fixedShims == null)
            fixedShims = new ArrayList<TileDataShim>();
        fixedShims.add(shim);
    }

    public void injectPortableData (TileDataShim shim) {
        if (portableShims == null)
            portableShims = new ArrayList<TileDataShim>();
        portableShims.add(shim);
    }

    @Override
    public final void readFromNBT (NBTTagCompound tag) {
        super.readFromNBT(tag);

        failureSnapshot = null;

        try {
            readFromFixedNBT(tag);
            readFromPortableNBT(tag);
        }
        catch (Throwable t) {
            trapLoadFailure(t, tag);
        }
    }

    @Override
    public final NBTTagCompound writeToNBT (NBTTagCompound tag) {
        super.writeToNBT(tag);

        if (failureSnapshot != null) {
            restoreLoadFailure(tag);
            return tag;
        }

        try {
            tag = writeToFixedNBT(tag);
            tag = writeToPortableNBT(tag);
        }
        catch (Throwable t) {
            FMLLog.log(Chameleon.MOD_ID, Level.ERROR, t, "Tile Save Failure.");
        }

        return tag;
    }

    public void readFromPortableNBT (NBTTagCompound tag) {
        if (portableShims != null) {
            for (TileDataShim shim : portableShims)
                shim.readFromNBT(tag);
        }
    }

    public NBTTagCompound writeToPortableNBT (NBTTagCompound tag) {
        if (portableShims != null) {
            for (TileDataShim shim : portableShims)
                tag = shim.writeToNBT(tag);
        }

        return tag;
    }

    protected void readFromFixedNBT (NBTTagCompound tag) {
        if (fixedShims != null) {
            for (TileDataShim shim : fixedShims)
                shim.readFromNBT(tag);
        }
    }

    protected NBTTagCompound writeToFixedNBT (NBTTagCompound tag) {
        if (fixedShims != null) {
            for (TileDataShim shim : fixedShims)
                tag = shim.writeToNBT(tag);
        }

        return tag;
    }

    private void trapLoadFailure (Throwable t, NBTTagCompound tag) {
        failureSnapshot = tag.copy();
        FMLLog.log(Chameleon.MOD_ID, Level.ERROR, t, "Tile Load Failure.");
    }

    private void restoreLoadFailure (NBTTagCompound tag) {
        Iterator<String> iter = failureSnapshot.getKeySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            if (!tag.hasKey(key))
                tag.setTag(key, failureSnapshot.getTag(key).copy());
        }
    }

    protected boolean loadDidFail () {
        return failureSnapshot != null;
    }

    @Override
    public final NBTTagCompound getUpdateTag () {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);

        return tag;
    }

    @Override
    public final SPacketUpdateTileEntity getUpdatePacket () {
        return hasDataPacket() ? new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), getUpdateTag()) : null;
    }

    @Override
    public final void onDataPacket (NetworkManager net, SPacketUpdateTileEntity pkt) {
        if (pkt != null && pkt.getNbtCompound() != null)
            readFromNBT(pkt.getNbtCompound());

        if (dataPacketRequiresRenderUpdate() && getWorld().isRemote) {
            IBlockState state = getWorld().getBlockState(getPos());
            getWorld().notifyBlockUpdate(getPos(), state, state, 3);
        }
    }

    public void markBlockForUpdate () {
        if (getWorld() != null && !getWorld().isRemote) {
            IBlockState state = getWorld().getBlockState(pos);
            getWorld().notifyBlockUpdate(pos, state, state, 3);
        }
    }

    public void markBlockForUpdateClient () {
        if (getWorld() != null && getWorld().isRemote) {
            IBlockState state = getWorld().getBlockState(pos);
            getWorld().notifyBlockUpdate(pos, state, state, 3);
        }
    }

    public void markBlockForRenderUpdate () {
        getWorld().markBlockRangeForRenderUpdate(pos, pos);
    }
}
