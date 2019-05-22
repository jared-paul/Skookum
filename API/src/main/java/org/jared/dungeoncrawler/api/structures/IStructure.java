package org.jared.dungeoncrawler.api.structures;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.concurrency.Callback;
import org.jared.dungeoncrawler.api.generation.block.IBlockPlacer;
import org.jared.dungeoncrawler.api.material.IMaterialAndData;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface IStructure extends Cloneable
{
    String getName();

    void setName(String name);

    File getFile();

    void setFile(File file);

    AABB getTestAABB(Vector position, int angle);

    int getVolume();

    int getWidth();

    int getHeight();

    int getDepth();

    int[] getDimensions();

    void place(Location base, IBlockPlacer blockPlacer, Callback<Boolean> callback);

    void place(Location base);

    //returns a COPY of the block data based off the rotation angle
    void rotate(int angle);

    List<Vector> getRotatedEdgeCases(Vector base, int angle);

    Map<Vector, IMaterialAndData> getBlockMap();

    Map<Vector, IMaterialAndData> getPoints(Vector testPoint);

    IStructure clone();
}
