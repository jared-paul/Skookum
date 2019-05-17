package org.jared.dungeoncrawler.api.generation;

import com.google.common.collect.Lists;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jared.dungeoncrawler.api.generation.cell.IRoom;
import org.jared.dungeoncrawler.api.structures.AABB;

import java.util.List;

public class DungeonMap
{
    Location base;
    //opposites for get()
    private double xmin = Integer.MAX_VALUE;
    private double xmax = Integer.MIN_VALUE;
    private double zmin = Integer.MAX_VALUE;
    private double zmax = Integer.MIN_VALUE;

    private DungeonMap(Location base)
    {
        this.base = base;
    }

    public double getLeft()
    {
        return xmin;
    }

    public double getRight()
    {
        return xmax;
    }

    public double getTop()
    {
        return zmax;
    }

    public double getBottom()
    {
        return zmin;
    }

    public double getWidth()
    {
        return xmax - xmin;
    }

    public double getLength()
    {
        return zmax - zmin;
    }

    public List<Chunk> getChunks()
    {
        List<Chunk> chunks = Lists.newArrayList();

        for (int x = (int) xmin; x <= xmax; x += 16)
        {
            for (int z = (int) zmin; z <= zmax; z += 16)
            {
                chunks.add(base.getWorld().getChunkAt(x, z));
            }
        }

        return chunks;
    }

    public static DungeonMap get(Location base, List<IRoom> cells)
    {
        DungeonMap cb = new DungeonMap(base);
        double i;
        for (IRoom cell : cells)
        {
            AABB bounds = cell.getAABB();
            i = bounds.getMinX();
            if (i < cb.xmin)
            {
                cb.xmin = i;
            }
            i = bounds.getMaxX();
            if (i > cb.xmax)
            {
                cb.xmax = i;
            }
            i = bounds.getMinZ();
            if (i < cb.zmin)
            {
                cb.zmin = i;
            }
            i = bounds.getMaxZ();
            if (i > cb.zmax)
            {
                cb.zmax = i;
            }
        }

        return cb;
    }

    @Override
    public String toString()
    {
        return "CellBounds [xmin=" + xmin + ", xmax=" + xmax + ", ymin=" + zmin
                + ", ymax=" + zmax + "]";
    }
}
