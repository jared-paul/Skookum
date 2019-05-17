package org.jared.dungeoncrawler.api.util;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.data.Tuple;
import org.jared.dungeoncrawler.api.generation.block.IMaterialAndData;

import java.util.Collection;
import java.util.List;

public class VectorUtil
{
    public static List<Tuple<Vector, IMaterialAndData>> toBlockData(List<Vector> vectors, IMaterialAndData material)
    {
        List<Tuple<Vector, IMaterialAndData>> blockData = Lists.newArrayList();

        for (Vector vector : vectors)
        {
            blockData.add(new Tuple<>(vector, material));
        }

        return blockData;
    }

    public static List<Vector> getDoorwayVectors(Vector doorway)
    {
        List<Vector> vectors = Lists.newArrayList();
        for (double y = 1; y < 4; y++)
        {
            vectors.add(doorway.clone().add(new Vector(0, y, 0)));

            for (Direction direction : Direction.values())
            {
                Vector offset = doorway.clone().add(direction.toVector()).add(new Vector(0, y, 0));
                vectors.add(offset);
            }
        }

        return vectors;
    }

    public static List<Location> toLocations(Collection<Vector> vectors, Location base)
    {
        List<Location> locations = Lists.newArrayList();
        vectors.forEach(vector -> locations.add(toLocation(vector, base)));
        return locations;
    }

    public static Location toLocation(Vector vector, Location base)
    {
        return base.clone().add(vector);
    }

    public static List<Vector> getCorners(double xmin, double xmax, double zmin, double zmax)
    {
        Vector corner1 = new Vector(xmin, 0, zmin);
        Vector corner2 = new Vector(xmin, 0, zmax);
        Vector corner3 = new Vector(xmax, 0, zmax);
        Vector corner4 = new Vector(xmax, 0, zmin);

        return Lists.newArrayList(corner1, corner2, corner3, corner4);
    }

    public static void setVectorRotation(Vector vector, int angle)
    {
        Vector rotated = rotateVector(vector, angle);
        vector.setX(rotated.getX());
        vector.setY(rotated.getY());
        vector.setZ(rotated.getZ());
    }

    public static Vector rotateVector(Vector vector, int angle)
    {
        double rad = Math.toRadians(angle);

        double currentX = vector.getX();
        double currentZ = vector.getZ();

        double cosine = Math.cos(rad);
        double sine = Math.sin(rad);

        return new Vector((cosine * currentX - sine * currentZ), vector.getY(), (sine * currentX + cosine * currentZ));
    }

    public static BlockFace fromVector(Vector vector)
    {
        for (BlockFace blockFace : BlockFace.values())
        {
            if (vector.equals(new Vector(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ())))
                return blockFace;
        }

        return null;
    }

    public static Vector toVector(BlockFace blockFace)
    {
        return new Vector(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());
    }

    public static Vector toVector(Location location, Location base)
    {
        return new Vector(location.getBlockX() - base.getBlockX(), location.getBlockY() - base.getBlockY(), location.getBlockZ() - base.getBlockZ());
    }
}
