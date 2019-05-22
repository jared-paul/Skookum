package org.jared.dungeoncrawler.api.material;

import org.bukkit.Material;

public interface IMaterialAndData
{
    Material getMaterial();

    String getBlockData();

    byte getBlockDataLegacy();


}
