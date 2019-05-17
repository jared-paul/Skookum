package org.jared.dungeoncrawler.api.generation.block;

import org.bukkit.Material;

//only material no data supported
public class MaterialAndData implements IMaterialAndData
{
    private Material material;
    private String data;
    private byte legacyData;

    public MaterialAndData(Material material)
    {
        this(material, "", (byte) 0);
    }

    public MaterialAndData(Material material, String data, byte legacyData)
    {
        this.material = material;
        this.data = data;
        this.legacyData = legacyData;
    }

    @Override
    public Material getMaterial()
    {
        return material;
    }

    @Override
    public String getBlockData()
    {
        return data;
    }

    @Override
    public byte getBlockDataLegacy()
    {
        return legacyData;
    }
}
