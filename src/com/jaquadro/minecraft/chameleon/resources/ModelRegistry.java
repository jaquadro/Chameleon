package com.jaquadro.minecraft.chameleon.resources;

import com.jaquadro.minecraft.chameleon.Chameleon;
import com.jaquadro.minecraft.chameleon.resources.register.IBlockModelRegister;
import com.jaquadro.minecraft.chameleon.resources.register.IItemModelRegister;
import com.jaquadro.minecraft.chameleon.resources.register.ITextureRegister;
import com.jaquadro.minecraft.chameleon.resources.register.IUnifiedRegister;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class ModelRegistry
{
    private final Set<IBlockModelRegister> blockRegistry = new HashSet<IBlockModelRegister>();
    private final Set<IItemModelRegister> itemRegistry = new HashSet<IItemModelRegister>();

    public void registerModel (IBlockModelRegister modelRegister) {
        if (modelRegister != null)
            blockRegistry.add(modelRegister);
    }

    public void registerModel (IItemModelRegister modelRegister) {
        if (modelRegister != null)
            itemRegistry.add(modelRegister);
    }

    public void registerModel (IUnifiedRegister modelRegister) {
        if (modelRegister != null) {
            blockRegistry.add(modelRegister);
            itemRegistry.add(modelRegister);

            registerTextureResources(modelRegister.getTextureResources());
        }
    }

    public void loadModels (IRegistry<ModelResourceLocation, IBakedModel> modelIRegistry) {
        for (IBlockModelRegister register : blockRegistry) {
            for (IBlockState state : register.getBlockStates()) {
                ModelResourceLocation location = getResourceLocation(state);
                modelIRegistry.putObject(location, register.getModel(state, modelIRegistry.getObject(location)));
            }
        }

        for (IItemModelRegister register : itemRegistry) {
            Item item = register.getItem();
            for (ItemStack stack : register.getItemVariants()) {
                ModelResourceLocation location = getResourceLocation(stack);
                modelIRegistry.putObject(location, register.getModel(stack, modelIRegistry.getObject(location)));
            }

            RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
            if (item instanceof IItemMeshResolver)
                renderItem.getItemModelMesher().register(item, ((IItemMeshResolver) item).getMeshResolver());
            else {
                for (ItemStack stack : register.getItemVariants())
                    renderItem.getItemModelMesher().register(item, stack.getMetadata(), getResourceLocation(stack));
            }
        }
    }

    private void registerTextureResources (List<ResourceLocation> resources) {
        for (ResourceLocation resource : resources)
            Chameleon.instance.iconRegistry.registerIcon(resource);
    }

    private static ModelResourceLocation getResourceLocation (IBlockState state) {
        ResourceLocation loc = GameData.getBlockRegistry().getNameForObject(state.getBlock());

        StringBuilder builder = new StringBuilder();
        for (Map.Entry<IProperty, Comparable> entry : state.getProperties().entrySet()) {
            builder.append(entry.getKey().getName() + '=' + entry.getValue().toString() + ',');
        }
        builder.deleteCharAt(builder.length() - 1);

        return new ModelResourceLocation(loc, builder.toString());
    }

    private static ModelResourceLocation getResourceLocation (ItemStack stack) {
        Item item = stack.getItem();

        if (item instanceof ItemMeshDefinition)
            return ((ItemMeshDefinition) item).getModelLocation(stack);

        if (item instanceof IItemMeshResolver) {
            ItemMeshDefinition resolver = ((IItemMeshResolver) item).getMeshResolver();
            if (resolver != null)
                return resolver.getModelLocation(stack);
        }

        if (item instanceof IItemMeshProvider) {
            ItemMeshDefinition resolver = ((IItemMeshProvider) item).getMeshResolver();
            if (resolver != null)
                return resolver.getModelLocation(stack);
        }

        return new ModelResourceLocation(GameData.getItemRegistry().getNameForObject(item).toString(), "inventory");
    }
}
