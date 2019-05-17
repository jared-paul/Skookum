package org.jared.dungeoncrawler.api.structures;

import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.generation.block.IMaterialAndData;

import java.util.Map;

public interface ICustomDefinedStructure
{
    int[] getDimensions();

    Map<Vector, IMaterialAndData> getBlockMap();
}
