package com.jaquadro.minecraft.chameleon.item;

import com.jaquadro.minecraft.chameleon.resources.IItemEnum;
import com.jaquadro.minecraft.chameleon.resources.IItemMeshMapper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

public class ChamMultiItem<T extends Enum<T> & IItemEnum> extends Item implements IItemMeshMapper
{
    private final Class<T> clazz;
    private final IntFunction<T> metaLookup;

    public ChamMultiItem (Class<T> clazz, IntFunction<T> metaLookup) {
        this.clazz = clazz;
        this.metaLookup = metaLookup;

        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public String getUnlocalizedName (ItemStack itemStack) {
        T material = metaLookup.apply(itemStack.getMetadata());
        return super.getUnlocalizedName() + "." + material.getUnlocalizedName();
    }

    @Override
    public int getMetadata (int damage) {
        return damage;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems (CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
        if (isInCreativeTab(creativeTabs)) {
            for (T entry : clazz.getEnumConstants())
                list.add(new ItemStack(this, 1, entry.getMetadata()));
        }
    }

    @Override
    public List<Pair<ItemStack, ModelResourceLocation>> getMeshMappings () {
        List<Pair<ItemStack, ModelResourceLocation>> mappings = new ArrayList<Pair<ItemStack, ModelResourceLocation>>();

        for (T entry : clazz.getEnumConstants()) {
            ModelResourceLocation location = new ModelResourceLocation(getRegistryName().toString() + '_' + entry.getName(), "inventory");
            mappings.add(Pair.of(new ItemStack(this, 1, entry.getMetadata()), location));
        }

        return mappings;
    }
}
