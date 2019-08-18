package com.jaquadro.minecraft.chameleon.block.properties;

import com.jaquadro.minecraft.chameleon.model.ModelData;
import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedModelData<T extends ModelData> implements IUnlistedProperty<T>
{
    public static <T extends ModelData> UnlistedModelData<T> create (Class<T> clazz) {
        return new UnlistedModelData<T>(clazz);
    }

    private final Class<T> valueClass;

    protected UnlistedModelData (Class<T> clazz) {
        valueClass = clazz;
    }

    @Override
    public String getName () {
        return "modelData";
    }

    @Override
    public boolean isValid (ModelData value) {
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
