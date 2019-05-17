package org.jared.dungeoncrawler.api.structures;

import org.bukkit.Location;

import java.io.File;

public interface IStructureUtil
{
    IStructure loadStructure(File source) throws Exception;

    int[] getDimensions(Location[] corners);

    Location[] normalizeEdges(Location startBlock, Location endBlock);
}
