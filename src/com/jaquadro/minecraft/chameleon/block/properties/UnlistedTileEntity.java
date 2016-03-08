package com.jaquadro.minecraft.chameleon.block.properties;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedTileEntity<T extends TileEntity> implements IUnlistedProperty<T>
{
    public static <T extends TileEntity> UnlistedTileEntity<T> create (Class<T> clazz) {
        return new UnlistedTileEntity<T>(clazz);
    }

    private final Class<T> valueClass;

    protected UnlistedTileEntity (Class<T> clazz) {
        valueClass = clazz;
    }

    @Override
    public String getName () {
        return "tileEntity";
    }

    @Override
    public boolean isValid (TileEntity value) {
        return true;
    }

    @Override
    public Class<T> getType () {
        return valueClass;
    }

    @Override
    public String valueToString (T value) {
        return value.toString();
    }
}
