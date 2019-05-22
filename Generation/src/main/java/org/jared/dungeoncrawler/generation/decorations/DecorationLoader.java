package org.jared.dungeoncrawler.generation.decorations;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.concurrency.Callback;
import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.api.generation.cell.IRoom;
import org.jared.dungeoncrawler.api.generation.decorations.IDecoration;
import org.jared.dungeoncrawler.api.material.IMaterialAndData;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.api.structures.AABB;
import org.jared.dungeoncrawler.api.util.VectorUtil;
import org.jared.dungeoncrawler.generation.decorations.states.IDecorationLoadingState;
import org.jared.dungeoncrawler.generation.decorations.states.preparation.BigPreparationState;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DecorationLoader
{
    private IGenerationContext generationContext;
    public Callback<Boolean> callback;

    private IDecorationLoadingState state;

    public DecorationLoader(IGenerationContext generationContext, Callback<Boolean> callback)
    {
        this.generationContext = generationContext;
        this.callback = callback;
        setState(new BigPreparationState());
        runState();
    }

    public void setState(IDecorationLoadingState state)
    {
        this.state = state;
    }

    public void runState()
    {
        state.run(generationContext, this);
    }

    public Vector getSuitablePosition(IDecoration decoration, IRoom cell)
    {
        List<Vector> basePoints = cell.getAABB().getAllPoints();
        AABB cellAABB = cell.getAABB().clone();
        cellAABB.grow(-1, -1, -1);

        List<Vector> allPoints = cellAABB.getAllPoints();
        basePoints.removeAll(allPoints);

        List<Vector> doorwayPoints = Lists.newArrayList();
        for (Vector doorway : cell.getDoorways())
        {
            doorwayPoints.addAll(VectorUtil.getDoorwayVectors(doorway));
        }
        basePoints.addAll(doorwayPoints);

        //idk a better way of doing this
        //decorations only get added to the cell when it's placed
        //thus meaning decoration.getPosition() will never be null
        List<IDecoration> decorations = cell.getDecorations();
        for (IDecoration cellDecoration : decorations)
        {
            if (!cellDecoration.hasBeenPlaced())
                continue;
            else
            {
                DungeonCrawler.LOG.warning("PLACED");
            }

            Map<Vector, IMaterialAndData> points = cellDecoration.getStructure().getPoints(cellDecoration.getPosition());

            for (Map.Entry<Vector, IMaterialAndData> pointEntry : points.entrySet())
            {
                Vector vector = pointEntry.getKey();
                IMaterialAndData materialAndData = pointEntry.getValue();

                if (materialAndData.getMaterial() != Material.AIR)
                {
                    basePoints.add(vector);
                }
            }
        }

        List<Vector> walls = cellAABB.getFacePoints(AABB.Face.WALLS);
        List<Vector> corners = cellAABB.getFacePoints(AABB.Face.CORNERS);
        List<Vector> top = cellAABB.getFacePoints(AABB.Face.TOP);
        List<Vector> bottom = cellAABB.getFacePoints(AABB.Face.BOTTOM);

        Collections.shuffle(allPoints);
        for (Vector point : allPoints)
        {
            Vector returnPoint = null;

            List<Rotation> rotations = Lists.newArrayList(Rotation.values());
            Collections.shuffle(rotations);
            for (Rotation rotation : rotations)
            {
                int angle = rotation.angle;

                List<Vector> rotatedEdgeCases = decoration.getStructure().getRotatedEdgeCases(point, angle);

                if (!rotatedEdgeCases.isEmpty())
                {
                    if (!isNearWalls(walls, rotatedEdgeCases))
                        continue;
                }

                AABB rotated = decoration.getStructure().getTestAABB(point, angle);

                if (rotated.intersects(basePoints))
                    continue;

                switch (decoration.getType())
                {
                    case WALL:
                        if (rotated.intersects(walls))
                            returnPoint = point;
                        break;
                    case CORNER:
                        if (rotated.intersects(corners))
                            returnPoint = point;
                        break;
                    case CEILING_CORNER:
                        if (rotated.intersects(top) && rotated.intersects(corners))
                            returnPoint = point;
                        break;
                    case CEILING_WALL:
                        if (rotated.intersects(top) && rotated.intersects(walls))
                            returnPoint = point;
                        break;
                    case FLOOR:
                        if (rotated.intersects(bottom))
                            returnPoint = point;
                        break;
                    case FLOOR_CORNER:
                        if (rotated.intersects(bottom) && rotated.intersects(corners))
                            returnPoint = point;
                        break;
                    case FLOOR_WALL:
                        if (rotated.intersects(bottom) && rotated.intersects(walls))
                            returnPoint = point;
                        break;
                }

                if (returnPoint != null)
                {
                    decoration.setRotation(angle);
                    decoration.setPosition(returnPoint);

                    return returnPoint;
                }
            }
        }

        return null;
    }

    private boolean isNearWalls(List<Vector> walls, List<Vector> edges)
    {
        List<Boolean> booleans = Lists.newArrayList();

        for (Vector edge : edges)
        {
            if (walls.contains(edge))
            {
                booleans.add(true);
            }
            else
            {
                booleans.add(false);
            }
        }

        return isAllTrue(booleans);
    }

    private boolean isAllTrue(List<Boolean> booleans)
    {
        for (Boolean bool : booleans)
        {
            if (bool != true)
            {
                return false;
            }
        }

        return true;
    }

    private void shuffleLists(List... lists)
    {
        for (List list : lists)
        {
            Collections.shuffle(list);
        }
    }

    /*
    public boolean isNearCorner(Bounds bounds, List<Vector> testPoints)
    {
        for (Direction direction : Direction.values())
        {
            for (Vector testPoint : testPoints)
            {
                Vector testOffset = testPoint.clone().add(direction.toVector());

                List<Vector> corners = bounds.getCorners();
                for (Vector vector : corners)
                {
                    Vector offset = vector.clone().setY(testOffset.getBlockY());

                    if (testOffset.equals(offset))
                        return true;
                }
            }
        }

        return false;
    }
    */

    public boolean isNearHeight(List<Vector> testPoints, int height)
    {
        for (Vector testPoint : testPoints)
        {
            if (testPoint.getBlockY() == height)
                return true;
        }

        return false;
    }

    private List<Vector> getPoints(IRoom cell, IDecoration decoration)
    {


        switch (decoration.getType())
        {
            case WALL:

        }

        return null;
    }

    enum Rotation
    {
        NONE(0),
        NINETY(90),
        ONE_EIGHTY(180),
        TWO_SEVENTY(270),;

        int angle;

        Rotation(int angle)
        {
            this.angle = angle;
        }
    }
}
