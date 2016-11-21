package com.jaquadro.minecraft.chameleon.resources.register;

import com.jaquadro.minecraft.chameleon.resources.IItemMeshMapper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

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
        NonNullList<ItemStack> variants = NonNullList.create();

        if (item instanceof IItemMeshMapper) {
            for (Pair<ItemStack, ModelResourceLocation> pair : ((IItemMeshMapper) item).getMeshMappings())
                variants.add(pair.getKey());
        }
        else
            item.getSubItems(item, null, variants);

        return variants;
    }

    @Override
    public List<ResourceLocation> getTextureResources () {
        return new ArrayList<ResourceLocation>();
    }
}
