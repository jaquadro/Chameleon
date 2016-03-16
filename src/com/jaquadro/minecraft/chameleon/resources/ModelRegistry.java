package com.jaquadro.minecraft.chameleon.resources;

import com.jaquadro.minecraft.chameleon.Chameleon;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
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
    private final Set<IModelRegister> registry = new HashSet<IModelRegister>();

    public void registerModel (IModelRegister modelRegister) {
        if (modelRegister != null) {
            if (registry.add(modelRegister))
                registerTextureResources(modelRegister.getTextureResources());
        }
    }

    public void loadModels (IRegistry<ModelResourceLocation, IBakedModel> modelIRegistry) {
        for (IModelRegister register : registry) {
            for (IBlockState state : register.getBlockStates())
                modelIRegistry.putObject(getResourceLocation(state), register.getModel(state));
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

}
