package com.jaquadro.minecraft.chameleon.resources;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

public class IconUtil
{
    public static TextureAtlasSprite getIconFromStack (ItemStack stack) {
        if (stack == null)
            return null;

        Block block = Block.getBlockFromItem(stack.getItem());
        if (block == null)
            return null;

        IBlockState matState = block.getStateFromMeta(stack.getMetadata());
        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBakedModel model = dispatcher.getBlockModelShapes().getModelForState(matState);

        //if (model instanceof ISmartBlockModel)
        //    model = ((ISmartBlockModel) model).handleBlockState(matState);

        return model.getParticleTexture();
    }
}
