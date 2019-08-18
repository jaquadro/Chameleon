package com.jaquadro.minecraft.chameleon.model.data;

import com.jaquadro.minecraft.chameleon.model.ModelData;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class ConnectedModelData extends ModelData
{
    private final boolean north;
    private final boolean east;
    private final boolean south;
    private final boolean west;
    private final boolean up;
    private final boolean down;

    public ConnectedModelData (IBlockAccess world, BlockPos pos, Block block) {
        north = world.getBlockState(pos.north()).getBlock() == block;
        east = world.getBlockState(pos.east()).getBlock() == block;
        south = world.getBlockState(pos.south()).getBlock() == block;
        west = world.getBlockState(pos.west()).getBlock() == block;
        up = world.getBlockState(pos.up()).getBlock() == block;
        down = world.getBlockState(pos.down()).getBlock() == block;
    }

    public boolean north () {
        return north;
    }

    public boolean east () {
        return east;
    }

    public boolean south () {
        return south;
    }

    public boolean west () {
        return west;
    }

    public boolean up () {
        return up;
    }

    public boolean down () {
        return down;
    }
}
