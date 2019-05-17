package org.jared.dungeoncrawler.api.structures;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.util.VectorUtil;

import java.util.List;

public class AABB implements Cloneable
{
    public Vector center;
    public int minX;
    public int minY;
    public int minZ;
    public int maxX;
    public int maxY;
    public int maxZ;

    public AABB(int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
    {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public int getMinX()
    {
        return minX;
    }

    public int getMaxX()
    {
        return maxX;
    }

    public int getMinY()
    {
        return minY;
    }

    public int getMaxY()
    {
        return maxY;
    }

    public int getMinZ()
    {
        return minZ;
    }

    public int getMaxZ()
    {
        return maxZ;
    }

    public Vector getMin()
    {
        return new Vector(minX, minY, minZ);
    }

    public void setCenter(int x, int y, int z)
    {
        int halfWidth = getWidth() / 2;
        int halfHeight = getHeight() / 2;
        int halfDepth = getDepth() / 2;

        this.minX = x - halfWidth;
        this.minZ = z - halfDepth;
        this.maxX = x + halfWidth;
        this.maxZ = z + halfDepth;
    }

    public Vector getCenter()
    {
        int xCenter = minX + (getWidth() / 2);
        int zCenter = minZ + (getDepth() / 2);
        int yCenter = minY + (getHeight() / 2);

        return new Vector(xCenter, yCenter, zCenter);
    }

    public void setMin(Vector min)
    {
        this.minX = min.getBlockX();
        this.minY = min.getBlockY();
        this.minZ = min.getBlockZ();
    }

    public Vector getMax()
    {
        return new Vector(maxX, maxY, maxZ);
    }

    public void setMax(Vector max)
    {
        this.maxX = max.getBlockX();
        this.maxY = max.getBlockY();
        this.maxZ = max.getBlockZ();
    }

    public int getBottomCenterZ()
    {
        return minZ + (getDepth() / 2);
    }

    public int getBottomCenterX()
    {
        return minX + (getWidth() / 2);
    }

    public int getArea()
    {
        return getWidth() * getDepth();
    }

    public int getVolume()
    {
        return getWidth() * getDepth() * getHeight();
    }

    public int getWidth()
    {
        return Math.abs(maxX - minX);
    }

    public int getHeight()
    {
        return Math.abs(maxY - minY);
    }

    public int getDepth()
    {
        return Math.abs(maxZ - minZ);
    }

    public Vector[] getBaseCorners()
    {
        Vector corner1 = new Vector(maxX, 0, maxZ);
        Vector corner2 = new Vector(maxX, 0, minZ);
        Vector corner3 = new Vector(minX, 0, maxZ);
        Vector corner4 = new Vector(minX, 0, minZ);

        return new Vector[]{corner1, corner2, corner3, corner4};
    }

    public List<Vector> getAllPoints()
    {
        List<Vector> points = Lists.newArrayList();

        for (int x = (int) minX; x <= maxX; x++)
        {
            for (int y = (int) minY; y <= maxY; y++)
            {
                for (int z = (int) minZ; z <= maxZ; z++)
                {
                    points.add(new Vector(x, y, z));
                }
            }
        }

        return points;
    }

    public List<Vector> getBasePoints()
    {
        List<Vector> points = Lists.newArrayList();

        for (int x = (int) minX; x <= maxX; x++)
        {
            for (int z = (int) minZ; z <= maxZ; z++)
            {
                points.add(new Vector(x, minY, z));
            }
        }

        return points;
    }

    public List<Vector> getFacePoints(Face... faces)
    {
        List<Vector> points = Lists.newArrayList();

        for (int x = (int) minX; x <= maxX; x++)
        {
            for (int y = (int) minY; y <= maxY; y++)
            {
                for (int z = (int) minZ; z <= maxZ; z++)
                {
                    for (Face face : faces)
                    {
                        switch (face)
                        {
                            case CORNERS:
                                if (x == minX && z == minZ ||
                                        x == minX && z == maxZ ||
                                        x == maxX && z == minZ ||
                                        x == maxX && z == maxZ)
                                {
                                    points.add(new Vector(x, y, z));
                                }
                                break;
                            case WALLS:
                                if (x == minX ||
                                        z == minZ ||
                                        x == maxX ||
                                        z == maxZ)
                                {
                                    points.add(new Vector(x, y, z));
                                }
                                break;
                            case TOP:
                                if (y == maxY)
                                {
                                    points.add(new Vector(x, y, z));
                                }
                                break;
                            case BOTTOM:
                                if (y == minY)
                                {
                                    points.add(new Vector(x, y, z));
                                }
                                break;
                        }
                    }
                }
            }
        }

        return points;
    }

    public void grow(int xa, int ya, int za)
    {
        this.minX -= xa;
        this.minY -= ya;
        this.minZ -= za;
        this.maxX += xa;
        this.maxY += ya;
        this.maxZ += za;
    }

    public AABB getAABB(Vector position)
    {
        int x = position.getBlockX();
        int y = position.getBlockY();
        int z = position.getBlockZ();

        return new AABB(x, y, z, x + maxX, y + maxY, z + maxZ);
    }

    public boolean intersects(AABB otherAABB)
    {
        if ((otherAABB.maxX < this.minX) || (otherAABB.minX > this.maxX)) return false;
        if ((otherAABB.maxY < this.minY) || (otherAABB.minY > this.maxY)) return false;
        if ((otherAABB.maxZ < this.minZ) || (otherAABB.minZ > this.maxZ)) return false;
        return true;
    }

    public boolean intersects(List<Vector> vectors)
    {
        for (Vector vector : vectors)
        {
            if (intersects(vector))
                return true;
        }

        return false;
    }

    public boolean intersects(Vector vector)
    {
        int x = vector.getBlockX();
        int y = vector.getBlockY();
        int z = vector.getBlockZ();

        Vector[] real = getMaxTest();
        Vector min = real[0];
        Vector max = real[1];

        if (x >= min.getBlockX() && x <= max.getBlockX() &&
                y >= min.getBlockY() && y <= max.getBlockY() &&
                z >= min.getBlockZ() && z <= max.getBlockZ())
        {
            return true;
        }

        return false;
    }

    private Vector[] getMaxTest()
    {
        int xMax = maxX, yMax = maxY, zMax = maxZ;
        int xMin = minX, yMin = minY, zMin = minZ;

        if (minX > maxX)
        {
            xMax = minX;
            xMin = maxX;
        }

        if (minY > maxY)
        {
            yMax = minY;
            yMin = maxY;
        }

        if (minZ > maxZ)
        {
            zMax = minZ;
            zMin = maxZ;
        }

        return new Vector[]{new Vector(xMin, yMin, zMin), new Vector(xMax, yMax, zMax)};
    }

    public AABB rotateAlongY(Vector position, int angle)
    {
        Vector min = new Vector(minX, minY, minZ);
        Vector max = new Vector(maxX, maxY, maxZ);

        Vector rotatedMin = VectorUtil.rotateVector(min, angle);
        Vector rotatedMax = VectorUtil.rotateVector(max, angle);

        rotatedMin = position.clone().add(rotatedMin);
        rotatedMax = position.clone().add(rotatedMax);

        return new AABB(rotatedMin.getBlockX(), rotatedMin.getBlockY(), rotatedMin.getBlockZ(), rotatedMax.getBlockX(), rotatedMax.getBlockY(), rotatedMax.getBlockZ());
    }

    public void move(int xa, int ya, int za)
    {
        this.minX += xa;
        this.minY += ya;
        this.minZ += za;
        this.maxX += xa;
        this.maxY += ya;
        this.maxZ += za;
    }

    public String toString(Location base)
    {
        Location minClone = base.clone().add(minX, minY, minZ);
        Location maxClone = base.clone().add(maxX, maxY, maxZ);

        return minClone + ",    " + maxClone;
    }

    @Override
    public AABB clone()
    {
        try
        {
            return (AABB) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public enum Face
    {
        TOP,
        BOTTOM,
        WALLS,
        CORNERS
    }
}
