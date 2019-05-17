package org.jared.dungeoncrawler.api.generation.cell;

import com.google.common.collect.Lists;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.data.Tuple;
import org.jared.dungeoncrawler.api.structures.AABB;

import javax.annotation.Nullable;
import java.util.List;

public class CellUtil
{
    public static IRoom getCell(Vector v, List<IRoom> cells)
    {
        for (IRoom cell : cells)
        {
            if (isPointInCell(v, cell))
            {
                return cell;
            }
        }

        return null;
    }

    public static boolean isPointInCells(Vector point, @Nullable IRoom exclude, List<IRoom> cells)
    {
        for (IRoom cell : cells)
        {
            if (exclude != null)
            {
                if (cell.equals(exclude))
                    continue;
            }

            if (isPointInCell(point, cell))
                return true;
        }

        return false;
    }

    public static boolean isPointInCell(Vector point, IRoom cell)
    {
        return cell.getAABB().intersects(point);
    }

    public static Vector getClosestRingPoint(Vector initialPoint, AABB otherAABB, int fromCorner, int augment)
    {
        double minDistance = Double.MAX_VALUE;
        List<Vector> points = getRingPoints(otherAABB, augment);

        Vector closest = null;

        for (Vector point : points)
        {
            if (isCloseToCorner(point, otherAABB, fromCorner))
                continue;

            double distance = point.distanceSquared(initialPoint);

            if (distance < minDistance)
            {
                minDistance = distance;
                closest = point;
            }
        }

        return closest;
    }

    public static Tuple<Vector, Vector> getClosestRingPoints(AABB aabb, AABB otherAABB, int fromCorner, int augment)
    {
        double minDistance = Double.MAX_VALUE;
        Tuple<Vector, Vector> closest = null;

        List<Vector> points = getRingPoints(aabb, augment);
        List<Vector> otherPoints = getRingPoints(otherAABB, augment);

        for (Vector point : points)
        {
            if (isCloseToCorner(point, aabb, fromCorner))
                continue;

            for (Vector otherPoint : otherPoints)
            {
                if (isCloseToCorner(otherPoint, otherAABB, fromCorner))
                    continue;

                double distance = point.distanceSquared(otherPoint);

                if (distance < minDistance)
                {
                    minDistance = distance;
                    closest = new Tuple<>(point, otherPoint);
                }
            }
        }

        return closest;
    }


    public static List<Vector> getRingPoints(AABB bounds, int augmentFromMax)
    {
        List<Vector> points = Lists.newArrayList();

        AABB clone = bounds.clone();
        clone.grow(augmentFromMax, 0, augmentFromMax);

        double x1 = clone.getMaxX();
        double z1 = clone.getMinZ();
        double x2 = clone.getMinX();
        double z2 = clone.getMaxZ();

        for (double x = x2; x <= x1; x++)
        {
            for (double z = z1; z <= z2; z++)
            {
                if (x == clone.getMinX() ||
                        z == clone.getMinZ() ||
                        z == clone.getMaxZ() ||
                        x == clone.getMaxX())
                {
                    Vector point = new Vector(x, 0, z);

                    points.add(point);
                }
            }
        }

        return points;
    }

    public static boolean isCloseToCorners(Vector point, List<IRoom> cells, double distanceInBlocks)
    {
        for (IRoom cell : cells)
        {
            if (isCloseToCorner(point, cell.getAABB(), distanceInBlocks))
                return true;
        }

        return false;
    }

    public static boolean isCloseToCorner(Vector point, AABB aabb, double distanceInBlocks)
    {
        for (Vector corner : aabb.getBaseCorners())
        {
            if (point.distanceSquared(corner) <= distanceInBlocks * distanceInBlocks)
            {
                return true;
            }
        }

        return false;
    }
}
