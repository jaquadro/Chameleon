package com.jaquadro.minecraft.chameleon.block.tiledata;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorldNameable;
import net.minecraftforge.common.util.Constants;

public class CustomNameData extends TileDataShim implements IWorldNameable
{
    private String defaultName;
    private String customName;

    public CustomNameData (String defaultName) {
        this.defaultName = defaultName;
    }

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        customName = null;
        if (tag.hasKey("CustomName", Constants.NBT.TAG_STRING))
            customName = tag.getString("CustomName");
    }

    @Override
    public NBTTagCompound writeToNBT (NBTTagCompound tag) {
        if (hasCustomName())
            tag.setString("CustomName", customName);

        return tag;
    }

    @Override
    public String getName () {
        return hasCustomName() ? customName : defaultName;
    }

    @Override
    public boolean hasCustomName () {
        return customName != null && customName.length() > 0;
    }

    @Override
    public ITextComponent getDisplayName () {
        return null;
    }

    public void setName (String name) {
        customName = name;
    }
}
