package org.jared.dungeoncrawler.api.generation.block;

import org.bukkit.Material;

public interface IMaterialAndData
{
    Material getMaterial();

    String getBlockData();

    byte getBlockDataLegacy();
}
