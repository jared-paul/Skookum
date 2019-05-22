package org.jared.dungeoncrawler.generation.states;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.data.Tuple;
import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.api.generation.State;
import org.jared.dungeoncrawler.api.generation.block.BlockPlaceTask;
import org.jared.dungeoncrawler.api.generation.cell.CellUtil;
import org.jared.dungeoncrawler.api.generation.cell.IHallway;
import org.jared.dungeoncrawler.api.generation.cell.IRoom;
import org.jared.dungeoncrawler.api.generation.decorations.IDecoration;
import org.jared.dungeoncrawler.api.material.IMaterialAndData;
import org.jared.dungeoncrawler.api.material.MaterialAndData;
import org.jared.dungeoncrawler.api.material.XMaterial;
import org.jared.dungeoncrawler.api.pathfinding.AStar;
import org.jared.dungeoncrawler.api.pathfinding.PathingResult;
import org.jared.dungeoncrawler.api.pathfinding.Tile;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.api.structures.AABB;
import org.jared.dungeoncrawler.api.util.Direction;
import org.jared.dungeoncrawler.api.util.VectorUtil;
import org.jared.dungeoncrawler.generation.cell.sorter.CellBaseSorter;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class HiddenRoomState implements State
{
    @Override
    public int order()
    {
        return 0;
    }

    @Override
    public void run(IGenerationContext generationContext)
    {
        List<IRoom> cells = generationContext.getRooms();
        cells.sort(new CellBaseSorter(generationContext.getBaseVector()));
        List<IHallway> hallways = generationContext.getHallways();

        ThreadLocalRandom random = ThreadLocalRandom.current();

        IRoom cell = cells.get(random.nextInt(cells.size()));

        while (cell.equals(cells.get(0)))
        {
            cell = cells.get(random.nextInt(cells.size()));
        }

        List<Vector> wallPoints = cell.getAABB().getFacePoints(AABB.Face.WALLS);
        List<Vector> cornerPoints = cell.getAABB().getFacePoints(AABB.Face.CORNERS);
        wallPoints.removeAll(cornerPoints);

        List<Vector> doorwayPoints = Lists.newArrayList();
        for (Vector doorway : cell.getDoorways())
        {
            doorwayPoints.addAll(VectorUtil.getDoorwayVectors(doorway));
        }

        Collections.shuffle(wallPoints);

        Vector point = null;
        Vector center = null;

        int radius = 3;
        int height = 5;

        for (Vector vector : wallPoints)
        {
            if (vector.getY() != 1 || doorwayPoints.contains(vector) || isNearDecorations(cell, vector))
                continue;

            center = getCenter(vector, cells, hallways);

            if (center == null)
                continue;

            center = center.clone().subtract(new Vector(0, 1, 0));
            point = vector;
        }

        if (point != null && center != null)
        {
            List<Tuple<Vector, IMaterialAndData>> blockData = Lists.newArrayList();

            int x = center.getBlockX();
            int y = center.getBlockY();
            int z = center.getBlockZ();
            AABB aabb = new AABB(x - radius, y, z - radius, x + radius, y + height, z + radius);

            Vector otherPoint = CellUtil.getClosestRingPoint(point, aabb, 1, 0);
            List<Tuple<Vector, IMaterialAndData>> line = getLine(point, otherPoint);
            blockData.addAll(line);

            for (AABB.Face face : AABB.Face.values())
            {
                if (face == AABB.Face.CORNERS)
                    continue;

                for (Vector vector : aabb.getFacePoints(face))
                {
                    blockData.add(new Tuple<>(vector, new MaterialAndData(XMaterial.NETHER_BRICKS.parseMaterial().getMaterial())));
                }
            }

            blockData.addAll(getDoorway(point));
            blockData.addAll(getDoorway(otherPoint.clone().add(new Vector(0 , 1, 0))));
            blockData.add(new Tuple<>(center.clone().add(new Vector(0, 1, 0)), new MaterialAndData(Material.CHEST)));

            generationContext.getBlockPlacer().addTask(new BlockPlaceTask(generationContext.getBaseLocation(), blockData, null));
        }

        generationContext.setState(new FinishState());
        generationContext.runState();
    }

    private List<Tuple<Vector, IMaterialAndData>> getDoorway(Vector point)
    {
        MaterialAndData materialAndData = new MaterialAndData(Material.AIR);
        return Lists.newArrayList(new Tuple<>(point.clone(), materialAndData), new Tuple<>(point.clone().add(new Vector(0, 1, 0)), materialAndData));
    }

    private List<Tuple<Vector, IMaterialAndData>> getLine(Vector start, Vector end)
    {
        List<Tuple<Vector, IMaterialAndData>> line = Lists.newArrayList();

        try
        {
            AStar aStar = new AStar(start, end, 1000);

            List<Tile> route = aStar.iterate();

            PathingResult result = aStar.getPathingResult();

            switch (result)
            {
                case SUCCESS:
                    for (Tile tile : route)
                    {
                        Vector vector = start.clone().add(tile.toVector());

                        if (vector.equals(start))
                            continue;

                        line.add(new Tuple<>(vector, new MaterialAndData(XMaterial.NETHER_BRICKS.parseMaterial().getMaterial())));
                    }
            }
        } catch (AStar.InvalidPathException e)
        {
            e.printStackTrace();
        }

        /*
        for (Direction direction : Direction.getMainDirections())
        {
            for (int depth = 0; depth < 2; depth++)
            {
                Vector offset = end.clone().add(direction.toVector().add(new Vector(0, depth, 0)));
                line.add(new Tuple<>(offset, new MaterialAndData(Material.AIR)));
            }
        }
        */

        for (Tuple<Vector, IMaterialAndData> dataTuple : getDoorway(end))
        {
            line.add(new Tuple<>(dataTuple.getA().clone().add(new Vector(0, 1, 0)), dataTuple.getB()));
        }

        return line;
    }

    private Vector getCenter(Vector vector, List<IRoom> cells, List<IHallway> hallways)
    {
        int radius = 3;
        int height = 5;

        for (Direction direction : Direction.getMainDirections())
        {
            Vector offset = direction.toVector().normalize().multiply(7); //multiply(7) means add 7 to vector magnitude since they're all 1
            Vector center = vector.clone().add(offset);

            int x = center.getBlockX();
            int y = center.getBlockY();
            int z = center.getBlockZ();

            AABB aabb = new AABB(x - radius, y, z - radius, x + radius, y + height, z + radius);

            if (!intersects(aabb, cells))
            {
                DungeonCrawler.LOG.warning(offset);
                return center;
            }
        }

        return null;
    }

    private boolean intersects(AABB aabb, List<IRoom> cells)
    {
        for (IRoom cell : cells)
        {
            if (aabb.intersects(cell.getAABB()))
                return true;
        }

        return false;
    }

    private boolean intersects(Vector vector, List<IRoom> cells, List<IHallway> hallways)
    {
        for (IRoom cell : cells)
        {
            if (cell.getAABB().intersects(vector))
                return true;
        }

        for (IHallway hallway : hallways)
        {
            for (Vector floorPoint : hallway.getFloor())
            {
                if (floorPoint.distanceSquared(vector) <= (5 * 5)) //5 blocks
                    return true;
            }
        }

        return false;
    }

    private boolean isNearDecorations(IRoom cell, Vector vector)
    {
        for (IDecoration decoration : cell.getDecorations())
        {
            if (!decoration.hasBeenPlaced())
                continue;

            Set<Vector> decorationPoints = decoration.getStructure().getPoints(decoration.getPosition()).keySet();

            for (Vector decorationPoint : decorationPoints)
            {
                if (decorationPoint.distanceSquared(vector) <= 1)
                    return true;
            }
        }

        return false;
    }


}
