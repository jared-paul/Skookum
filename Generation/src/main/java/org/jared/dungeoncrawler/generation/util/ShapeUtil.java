package org.jared.dungeoncrawler.generation.util;

import com.google.common.collect.Lists;
import org.bukkit.util.Vector;

import java.util.List;

public class ShapeUtil
{
    public static List<Vector> createSquare(Vector center, int radius)
    {
        List<Vector> square = Lists.newArrayList();

        Vector min = new Vector(center.getX() - radius, center.getY(), center.getZ() - radius);
        Vector max = new Vector(center.getX() + radius, center.getY(), center.getZ() + radius);

        for (int x = min.getBlockX(); x < max.getBlockX(); x++)
        {
            for (int z = min.getBlockZ(); z < max.getBlockZ(); z++)
            {
                square.add(new Vector(x, center.getY(), z));
            }
        }

        return square;
    }
}
