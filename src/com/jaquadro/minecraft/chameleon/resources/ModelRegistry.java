package com.jaquadro.minecraft.chameleon.resources;

import com.jaquadro.minecraft.chameleon.Chameleon;
import com.jaquadro.minecraft.chameleon.resources.register.IBlockModelRegister;
import com.jaquadro.minecraft.chameleon.resources.register.IItemModelRegister;
import com.jaquadro.minecraft.chameleon.resources.register.IUnifiedRegister;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

@SideOnly(Side.CLIENT)
public class ModelRegistry
{
    private final Set<IBlockModelRegister> blockRegistry = new HashSet<IBlockModelRegister>();
    private final Set<IItemModelRegister> itemRegistry = new HashSet<IItemModelRegister>();

    public final ChamModelLoader loader = new ChamModelLoader();

    public void registerModel (IBlockModelRegister modelRegister) {
        if (modelRegister != null)
            blockRegistry.add(modelRegister);
    }

    public void registerModel (IItemModelRegister modelRegister) {
        if (modelRegister != null) {
            itemRegistry.add(modelRegister);
            registerItemVariants(modelRegister.getItem());
        }
    }

    public void registerModel (IUnifiedRegister modelRegister) {
        if (modelRegister != null) {
            blockRegistry.add(modelRegister);
            itemRegistry.add(modelRegister);

            registerItemVariants(modelRegister.getItem());
            registerTextureResources(modelRegister.getTextureResources());
        }
    }

    public void registerItemVariants (Block block) {
        if (block != null && Block.REGISTRY.getIDForObject(block) != Block.getIdFromBlock(Blocks.AIR)) {
            Item item = Item.getItemFromBlock(block);
            registerItemVariants(item);
        }
    }

    public void registerItemVariants (Item item) {
        if (item != null && Item.REGISTRY.getNameForObject(item) != null) {
            registerItemMapping(item);
            if (item instanceof IItemVariantProvider)
                registerItemVariants(item, (IItemVariantProvider) item);
            else if (item instanceof IItemMeshMapper)
                registerItemVariants(item, (IItemMeshMapper) item);
        }
    }

    public void registerItemVariants (Item item, IItemVariantProvider provider) {
        if (item != null && provider != null && Item.REGISTRY.getNameForObject(item) != null) {
            List<ResourceLocation> variants = ((IItemVariantProvider) item).getItemVariants();
            ModelBakery.registerItemVariants(item, variants.toArray(new ResourceLocation[variants.size()]));
        }
    }

    public void registerItemVariants (Item item, IItemMeshMapper mapper) {
        if (item != null && mapper != null && Item.REGISTRY.getNameForObject(item) != null) {
            List<ResourceLocation> variants = new ArrayList<ResourceLocation>();
            for (Pair<ItemStack, ModelResourceLocation> pair : mapper.getMeshMappings())
                variants.add(pair.getValue());

            ModelBakery.registerItemVariants(item, variants.toArray(new ResourceLocation[variants.size()]));
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

            registerItemMapping(item, register);
        }
    }

    private void registerItemMapping (Item item) {
        registerItemMapping(item, null);
    }

    private void registerItemMapping (Item item, IItemModelRegister register) {
        if (item instanceof IItemMeshResolver)
            ModelLoader.setCustomMeshDefinition(item, ((IItemMeshResolver) item).getMeshResolver());
        else if (register != null) {
            for (ItemStack stack : register.getItemVariants())
                ModelLoader.setCustomModelResourceLocation(item, stack.getMetadata(), getResourceLocation(stack));
        }
        else if (item instanceof IItemMeshMapper) {
            for (Pair<ItemStack, ModelResourceLocation> pair : ((IItemMeshMapper) item).getMeshMappings())
                ModelLoader.setCustomModelResourceLocation(item, pair.getKey().getMetadata(), pair.getValue());
        }
        else {
            List<ItemStack> variants = new ArrayList<ItemStack>();
            item.getSubItems(item, null, variants);
            for (ItemStack stack : variants)
                ModelLoader.setCustomModelResourceLocation(item, stack.getMetadata(), getResourceLocation(stack));
        }
    }

    private void registerTextureResources (List<ResourceLocation> resources) {
        for (ResourceLocation resource : resources)
            Chameleon.instance.iconRegistry.registerIcon(resource);
    }

    private static ModelResourceLocation getResourceLocation (IBlockState state) {
        ResourceLocation loc = state.getBlock().getRegistryName();

        StringBuilder builder = new StringBuilder();
        for (Map.Entry<IProperty<?>, Comparable<?>> entry : state.getProperties().entrySet()) {
            builder.append(entry.getKey().getName()).append('=').append(entry.getValue().toString()).append(',');
        }

        if (builder.length() > 0)
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

        if (item instanceof IItemMeshMapper) {
            for (Pair<ItemStack, ModelResourceLocation> pair : ((IItemMeshMapper) item).getMeshMappings()) {
                if (ItemStack.areItemsEqual(stack, pair.getKey()))
                    return pair.getValue();
            }
        }

        Block block = Block.getBlockFromItem(item);
        if (block != null)
            return new ModelResourceLocation(block.getRegistryName(), "inventory");
        else
            return new ModelResourceLocation(item.getRegistryName(), "inventory");
    }

    public class ChamModelLoader implements ICustomModelLoader
    {
        @Override
        public boolean accepts (ResourceLocation modelLocation) {
            if (!modelLocation.getResourcePath().contains("#"))
                return false;

            return true;
        }

        @Override
        public IModel loadModel (ResourceLocation modelLocation) throws Exception {
            return null;
        }

        @Override
        public void onResourceManagerReload (IResourceManager resourceManager) {
        }
    }
}
