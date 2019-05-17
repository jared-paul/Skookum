package org.jared.dungeoncrawler.api.util;

import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;

public enum Direction
{
    EAST(1, 0, 0, BlockFace.EAST),
    WEST(-1, 0, 0, BlockFace.WEST),
    NORTH(0, 0, -1, BlockFace.NORTH),
    SOUTH(0, 0, 1, BlockFace.SOUTH),
    NORTH_EAST(NORTH, EAST),
    NORTH_WEST(NORTH, WEST),
    SOUTH_EAST(SOUTH, EAST),
    SOUTH_WEST(SOUTH, WEST),;

    public double xOffset, yOffset, zOffset;
    public BlockFace blockFace;

    Direction(double x, double y, double z, BlockFace blockFace)
    {
        this.xOffset = x;
        this.yOffset = y;
        this.zOffset = z;
        this.blockFace = blockFace;
    }

    Direction(Direction direction1, Direction direction2)
    {
        this.xOffset = direction1.xOffset + direction2.xOffset;
        this.yOffset = direction1.yOffset + direction2.yOffset;
        this.zOffset = direction1.zOffset + direction2.zOffset;
    }

    public static Direction[] getMainDirections()
    {
        return new Direction[]{EAST, WEST, NORTH, SOUTH};
    }

    public static Direction[] getOddDirections()
    {
        return new Direction[]{NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST};
    }

    public Vector toVector()
    {
        return new Vector(xOffset, yOffset, zOffset);
    }

    public static Direction getDirection(Vector vector)
    {
        for (Direction direction : Direction.values())
        {
            if (direction.toVector().equals(vector))
                return direction;
        }

        return null;
    }

    public static Direction test(Vector initial, Vector target)
    {
        Vector vector = initial.clone().subtract(target.clone()).normalize();

        DungeonCrawler.LOG.severe("VECTOR: " + vector);

        double x = vector.getX();
        double y = vector.getY();
        double z = vector.getZ();

        if (x > 0)
        {
            x = Math.ceil(x);
        }
        else
        {
            x = Math.floor(x);
        }

        if (z > 0)
        {
            z = Math.ceil(z);
        }
        else
        {
            z = Math.floor(z);
        }

        Vector offset = new Vector(x, y, z);

        DungeonCrawler.LOG.severe("OFFSET: " + offset);

        return getDirection(offset);
    }

    private static Direction getNearestDirection(Vector vector)
    {
        Direction closest = Direction.NORTH_EAST;

        for (Direction direction : Direction.getOddDirections())
        {
            if (vector.distanceSquared(direction.toVector()) > vector.distanceSquared(closest.toVector()))
                closest = direction;
        }

        return closest;
    }

    public BlockFace toBlockFace()
    {
        return blockFace;
    }
}
