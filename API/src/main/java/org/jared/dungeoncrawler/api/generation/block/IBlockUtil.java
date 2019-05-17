package org.jared.dungeoncrawler.api.generation.block;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.List;

public interface IBlockUtil
{
    boolean setBlockFast(Location location, IMaterialAndData materialAndData);

    boolean setBlockSlow(Location location, IMaterialAndData materialAndData);

    void refreshChunkVectors(List<Vector> vectors, Location base);

    void refreshChunkLocations(List<Location> locations);

    void refreshChunks(List<Chunk> chunks);

    List<Chunk> getChunks(List<Location> locations);
}
