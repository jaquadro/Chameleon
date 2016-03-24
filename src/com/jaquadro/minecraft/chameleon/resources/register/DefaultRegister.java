package com.jaquadro.minecraft.chameleon.resources.register;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public abstract class DefaultRegister implements IUnifiedRegister
{
    private final Block block;

    public DefaultRegister (Block block) {
        this.block = block;
    }

    @Override
    public Item getItem () {
        return Item.getItemFromBlock(block);
    }

    @Override
    public List<ItemStack> getItemVariants () {
        Item item = getItem();
        List<ItemStack> variants = new ArrayList<ItemStack>();
        item.getSubItems(item, null, variants);

        return variants;
    }

    @Override
    public List<ResourceLocation> getTextureResources () {
        return new ArrayList<ResourceLocation>();
    }
}
