package com.jaquadro.minecraft.chameleon.item;

import com.jaquadro.minecraft.chameleon.resources.IItemEnum;
import com.jaquadro.minecraft.chameleon.resources.IItemMeshMapper;
import com.jaquadro.minecraft.chameleon.resources.IItemVariantProvider;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

public class ChamMultiItemBlock<T extends Enum<T> & IItemEnum> extends ItemBlock implements IItemMeshMapper, IItemVariantProvider
{
    private final Class<T> clazz;
    private final IntFunction<T> metaLookup;
    private final PropertyEnum<T> property;

    public ChamMultiItemBlock (Block block, PropertyEnum<T> property, Class<T> clazz, IntFunction<T> metaLookup) {
        super(block);
        this.property = property;
        this.clazz = clazz;
        this.metaLookup = metaLookup;

        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata (int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName (ItemStack itemStack) {
        T material = metaLookup.apply(itemStack.getMetadata());
        return super.getUnlocalizedName() + "." + material.getUnlocalizedName();
    }

    @Override
    public List<Pair<ItemStack, ModelResourceLocation>> getMeshMappings () {
        List<Pair<ItemStack, ModelResourceLocation>> mappings = new ArrayList<Pair<ItemStack, ModelResourceLocation>>();

        for (T entry : clazz.getEnumConstants()) {
            IBlockState state = block.getDefaultState().withProperty(property, entry);
            ModelResourceLocation location = new ModelResourceLocation(block.getRegistryName().toString() + '_' + entry.getName(), "inventory");
            mappings.add(Pair.of(new ItemStack(this, 1, block.getMetaFromState(state)), location));
        }

        return mappings;
    }

    @Override
    public List<ResourceLocation> getItemVariants () {
        ResourceLocation location = ForgeRegistries.ITEMS.getKey(this);
        List<ResourceLocation> variants = new ArrayList<ResourceLocation>();

        for (T entry : clazz.getEnumConstants())
            variants.add(new ResourceLocation(location.getResourceDomain(), location.getResourcePath() + '_' + entry.getName()));

        return variants;
    }
}
