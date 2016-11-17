package com.jaquadro.minecraft.chameleon.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import scala.actors.threadpool.Arrays;

public class ItemResourceLocation extends ResourceLocation
{
    private final int metadata;

    protected ItemResourceLocation (int unused, String domain, String path, int metadata) {
        super(0, domain, path);
        this.metadata = metadata;
    }

    public ItemResourceLocation (String domain, String path, int metadata) {
        this(0, domain, path, metadata);
    }

    public ItemResourceLocation (ResourceLocation location, int metadata) {
        this(0, location.getResourceDomain(), location.getResourcePath(), metadata);
    }

    public ItemResourceLocation (String combined) {
        super(0, extractPathParts(combined));
        this.metadata = extractMetadata(combined);
    }

    public int getMetadata () {
        return metadata;
    }

    public ItemStack getItemStack () {
        Item item = Item.getByNameOrId(super.toString());
        if (item == null) {
            Block block = Block.getBlockFromName(super.toString());
            if (block != null)
                item = Item.getItemFromBlock(block);
        }

        if (item == null)
            return null;

        return new ItemStack(item, 1, metadata);
    }

    @Override
    public boolean equals (Object object) {
        if (this == object)
            return true;
        if (object instanceof ItemResourceLocation && super.equals(object))
            return metadata == ((ItemResourceLocation)object).metadata;

        return false;
    }

    @Override
    public int hashCode () {
        return 31 * super.hashCode() + metadata;
    }

    @Override
    public String toString () {
        return super.toString() + ':' + metadata;
    }

    private static String[] extractPathParts (String combined) {
        String[] parts = combined.split(":");
        switch (parts.length) {
            case 1:
                return new String[] { null, parts[0] };
            case 2:
                return parts;
            case 3:
            default:
                return (String[])Arrays.copyOfRange(parts, 0, 2);
        }
    }

    private static int extractMetadata (String combined) {
        String[] parts = combined.split(":");
        if (parts.length < 3)
            return OreDictionary.WILDCARD_VALUE;

        try {
            return Integer.parseInt(parts[parts.length - 1]);
        }
        catch (NumberFormatException e) {
            return OreDictionary.WILDCARD_VALUE;
        }
    }
}
