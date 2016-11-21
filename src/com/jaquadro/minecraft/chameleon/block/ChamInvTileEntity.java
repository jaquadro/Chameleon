package com.jaquadro.minecraft.chameleon.block;

import com.jaquadro.minecraft.chameleon.block.tiledata.CustomNameData;
import com.jaquadro.minecraft.chameleon.block.tiledata.TileDataShim;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorldNameable;

import javax.annotation.Nullable;

public class ChamInvTileEntity extends ChamTileEntity implements IInventory, IWorldNameable
{
    private static final InventoryBasic emptyInventory = new InventoryBasic("[null]", true, 0);

    private CustomNameData customNameData;

    @Override
    public void injectData (TileDataShim shim) {
        super.injectData(shim);
        if (shim instanceof CustomNameData)
            customNameData = (CustomNameData)shim;
    }

    @Override
    public int getSizeInventory () {
        return emptyInventory.getSizeInventory();
    }

    @Override
    public boolean isEmpty () {
        return true;
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot (int index) {
        return emptyInventory.getStackInSlot(index);
    }

    @Nullable
    @Override
    public ItemStack decrStackSize (int index, int count) {
        return emptyInventory.decrStackSize(index, count);
    }

    @Nullable
    @Override
    public ItemStack removeStackFromSlot (int index) {
        return emptyInventory.removeStackFromSlot(index);
    }

    @Override
    public void setInventorySlotContents (int index, @Nullable ItemStack stack) {
        emptyInventory.setInventorySlotContents(index, stack);
    }

    @Override
    public int getInventoryStackLimit () {
        return emptyInventory.getInventoryStackLimit();
    }

    @Override
    public boolean isUsableByPlayer (EntityPlayer player) {
        return emptyInventory.isUsableByPlayer(player);
    }

    @Override
    public void openInventory (EntityPlayer player) {
        emptyInventory.openInventory(player);
    }

    @Override
    public void closeInventory (EntityPlayer player) {
        emptyInventory.closeInventory(player);
    }

    @Override
    public boolean isItemValidForSlot (int index, ItemStack stack) {
        return emptyInventory.isItemValidForSlot(index, stack);
    }

    @Override
    public int getField (int id) {
        return emptyInventory.getField(id);
    }

    @Override
    public void setField (int id, int value) {
        emptyInventory.setField(id, value);
    }

    @Override
    public int getFieldCount () {
        return emptyInventory.getFieldCount();
    }

    @Override
    public void clear () {
        emptyInventory.clear();
    }

    @Override
    public String getName () {
        if (customNameData != null)
            return customNameData.getName();

        return emptyInventory.getName();
    }

    @Override
    public boolean hasCustomName () {
        if (customNameData != null)
            return customNameData.hasCustomName();

        return emptyInventory.hasCustomName();
    }

    @Nullable
    @Override
    public ITextComponent getDisplayName () {
        if (customNameData != null)
            return customNameData.getDisplayName();

        return emptyInventory.getDisplayName();
    }

    public void setInventoryName (String name) {
        if (customNameData != null)
            customNameData.setName(name);
    }
}
