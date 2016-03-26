package com.jaquadro.minecraft.chameleon.resources.register;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;

import java.lang.reflect.ParameterizedType;

public abstract class SmartHandlerRegister<TBlock extends ISmartBlockModel, TItem extends ISmartItemModel> extends DefaultRegister
{
    private final Class<TBlock> blockClass;
    private final Class<TItem> itemClass;

    private ISmartBlockModel blockModel;
    private ISmartItemModel itemModel;

    @SuppressWarnings("unchecked")
    public SmartHandlerRegister (Block block) {
        super(block);

        blockClass = (Class<TBlock>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        itemClass = (Class<TItem>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    @Override
    public IBakedModel getModel (IBlockState state, IBakedModel existingModel) {
        if (blockModel == null) {
            try {
                blockModel = blockClass.newInstance();
            } catch (Exception e) { }
        }
        return blockModel;
    }

    @Override
    public IBakedModel getModel (ItemStack stack, IBakedModel existingModel) {
        if (itemModel == null) {
            try {
                itemModel = itemClass.newInstance();
            } catch (Exception e) { }
        }
        return itemModel;
    }
}
