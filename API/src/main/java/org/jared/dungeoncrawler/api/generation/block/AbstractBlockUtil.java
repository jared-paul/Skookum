package org.jared.dungeoncrawler.api.generation.block;

import com.google.common.collect.Lists;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.util.VectorUtil;

import java.util.List;

public abstract class AbstractBlockUtil implements IBlockUtil
{
    @Override
    public void refreshChunkVectors(List<Vector> vectors, Location base)
    {
        refreshChunkLocations(VectorUtil.toLocations(vectors, base));
    }

    @Override
    public void refreshChunkLocations(List<Location> locations)
    {
        refreshChunks(getChunks(locations));
    }

    @Override
    public void refreshChunks(List<Chunk> chunks)
    {
        chunks.forEach(chunk -> chunk.getWorld().refreshChunk(chunk.getX(), chunk.getZ()));
    }

    @Override
    public List<Chunk> getChunks(List<Location> locations)
    {
        List<Chunk> chunks = Lists.newArrayList();

        for (Location location : locations)
        {
            Chunk chunk = location.getChunk();

            if (!chunks.contains(chunk))
                chunks.add(chunk);
        }

        return chunks;
    }
}
