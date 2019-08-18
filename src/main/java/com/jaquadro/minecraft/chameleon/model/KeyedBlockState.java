package com.jaquadro.minecraft.chameleon.model;

import net.minecraft.block.state.IBlockState;

import java.util.List;

class KeyedBlockState
{
    private IBlockState state;
    private List<Object> key;

    public KeyedBlockState (IBlockState state, List<Object> key) {
        this.state = state;
        this.key = key;
    }

    public IBlockState getState () {
        return state;
    }

    @Override
    public boolean equals (Object obj) {
        if (obj == null || this.getClass() != obj.getClass())
            return false;

        KeyedBlockState other = (KeyedBlockState)obj;
        return this.key.equals(other.key);
    }

    @Override
    public int hashCode () {
        return key.hashCode();
    }
}
