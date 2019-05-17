package org.jared.dungeoncrawler.generation.states;

import com.google.common.collect.Lists;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.data.Tuple;
import org.jared.dungeoncrawler.api.generation.IGenerationContext;
import org.jared.dungeoncrawler.api.generation.ImageState;
import org.jared.dungeoncrawler.api.generation.State;
import org.jared.dungeoncrawler.api.generation.cell.CellUtil;
import org.jared.dungeoncrawler.api.generation.cell.IHallway;
import org.jared.dungeoncrawler.api.generation.cell.IRoom;
import org.jared.dungeoncrawler.api.generation.delaunay.DT_Point;
import org.jared.dungeoncrawler.api.generation.delaunay.EdgeList;
import org.jared.dungeoncrawler.api.maps.MapConstants;
import org.jared.dungeoncrawler.api.maps.util.MapImageUtil;
import org.jared.dungeoncrawler.api.pathfinding.AStar;
import org.jared.dungeoncrawler.api.pathfinding.PathingResult;
import org.jared.dungeoncrawler.api.pathfinding.Tile;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.api.util.Direction;
import org.jared.dungeoncrawler.generation.cell.Hallway;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ListIterator;

public class HallwayState implements State, ImageState
{
    @Override
    public int order()
    {
        return 10;
    }

    @Override
    public void run(IGenerationContext generationContext)
    {
        DungeonCrawler.LOG.info("Generating Hallways...");

        List<EdgeList.Edge> minTree = generationContext.getMinTree();
        List<IHallway> hallways = generationContext.getHallways();

        BufferedImage oldImage = null;
        int size = minTree.size();
        while (size > 0)
        {
            EdgeList.Edge edge = minTree.remove(size - 1);
            Vector start = getVectorFromDTPoint(edge.getP1());
            Vector end = getVectorFromDTPoint(edge.getP2());

            IRoom cell = CellUtil.getCell(start, generationContext.getRooms());
            IRoom otherCell = CellUtil.getCell(end, generationContext.getRooms());
            IHallway hallway = new Hallway(cell, otherCell);
            hallways.add(hallway);

            Tuple<Vector, Vector> closestPoints = CellUtil.getClosestRingPoints(cell.getAABB(), otherCell.getAABB(), 3, 0);

            cell.getDoorways().add(closestPoints.getA());
            otherCell.getDoorways().add(closestPoints.getB());

            start = closestPoints.getA();
            end = closestPoints.getB();

            try
            {
                AStar aStar = new AStar(start, end, 5000);
                List<Tile> route = aStar.iterate();

                PathingResult result = aStar.getPathingResult();
                switch (result)
                {
                    case SUCCESS:
                        oldImage = createImage(generationContext.getRooms(), getVectorPath(start, route), oldImage);
                        generationContext.getMapContext().offerImage(oldImage, MapConstants.DEFAULT_TICK_DELAY);

                        buildPath(route, hallway, generationContext, start);
                        break;
                    case NO_PATH:
                        break;
                }
            } catch (AStar.InvalidPathException e)
            {
                e.printStackTrace();
            }

            size--;
        }

        generationContext.setState(new BuildState());
        generationContext.runState();
    }

    private List<Vector> getVectorPath(Vector start, List<Tile> tilePath)
    {
        List<Vector> vectorPath = Lists.newArrayList();

        tilePath.forEach(tile -> vectorPath.add(start.clone().add(tile.toVector())));

        return vectorPath;
    }

    private void buildPath(List<Tile> route, IHallway hallway, IGenerationContext context, Vector start)
    {
        List<Vector> vectorRoute = getVectorPath(start, route);

        ListIterator<Vector> vectorIterator = vectorRoute.listIterator();
        while (vectorIterator.hasNext())
        {
            Vector vector = vectorIterator.next();

            for (int x = -2; x <= 2; x++)
            {
                for (int z = -2; z <= 2; z++)
                {
                    Vector offset = vector.clone().add(new Vector(x, 0, z));

                    IRoom cell = CellUtil.getCell(offset, context.getRooms());
                    if (cell != null)
                    {
                        if (!context.getIntersectingCells().contains(cell))
                        {
                            context.getIntersectingCells().add(cell);
                            continue;
                        }

                        continue;
                    }

                    vectorIterator.add(offset);
                    hallway.getFloor().add(offset);
                }
            }
        }

        ListIterator<Vector> edgeIterator = vectorRoute.listIterator();
        while (edgeIterator.hasNext())
        {
            Vector vector = edgeIterator.next();

            for (Direction direction : Direction.getMainDirections())
            {
                Vector check = vector.clone().add(direction.toVector());

                if (!vectorRoute.contains(check))
                {
                    IRoom cell = CellUtil.getCell(check, context.getRooms());
                    if (cell != null)
                    {
                        continue;
                    }

                    int height = 10;
                    while (height > 0)
                    {
                        Vector offset = check.clone().add(new Vector(0, height, 0));

                        edgeIterator.add(offset);
                        hallway.getWalls().add(offset);
                        height--;
                    }
                }
            }
        }

    }

    private Vector getVectorFromDTPoint(DT_Point p)
    {
        return new Vector((int) p.x(), 0, (int) p.y());
    }

    private BufferedImage createImage(List<IRoom> rooms, List<Vector> path, BufferedImage oldImage)
    {
        if (oldImage == null)
        {
            oldImage = new BufferedImage(MapConstants.SCALE_FACTOR, MapConstants.SCALE_FACTOR, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D graphics2D = oldImage.createGraphics();
            graphics2D.setColor(Color.BLACK);
            graphics2D.fillRect(0, 0, MapConstants.SCALE_FACTOR, MapConstants.SCALE_FACTOR);

            for (IRoom room : rooms)
            {
                int scaledX = getScaledPosition(room.getAABB().getMinX(), oldImage.getWidth());
                int scaledZ = getScaledPosition(room.getAABB().getMinZ(), oldImage.getHeight());

                graphics2D.setPaint(Color.BLUE);
                graphics2D.fillRect(
                        scaledX,
                        scaledZ,
                        room.getAABB().getWidth() + 1, //fixing scale proportions
                        room.getAABB().getDepth() + 1 //fixing scale proportions
                );

                graphics2D.setPaint(Color.WHITE);
                graphics2D.drawRect(scaledX, scaledZ, room.getWidth(), room.getDepth());
            }
        }
        else
        {
            oldImage = MapImageUtil.resizeImage(oldImage, MapConstants.SCALE_FACTOR, MapConstants.SCALE_FACTOR);
        }

        Graphics2D graphics2D = oldImage.createGraphics();

        graphics2D.setPaint(new Color(157, 24, 60));
        for (Vector point : path)
        {
            graphics2D.drawRect(getScaledPosition(point.getBlockX(), oldImage.getWidth()), getScaledPosition(point.getBlockZ(), oldImage.getHeight()), 1, 1);
        }

        return oldImage;
    }
}

